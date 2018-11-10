package org.birdhelpline.app.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.mysql.jdbc.Blob;
import org.birdhelpline.app.model.Role;
import org.birdhelpline.app.model.Task;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.model.UserTask;
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


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@SessionAttributes({"user"})
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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


    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @RequestMapping(path = "/registration", method = {RequestMethod.GET, RequestMethod.POST})
    public String processPage(@RequestParam(name = "page", required = false) final Integer currentPage,
                              @RequestParam(name = "image", required = false) MultipartFile file,
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
        if(userService.findUserByUserName(userName)) {
            result.rejectValue("userName", "userName.invalid", "UserName is already taken up");
            return "RequestRegistration/Step3";
        }
        if (action != null) {
            user.setImage(file.getBytes());
            if (action.equals("Preview")) {
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

    @RequestMapping(path = "/registrationPreview", method = {RequestMethod.GET})
    public String registrationPreview(@RequestParam(name = "page", required = false) final Integer currentPage,
                                      @RequestParam(name = "image", required = false) MultipartFile file,
                                      final @ModelAttribute("user") User user, BindingResult result,
                                      final HttpSession session,
                                      SessionStatus status) throws IOException {
        logger.info(" vkj inside reg preview : " + user + " " + currentPage);
        user.setImage(file.getBytes());
        return "RequestRegistration/Preview";
    }

    @RequestMapping(path = "/forgotPassword", method = {RequestMethod.GET})
    public String registrationPreview() throws IOException {
        logger.info(" vkj inside forgot password ");
        //user.setImage(file.getBytes());
        return "ForgotPassword";
    }

//    @RequestMapping(value = "/registration1", method = RequestMethod.GET)
//    public ModelAndView registration1(HttpSession session) {
//        logger.info("vkj session 1 " + session.getId());
//        ModelAndView modelAndView = new ModelAndView();
//        User user = new User();
//        modelAndView.addObject("user", user);
//        modelAndView.setViewName("RequestRegistration/Step1");
//        session.setAttribute("user", user);
//        return modelAndView;
//    }
//
//
//    @RequestMapping(value = "/registration2", method = RequestMethod.GET)
//    public ModelAndView registration2(@Valid User user2, @RequestParam String role, HttpSession session, ModelAndView modelAndView) {
//        logger.info("vkjs2 " + session.getId());
//        User user = (User) session.getAttribute("user");
//        logger.info("vkj : " + role);
//        //user.getUserInfo().setRole(role);
//        logger.info(String.valueOf(user));
//        logger.info(String.valueOf(user2));
//        modelAndView.addObject("user", user);
//        modelAndView.setViewName("RequestRegistration/Step2");
//        logger.info("model " + modelAndView.getModel().get("user"));
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/registration3", method = RequestMethod.GET)
//    public ModelAndView registration3(@Valid User user2, ModelAndView modelAndView, HttpSession session) {
//        logger.info("vkjs2 " + session.getId());
//        User user = (User) session.getAttribute("user");
//        logger.info(String.valueOf(user));
//        logger.info(String.valueOf(user2));
//        modelAndView.setViewName("RequestRegistration/Step3");
//        modelAndView.addObject("user", user);
//        logger.info("model " + modelAndView.getModel().get("user"));
//        return modelAndView;
//    }
//
//    //@RequestMapping(value = "/registration", method = RequestMethod.POST)
//    public ModelAndView createNewUser(@Valid User user2, HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        logger.info(String.valueOf(user));
//        logger.info(String.valueOf(user2));
//        ModelAndView modelAndView = new ModelAndView();
//        User userExists = userService.findUserByEmail(user.getEmail());
//
//        userService.saveUser(user);
//        modelAndView.addObject("successMessage", "Registration Successful.");
//        modelAndView.addObject("user", new User());
//        modelAndView.setViewName("index");
//
//        return modelAndView;
//    }


    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("403");
        return modelAndView;
    }


    /*@RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        Role role = new Role();
        Role role2 = new Role();
        role = roleService.findRole("ADMIN");
        role2 = roleService.findRole("USER");
        List<User> users = new ArrayList<>();
        List<User> users2 = new ArrayList<>();
        users = userService.findUserbyRole(role);
        users2 = userService.findUserbyRole(role2);
        List<Task> tasks = new ArrayList<>();
        tasks = taskService.findAll();
        int taskCount = tasks.size();
        int adminCount = users.size();
        int userCount = users2.size();
        modelAndView.addObject("adminCount", adminCount);//Authentication for NavBar
        modelAndView.addObject("userCount", userCount);//Authentication for NavBar
        modelAndView.addObject("taskCount", taskCount);//Authentication for NavBar
        //-----------------------------------------
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("control", loginUser.getRole().getRole());//Authentication for NavBar
        modelAndView.addObject("auth", loginUser);
        List<UserTask> userTasks = new ArrayList<>();
        userTasks = userTaskService.findByUser(loginUser);
        modelAndView.addObject("userTaskSize", userTasks.size());
        modelAndView.setViewName("home");
        return modelAndView;
    }*/
}
