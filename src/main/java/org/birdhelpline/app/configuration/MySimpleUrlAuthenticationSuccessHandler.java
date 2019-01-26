package org.birdhelpline.app.configuration;

import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component("myAuthenticationSuccessHandler")
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) {
        System.out.println("vkj inside login");
        HttpSession session = request.getSession();
        if (session != null) {
            User user = userService.findUserByUserName(authentication.getName());
            if (user == null) {
                throw new UsernameNotFoundException(authentication.getName());
            }
            session.setAttribute("user", user);
        }
    }
}