package org.birdhelpline.app.controller;

import com.paytm.pg.merchant.CheckSumServiceHelper;
import org.birdhelpline.app.model.DonateVO;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.UserService;
import org.birdhelpline.app.utils.Role;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


@Controller
public class DefaultController {
    private static final List<String> ROLE_NOT_REQ_PROFILE_COMP = Arrays.asList(Role.ADMIN.name(), Role.Receptionist.name());
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);
    @Autowired
    UserService userService;
    @Value("${paytm.mid}")
    private String mid;
    @Value("${paytm.mkey}")
    private String mkey;
    @Value("${paytm.website}")
    private String website;
    @Value("${paytm.itype}")
    private String itype;
    @Value("${paytm.channelid}")
    private String channelid;
    @Value("${paytm.turl}")
    private String turl;
    @Value("${paytm.tStatus}")
    private String tStatus;
    @Value("${paytm.callback}")
    private String callback;

    Timer timer = new Timer();

    @GetMapping(value = "/donate")
    public ModelAndView handleDonate() {
        return new ModelAndView("doanteRequest");
    }


    public String verifyTxn(String orderId) throws Exception {
        String responseData = "";
        /* initialize a TreeMap object */
        TreeMap<String, String> paytmParams = new TreeMap<String, String>();

        /* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
        paytmParams.put("MID", mid);

        /* Enter your order id which needs to be check status for */
        paytmParams.put("ORDERID", orderId);

        /**
         * Generate checksum by parameters we have
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */
        String checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(mkey, paytmParams);

        /* put generated checksum value here */
        paytmParams.put("CHECKSUMHASH", checksum);

        /* prepare JSON string for request */
        JSONObject obj = new JSONObject(paytmParams);
        String post_data = obj.toString();

        /* for Staging */
        URL url = new URL(tStatus);

        /* for Production */
        // URL url = new URL("https://securegw.paytm.in/order/status");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
                System.out.println("vkj : " + responseData);
            }
            System.out.append("Request: " + post_data);
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error(exception.getMessage(),exception);
        }
        return responseData;
    }

    public String processDonate(DonateVO donateVO) throws Exception {
        String orderId = UUID.randomUUID().toString();
        donateVO.setOrderId(orderId);
        Long id = userService.saveDonateVO(donateVO);
        donateVO.setId(id);
        /* initialize a TreeMap object */
        TreeMap<String, String> paytmParams = new TreeMap<>();

        /* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
        paytmParams.put("MID", mid);

        /* Find your WEBSITE in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
        paytmParams.put("WEBSITE", website);

        /* Find your INDUSTRY_TYPE_ID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
        paytmParams.put("INDUSTRY_TYPE_ID", itype);

        /* WEB for website and WAP for Mobile-websites or App */
        paytmParams.put("CHANNEL_ID", channelid);

        /* Enter your unique order id */

        logger.info("order id generated is : " + orderId);
        paytmParams.put("ORDER_ID", orderId);

        /* unique id that belongs to your customer */
        paytmParams.put("CUST_ID", "Donor");

        /* customer's mobile number */
        paytmParams.put("MOBILE_NO", String.valueOf(donateVO.getMobile()));

        /* customer's email */
        //paytmParams.put("EMAIL", "vkjk89@gmail.com");

        /**
         * Amount in INR that is payble by customer
         * this should be numeric with optionally having two decimal points
         */
        paytmParams.put("TXN_AMOUNT", String.valueOf(donateVO.getFinalAmount()));

        /* on completion of transaction, we will send you the response on this URL */
        paytmParams.put("CALLBACK_URL", callback);

        /**
         * Generate checksum for parameters we have
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */
        String checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(mkey, paytmParams);

        /* for Staging */
        String url = turl;

        /* for Production */
        // String url = "https://securegw.paytm.in/order/process";

        /* Prepare HTML Form and Submit to Paytm */
        StringBuilder outputHtml = new StringBuilder();
        outputHtml.append("<html>");
        outputHtml.append("<head>");
        outputHtml.append("<title>Merchant Checkout Page</title>");
        outputHtml.append("</head>");
        outputHtml.append("<body>");
        outputHtml.append("<center><h1>Please do not refresh this page...</h1></center>");
        outputHtml.append("<form method='post' action='" + url + "' name='paytm_form'>");

        for (Map.Entry<String, String> entry : paytmParams.entrySet()) {
            outputHtml.append("<input type='hidden' name='" + entry.getKey() + "' value='" + entry.getValue() + "'>");
        }

        outputHtml.append("<input type='hidden' name='CHECKSUMHASH' value='" + checksum + "'>");
        outputHtml.append("</form>");
        outputHtml.append("<script type='text/javascript'>");
        outputHtml.append("document.paytm_form.submit();");
        outputHtml.append("</script>");
        outputHtml.append("</body>");
        outputHtml.append("</html>");
        return outputHtml.toString();
    }

    @PostMapping(value = "/donate")
    public @ResponseBody
    String
    handleDonateCustom(@ModelAttribute DonateVO donateVO, HttpSession session) throws Exception {
        if(donateVO.getPan() !=null) {
            donateVO.setPan(donateVO.getPan().toUpperCase());
        }
        String response = processDonate(donateVO);
        session.setAttribute("donateVO", donateVO);
        return response;
    }

    @PostMapping(value = "/donateCallback")
    public ModelAndView
    handleDonateCallback(@RequestBody String body, HttpSession session) throws Exception {
        logger.info("Body recved : " + body);
        String recPrefix = "ON/"+getFinYear();
        ModelAndView modelAndView = new ModelAndView("donateCallback");
        String[] parts = body.split("&");
        String dateFormat = "yyyy-MM-dd HH:mm:ss.S";
        String newDateFormat = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat oldDateTimeForm = new SimpleDateFormat(dateFormat);
        SimpleDateFormat newDateTimeForm = new SimpleDateFormat(newDateFormat);
        Map<String, String> map = new HashMap<>();
        for (String x : parts) {
            String[] ps = x.split("=");
            modelAndView.addObject(ps[0], ps[1]);
            map.put(ps[0], ps[1]);
        }
        try {
//            modelAndView.addObject("TXNDATE", newDateTimeForm.format(oldDateTimeForm.parse(map.get("TXNDATE"))));
            modelAndView.addObject("TXNDATE", newDateTimeForm.format(new Date()));
            //map.put("TXNDATE", oldDateTimeForm.format(map.get("TXNDATE")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        DonateVO vo = (DonateVO) session.getAttribute("donateVO");
        if (vo == null) {
            vo = userService.findDonateInfoByOrderId(map.get("ORDERID"));
        }

        if (vo == null) {
            logger.error("Could not save call back info : " + map);
            return modelAndView;
        }
        userService.saveDonateInfo(map, vo.getId());
        vo.setIdStr(recPrefix+"/"+vo.getId());
        modelAndView.addObject("donateVO", vo);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    verifyTxn(map.get("ORDERID"));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, 10 * 1000);
        return modelAndView;
    }

    @GetMapping(value = "/home")
    public String
    handleHome() {
        return "home";
    }

    @GetMapping(value = "/default")
    public ModelAndView handleLogin(HttpServletRequest request, Model model) {
        ModelAndView modelAndView = new ModelAndView("Error");
        Principal principal = request.getUserPrincipal();
        logger.info("Inside handleLogin for user : " + principal.getName());
        logger.info(" vkj model" + model.asMap());

        User user = userService.findUserByUserName(principal.getName());

        if (user == null) {
            return modelAndView;
        }

        HttpSession session = request.getSession();
        modelAndView.addObject("user", user);
        session.setAttribute("user", user);

        logger.info("VKJ user is : " + user);

        Boolean profileCompleted = (Boolean) model.asMap().get("profileCompleted");
        if (profileCompleted != null && profileCompleted) {
            logger.info("From profile completion page so redirecting ..");
            getViewBasedOnRole(modelAndView);
            userService.updateUserLoginDetails(user);
            return modelAndView;
        }

        if (!ROLE_NOT_REQ_PROFILE_COMP.contains(user.getRole()) && (user.getLastLoginDate() == null || user.getLoginCount() == 0)) {
            logger.info("User login for first time so redirecting to profile completion page");
            modelAndView.setViewName("Profile-Completion/step1");
            return modelAndView;
        }

        modelAndView.addObject("birds", userService.getListBirds());
        modelAndView.addObject("animals", userService.getListAnimals());
        getViewBasedOnRole(modelAndView);
        userService.updateUserLoginDetails(user);
        return modelAndView;
    }

    @RequestMapping(value = {"/refunds_policy"}, method = RequestMethod.GET)
    public ModelAndView getRefundPolicy() {
        return new ModelAndView("refunds_policy");
    }

    @RequestMapping(value = {"/terms_and_conditions"}, method = RequestMethod.GET)
    public ModelAndView getTAndC() {
        return new ModelAndView("terms_and_conditions");
    }


    private void getViewBasedOnRole(ModelAndView modelAndView) {
        Collection<? extends GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        logger.info("vkj auths : " + auth);
        for (GrantedAuthority authority : auth) {
            if (authority.getAuthority().equalsIgnoreCase("ADMIN")) {
                modelAndView.setViewName("admin-dashboard");
                break;
            } else if (authority.getAuthority().equalsIgnoreCase("RECEPTIONIST")) {
                modelAndView.setViewName("receptionist-dashboard");
                break;
            } else {
                modelAndView.setViewName("Vol_Dashboard/Vol-dashboard");
                break;
            }
        }
    }

    //paytm link code

    /* String responseData = "";
     *//* initialize an object *//*
    JSONObject paytmParams = new JSONObject();

    *//* body parameters *//*
    JSONObject body = new JSONObject();

    *//* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys *//*
        body.put("mid", "DoPFwD45117944182519");

    *//* Possible value are "GENERIC", "FIXED", "INVOICE" *//*
        body.put("linkType", "FIXED");

    *//* Enter your choice of payment link description here, special characters are not allowed *//*
        body.put("linkDescription", "For donation to Bird Help Line");

    *//* Enter your choice of payment link name here, special characters and spaces are not allowed *//*
        body.put("linkName", "BIRDANIMALHELPLINE");

        body.put("amount", 1000d);

        body.put("customerName", "testCustomerVKJ");

        body.put("customerEmail", "vkjk89@gmail.com");

        body.put("sendSms",true);
        body.put("sendEmail",true);

        body.put("customerMobile", "9029787026");


    *//**
     * Generate checksum by parameters we have in body
     * You can get Checksum JAR from https://developer.paytm.com/docs/v1/payment-gateway/#code
     * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
     *//*
    String checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("NnL9rpmgPfLRCObu", body.toString());

    *//* head parameters *//*
    JSONObject head = new JSONObject();

    *//* This will be AES *//*
        head.put("tokenType", "AES");

    *//* put generated checksum value here *//*
        head.put("signature", checksum);

    *//* prepare JSON string for request *//*
        paytmParams.put("body", body);
        paytmParams.put("head", head);
    String post_data = paytmParams.toString();

    *//* for Staging *//*
    URL url = new URL("https://securegw-stage.paytm.in/link/create");

    *//* for Production *//*
// URL url = new URL("https://securegw.paytm.in/link/create);

        try {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
        requestWriter.writeBytes(post_data);
        requestWriter.close();

        InputStream is = connection.getInputStream();
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
        if ((responseData = responseReader.readLine()) != null) {
            System.out.append("Response: " + responseData);
        }
        // System.out.append("Request: " + post_data);
        responseReader.close();
    } catch (Exception exception) {
        exception.printStackTrace();
    }
        return responseData;*/
    
    private String getFinYear() {
        LocalDate date = LocalDate.now();
        LocalDate date2;
        if(date.getMonthValue() > 3) {
            date2 = date.plusYears(1);
            return date.getYear()+"-"+(date2.getYear()/100);
        }
        else {
            date2 = date.minusYears(1);
            return date2.getYear()+"-"+(date.getYear()/100);
        }
    }
}