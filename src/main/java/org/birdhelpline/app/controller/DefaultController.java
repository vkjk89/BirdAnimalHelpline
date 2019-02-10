package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.UserService;
import org.birdhelpline.app.utils.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Controller
public class DefaultController {
    private static final List<String> ROLE_NOT_REQ_PROFILE_COMP = Arrays.asList(Role.ADMIN.name(),Role.Receptionist.name());
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    UserService userService;

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
}