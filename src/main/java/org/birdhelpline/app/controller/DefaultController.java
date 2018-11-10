package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;


@Controller
public class DefaultController {
    Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    UserService userService;

    @GetMapping(value = "/default")
    public ModelAndView handleLogin(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("Error");
        Principal principal = request.getUserPrincipal();
        logger.info("Inside handleLogin for user : "+ principal.getName());
        User user = userService.findUserByUserName(principal.getName());
        if(user == null) {
            //modelAndView.setViewName("Error");
            return modelAndView;
        }
        modelAndView.addObject("user", user);
        logger.info("VKJ user is : "+user);
        if(user.getLastLoginDate() == null) {
            logger.info("User login for first time so redirecting to profile completion page");
            modelAndView.setViewName("Vol-dashboard");
            //modelAndView.setViewName("Profile-Completion/step1");
            return modelAndView;
        }

        Collection<? extends GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority authority : auth) {
            if(authority.getAuthority().equalsIgnoreCase("ADMIN")){
                modelAndView.setViewName("Admin");
                break;
            }
            else if(authority.getAuthority().equalsIgnoreCase("RECEPTIONIST")) {
                modelAndView.setViewName("Receptionist");
                break;
            }
            else {
                modelAndView.setViewName("Vol-dashboard");
                break;
            }
        }
        return  modelAndView;
    }
}
