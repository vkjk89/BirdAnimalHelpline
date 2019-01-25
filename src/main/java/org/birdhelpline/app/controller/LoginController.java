package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.User;
import org.birdhelpline.app.model.UserServiceTimeInfo;
import org.birdhelpline.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@SessionAttributes({"user"})
public class LoginController {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(value = {"/", "/error"}, method = RequestMethod.GET)
    public ModelAndView welcome(HttpSession session) {
        if (checkIfUserLoggedIn()) return new ModelAndView("forward:/default");
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        session.setAttribute("user", user);
        modelAndView.setViewName("Index");
        return modelAndView;
    }

    @RequestMapping(value = {"/signIn"}, method = RequestMethod.GET)
    public ModelAndView signIn(@RequestParam(name = "error", required = false) Boolean error) {
        if (checkIfUserLoggedIn()) return new ModelAndView("forward:/default");
        ModelAndView modelAndView = new ModelAndView();
        if (error != null && error) {
            modelAndView.addObject("error", "Invalid credentials");
        }
        modelAndView.setViewName("SignIn");
        return modelAndView;
    }

    private boolean checkIfUserLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !(auth instanceof AnonymousAuthenticationToken);
    }


    @RequestMapping(path = "/registration", method = {RequestMethod.GET, RequestMethod.POST})
    public String processPage(@RequestParam(name = "page", required = false) final Integer currentPage,
                              @ModelAttribute("user") User user, BindingResult result,
                              SessionStatus status,
                              @RequestParam(name = "action", required = false) String action,
                              Model model) {
        logger.info(" vkj inside reg : " + user + " " + currentPage);
        logger.info("vkj action : " + action);
        model.addAttribute("securityQs", userService.getSecurityQs());

        if (currentPage == null) {
            return "RequestRegistration/Step1";
        }
        switch (currentPage) {
            case 0:
                return "RequestRegistration/Step1";
            case 1:
                return "RequestRegistration/Step2";
            case 2:
                return handleStep2(user, result);
            case 3:
                return handleStep3(user, status, action, result);
            //TODO create this page
            default:
                return "error/UndefinedPage";
        }
    }

    private String handleStep3(User user, SessionStatus status, String action, BindingResult result) {
        String userName = user.getUserName();
        User fromDb = userService.findUserByUserName(userName);
        if (fromDb != null) {
            result.rejectValue("userName", "userName.invalid", "UserName is already taken up");
            return "RequestRegistration/Step3";
        }
        if (action != null) {
            if (action.equalsIgnoreCase("preview")) {
                return "RequestRegistration/Preview";
            }
        }

        long userId = userService.saveUser(user);
        if (userId == 0) {
            return "Error";
        } else {
            status.setComplete();
        }

        return "RequestRegistration/reg-complete";
    }

    private String handleStep2(@ModelAttribute("user") User user, BindingResult result) {
        long mobile = user.getMobile();
        if (mobile < 0 || String.valueOf(mobile).length() != 10) {
            result.rejectValue("mobile", "mobile.invalid", "Mobile no is not valid");
            return "RequestRegistration/Step2";
        }

        if (userService.findUserByMobile(mobile)) {
            result.rejectValue("mobile", "mobile.already.registred", "Mobile no is already registered");
            return "RequestRegistration/Step2";
        }

        String email = user.getEmail();

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.find()) {
            result.rejectValue("email", "email.invalid", "Email is not valid");
            return "RequestRegistration/Step2";
        }

        user.setSecurityQIdStr(userService.getSecurityQs().get(user.getSecurityQId()));
        return "RequestRegistration/Step3";
    }

    @ModelAttribute("user")
    private User getUserObj() {
        return new User();
    }

    @RequestMapping(path = "/profileCompletion", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView processProfileCompletionPage(@RequestParam(name = "page", required = false) final Integer currentPage,
                                                     final @ModelAttribute("user") User user, BindingResult result,
                                                     SessionStatus status,
                                                     @RequestParam(name = "action", required = false) String action,
                                                     HttpServletRequest request,
                                                     RedirectAttributes redirectAttrs) {
        logger.info("vkj inside profile comple : " + user + " " + currentPage);
        logger.info("vkj action : " + action);

        ModelAndView modelAndView = new ModelAndView();
        if (currentPage == null) {
            //modelAndView.setViewName("Vol-dashboard");
            modelAndView.setViewName("Profile-Completion/step1");
            return modelAndView;
        }
        switch (currentPage) {
            case 1:
                handleProfileStep1(modelAndView, user, result);
                break;
            case 2:
                handleProfileStep2(modelAndView, user, result);
                break;
            case 3:
                handleProfileStep3(modelAndView, user, request, redirectAttrs);
                break;
            //TODO create this page
            default:
                modelAndView.setViewName("Error");

        }
        return modelAndView;
    }

    private void handleProfileStep1(ModelAndView modelAndView, User user, BindingResult result) {
        modelAndView.setViewName("Profile-Completion/step2");
    }

    private void handleProfileStep2(ModelAndView modelAndView, User user, BindingResult result) {
        modelAndView.setViewName("Profile-Completion/step3");
    }

    private void
    handleProfileStep3(ModelAndView modelAndView, User user, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        logger.info("vkj request 1: " + request.getParameterMap().keySet());
        logger.info("vkj request 2: " + request.getParameterMap().values());
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            logger.info(entry.getKey() + " : " + Arrays.toString(entry.getValue()));
        }
        populateUserServiceTimeInfo(user, request);
        userService.saveUserAddrPinDetails(user);
        redirectAttrs.addFlashAttribute("profileCompleted", true);
        modelAndView.setViewName("redirect:/default");
    }

    private void populateUserServiceTimeInfo(User user, HttpServletRequest request) {
        List<UserServiceTimeInfo> list = new ArrayList<>();
        UserServiceTimeInfo serviceTimeInfo = null;
        String[] selectedPinCodeIds = request.getParameterMap().get("pincodeId");
        String[] selectedTimings = request.getParameterMap().get("selectedTiming");

        if (selectedPinCodeIds != null && selectedTimings != null && selectedPinCodeIds.length == selectedTimings.length) {
            for (int i = 0; i < selectedPinCodeIds.length; i++) {
                serviceTimeInfo = new UserServiceTimeInfo();
                serviceTimeInfo.setPincodeId(Long.parseLong(selectedPinCodeIds[i]));
                String[] time = selectedTimings[i].trim().split("-");
                serviceTimeInfo.setFromTime(Integer.parseInt(time[0].trim()));
                serviceTimeInfo.setToTime(Integer.parseInt(time[1].trim()));
                list.add(serviceTimeInfo);
            }
        }
        user.setUserServiceTimeInfos(list);
        logger.info("vkj user service time : " + list);
    }

    @RequestMapping(path = "/forgotPassword", method = {RequestMethod.GET})
    public String forgotPassword(Model model) throws IOException {
        logger.info(" vkj inside forgot password ");
        model.addAttribute("securityQs", userService.getSecurityQs());
        return "forgot-password";
    }


    @RequestMapping(path = "/validateForgotPasswdDetails", method = {RequestMethod.POST})
    public @ResponseBody
    String validateForgotPasswordDetails(@RequestParam("birthdate") String dob,
                                         @RequestParam("contact-number") String mobile,
                                         @RequestParam("securityquestion") String securityQ,
                                         @RequestParam("securityanswer") String securityA,
                                         HttpSession session) {
        logger.info(" vkj inside valiate forgot password ");
        User user = userService.validateForgotPasswdDetails(dob, mobile, securityQ, securityA);
        logger.info("User is : " + user);
        if (user != null) {
            session.setAttribute("userId", user.getUserId());
        }
        return user != null ? "valid" : "invalid";
    }

    @RequestMapping(path = "/forgotPassword", method = {RequestMethod.POST})
    public String forgotPasswordPost(@RequestParam("password") String newPasswd, HttpSession session, Model model) throws IOException {
        logger.info(" vkj inside set new password ");
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            userService.setNewPassword(userId, newPasswd);
            model.addAttribute("error", "Password reset successfully");
            return "SignIn";
        } else {
            return "Error";
        }
    }

    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("403");
        return modelAndView;
    }


    @RequestMapping(path = "/profilePicUpload", method = RequestMethod.POST)
    public @ResponseBody
    String profilePicUpload(@RequestParam(name = "dp-image", required = false) MultipartFile file,
                            final HttpSession session, final @ModelAttribute("user") User user
    ) throws IOException {
        logger.info("Received data 1 : " + file);
        user.getUserImage().setImage(file.getBytes());
        return Base64.getEncoder().encodeToString(user.getUserImage().getImage());
    }
}
