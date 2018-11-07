//package org.birdhelpline.app.service;
//
//import org.birdhelpline.app.domain.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//
///**
// * An implementation of {@link UserContext} that looks up the {@link User} using the Spring Security's
// * {@link Authentication} by principal name.
// *
// * @author Rob Winch
// *
// */
//@Component
//public class SpringSecurityUserContext implements UserContext {
//    private final UserService userService;
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public SpringSecurityUserContext(UserService userService, UserDetailsService userDetailsService) {
//        if (userService == null) {
//            throw new IllegalArgumentException("userService cannot be null");
//        }
//        if (userDetailsService == null) {
//            throw new IllegalArgumentException("userDetailsService cannot be null");
//        }
//        this.userService = userService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    /**
//     * Get the {@link User} by obtaining the currently logged in Spring Security user's
//     * {@link Authentication#getName()} and using that to find the {@link User} by email address (since for our
//     * application Spring Security usernames are email addresses).
//     */
//    @Override
//    public User getCurrentUser() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
//        if (authentication == null) {
//            return null;
//        }
//
//        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();
//        String email = user.getUsername();
////        String email = user.getEmail();
//        if (email == null) {
//            return null;
//        }
//        User result = userService.findUserByMobile(11);
//        if (result == null) {
//            throw new IllegalStateException(
//                    "Spring Security is not in synch with CalendarUsers. Could not find user with email " + email);
//        }
//        return result;
//    }
//
//    @Override
//    public void setCurrentUser(User user) {
//        if (user == null) {
//            throw new IllegalArgumentException("user cannot be null");
//        }
//        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
//                user.getPassword(),userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//}
