package org.birdhelpline.app.utils;

import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class WebUtils {

    @Autowired
    private UserService userService;

    private User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByUserName(auth.getName());
    }

    public static User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
