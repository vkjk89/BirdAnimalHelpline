//package org.birdhelpline.app.service;
//
//import org.birdhelpline.app.dataaccess.UserDao;
//import org.birdhelpline.app.domain.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcOperations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//
//@Repository
//public class DefaultUserService implements UserService {
//    private final UserDao userDao;
//    private final JdbcOperations jdbcOperations;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public DefaultUserService(
//                                  final UserDao userDao,
//                                  final JdbcOperations jdbcOperations,
//                                  final PasswordEncoder passwordEncoder) {
//       if (userDao == null) {
//            throw new IllegalArgumentException("userDao cannot be null");
//        }
//        if (jdbcOperations == null) {
//            throw new IllegalArgumentException("jdbcOperations cannot be null");
//        }
//        if (passwordEncoder == null) {
//            throw new IllegalArgumentException("passwordEncoder cannot be null");
//        }
//        this.userDao = userDao;
//        this.jdbcOperations = jdbcOperations;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public User getUser(int id) {
//        return userDao.getUser(id);
//    }
//
//    @Override
//    public User findUserByMobile(int mobile) {
//        return userDao.findUserByMobile(mobile);
//    }
//
//
//    public long createUser(User user) {
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//        long userId = userDao.createUser(user);
//        jdbcOperations.update("insert into calendar_user_authorities(calendar_user,authority) values (?,?)", userId,
//                "ROLE_USER");
//        return userId;
//    }
//}