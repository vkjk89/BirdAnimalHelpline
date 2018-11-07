package org.birdhelpline.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;


@Controller
public class DefaultController {
    Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @GetMapping(value = "/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        logger.info("inside default controller");
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
