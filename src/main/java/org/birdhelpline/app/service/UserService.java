package org.birdhelpline.app.service;

import org.birdhelpline.app.dataaccess.UserDao;
import org.birdhelpline.app.model.PinCodeLandmarkInfo;
import org.birdhelpline.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("userService")
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public long saveUser(User user) {
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            return userDao.createUser(user);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return 0;
        }


    }

    public Map<Integer, String> getSecurityQs() {
        return userDao.getSecurityQs();

    }


    public List<PinCodeLandmarkInfo> getPinCodeLandMarks(String term) {
        List<PinCodeLandmarkInfo> list = userDao.getPinCodeLandMarks();
        return list.stream().filter(p -> p.getLandmark().toLowerCase().indexOf(term) >= 0).collect(Collectors.toList());

    }

    public boolean findUserByMobile(long mobile) {
        return userDao.getUserByMobile(mobile);
    }

    public User findUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

//    public boolean isFirstTimeLogin(String name) {
//        User user = findUserByUserName(name);
//        if (user == null || user.getLastLoginDate() == null || user.getLoginCount() == 0) {
//            return true;
//        }
//        userDao.updateLastLoginDate(name);
//        return false;
//    }

    public void enableUser(String userName) {
        User user = userDao.getUserByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }
        logger.info("Enabling user : " + user);
        userDao.enableUser(user);
        userDao.insertUserAuthority(user.getUserId(), user.getRole());

    }

    public List<String> getListBirds() {
        return userDao.getListBirds();
    }

    public List<String> getListAnimals() {
        return userDao.getListAnimals();
    }

    public void saveUserAddrPinDetails(User user) {
        logger.info(" vkj : " + user);
        userDao.saveUserAddrPinDetails(user);
    }

    public User validateForgotPasswdDetails(String dob, String mobile, String securityQ, String securityA) {
        return userDao.getUserWithForgotPasswd(dob, mobile, securityQ, securityA);
    }

    public void setNewPassword(Long userId, String newPasswd) {
        userDao.setNewPassword(userId, bCryptPasswordEncoder.encode(newPasswd));
    }

    public List<User> getUserList(String term) {
        return userDao.getUserByTerm(term);
    }
}
