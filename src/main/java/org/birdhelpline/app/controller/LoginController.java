package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.RoleService;
import org.birdhelpline.app.service.TaskService;
import org.birdhelpline.app.service.UserService;
import org.birdhelpline.app.service.UserTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@SessionAttributes({"user"})
public class LoginController {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserTaskService userTaskService;

    @RequestMapping(value = {"/", "/error"}, method = RequestMethod.GET)
    public ModelAndView welcome(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("forward:/default");
        }
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        session.setAttribute("user", user);
        modelAndView.setViewName("Index");
        return modelAndView;
    }

    @RequestMapping(value = {"/signIn"}, method = RequestMethod.GET)
    public ModelAndView signIn(@RequestParam(name = "error", required = false) Boolean error) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("forward:/default");
        }
        ModelAndView modelAndView = new ModelAndView();
        if (error != null && error) {
            modelAndView.addObject("error", "Invalid credentials");
        }
        modelAndView.setViewName("SignIn");
        return modelAndView;
    }




//    @Bean
//    public MultipartResolver multipartResolver() {
//        return new CommonsMultipartResolver();
//    }

    @RequestMapping(path = "/registration", method = {RequestMethod.GET, RequestMethod.POST})
    public String processPage(@RequestParam(name = "page", required = false) final Integer currentPage,
                              @RequestParam(name = "dp-image", required = false) MultipartFile file,
//                              MultipartFile file,
                              final @ModelAttribute("user") User user, BindingResult result,
                              final HttpSession session,
                              SessionStatus status,
                              @RequestParam(name = "action", required = false) String action) throws IOException {
        logger.info(" vkj inside reg : " + user + " " + currentPage);
        logger.info("vkj action : " + action);
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
                return handleStep3(file, user, status, action,result);
            //TODO create this page
            default:
                return "error/UndefinedPage";
        }
    }

//    private String handleStep3(@RequestParam(name = "image", required = false) MultipartFile file, @ModelAttribute("user") User user, SessionStatus status, @RequestParam(name = "action", required = false) String action, BindingResult result) throws IOException {
    private String handleStep3(MultipartFile file,  User user, SessionStatus status, String action, BindingResult result) throws IOException {

        String userName = user.getUserName();
        User fromDb = userService.findUserByUserName(userName);
        if(fromDb != null) {
            result.rejectValue("userName", "userName.invalid", "UserName is already taken up");
            return "RequestRegistration/Step3";
        }
        if (action != null) {
            //user.setImage(file.getBytes());
            if (action.equalsIgnoreCase("preview")) {
                return "RequestRegistration/Preview";
            }
        }

        userService.saveUser(user);
        status.setComplete();
        return "RequestRegistration/reg-complete";
    }

    private String handleStep2(@ModelAttribute("user") User user, BindingResult result) {
        long mobile = user.getMobile();
        if (mobile < 0 || String.valueOf(mobile).length() != 10) {
            result.rejectValue("mobile", "mobile.invalid", "Mobile no is not valid");
            return "RequestRegistration/Step2";
        }

        if (userService.getUser(mobile)) {
            result.rejectValue("mobile", "mobile.already.registred", "Mobile no is already registered");
            return "RequestRegistration/Step2";
        }

        String email = user.getEmail();

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.find()) {
            result.rejectValue("email", "email.invalid", "Email is not valid");
            return "RequestRegistration/Step2";
        }

        return "RequestRegistration/Step3";
    }

    @RequestMapping(path = "/profileCompletion", method = RequestMethod.POST)
    public ModelAndView processProfileCompletionPage(@RequestParam(name = "page", required = false) final Integer currentPage,
                              final @ModelAttribute("user") User user, BindingResult result,
                              final HttpSession session,
                              SessionStatus status,
                              @RequestParam(name = "action", required = false) String action) throws IOException {
        logger.info("vkj inside reg : " + user + " " + currentPage);
        logger.info("vkj action : " + action);
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage == null) {
            modelAndView.setViewName("Profile-Completion/step1");
            return modelAndView;
        }
        switch (currentPage) {
            case 1:
                handleProfileStep1(modelAndView,user, result); break;
            case 2:
                handleProfileStep2(modelAndView,user, result); break;
            case 3:
                handleProfileStep3(modelAndView,user, status, action,result); break;
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

    private void handleProfileStep3(ModelAndView modelAndView, User user, SessionStatus status, String action, BindingResult result) {
        modelAndView.setViewName("Admin");
    }

    @RequestMapping(path = "/forgotPassword", method = {RequestMethod.GET})
    public String forgotPassword() throws IOException {
        logger.info(" vkj inside forgot password ");
        //user.setImage(file.getBytes());
        return "ForgotPassword";
    }

 @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("403");
        return modelAndView;
    }


    @RequestMapping(path = "/profilePicUpload", method = RequestMethod.POST)
    public @ResponseBody String profilePicUpload(@RequestParam(name = "image", required = false) MultipartFile file,
                                   @RequestPart(name = "image" , required = false) MultipartFile file1,
                              final HttpSession session, final @ModelAttribute("user") User user, BindingResult result
                              ) throws IOException {
       logger.info("Received data 1 : "+file);
       logger.info("Received data  2: "+file1);
       file1.getBytes();
       //return file.getBytes();
        user.setImage(file.getBytes());
       return Base64.getEncoder().encodeToString(file.getBytes());
    }
}
