package org.birdhelpline.app.controller;

import org.birdhelpline.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;


@Controller
public class DefaultController {
    Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    UserService userService;

    @GetMapping(value = "/default")
    public String handleLogin(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        logger.info("Inside handleLogin for user : "+ principal.getName());
        if(userService.firstTimeLogin(principal.getName())) {
            logger.info("User login for first time so redirecting to profile completion page");
            return "Profile-Completion/step1";
        }
        System.out.println(request.getUserPrincipal());
        //if(request.isUserInRole())
        Collection<? extends GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority authority : auth) {
            if(authority.getAuthority().equalsIgnoreCase("ADMIN")){
                return "Admin";
            }
        }

        return "Volunteer";
    }
}
