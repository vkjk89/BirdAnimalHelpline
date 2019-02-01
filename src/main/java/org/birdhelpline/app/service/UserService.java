package org.birdhelpline.app.service;

import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
import java.util.stream.Collectors;


@Service("userService")
public class UserService {
    private static  final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public List<PinCodeLandmarkInfo> getPinCodeLandMarks(String term, String selectedPinCodes) {
        Set<Long> setSelected = new HashSet<>();
        if (StringUtils.isNotBlank(selectedPinCodes)) {
            List<String> list = Arrays.asList(selectedPinCodes.split(",", -1));
            list.stream().forEach(s -> setSelected.add(Long.parseLong(s)));
        }
        List<PinCodeLandmarkInfo> list = userDao.getPinCodeLandMarks();
        return list.stream().filter(p -> !setSelected.contains(p.getPincodeId()) && (p.getLandmark().toLowerCase().indexOf(term) >= 0 || String.valueOf(p.getPincode()).indexOf(term) >= 0)).collect(Collectors.toList());
    }
    public boolean findUserByMobile(long mobile) {
        return userDao.getUserByMobile(mobile);
    }

    public User findUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }
    public User findUserByUserId(Long userId) {
        return userDao.getUser(userId);
    }

    public void enableUser(String userName) {
        User user = userDao.getUserByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }
        logger.info("Enabling user : " + user);
        userDao.enableUser(user);
        userDao.insertUserAuthority(user.getUserId(), user.getRole());
    }

    public Set<String> getListBirds() {
        return userDao.getListBirds();
    }

    public Set<String> getListAnimals() {
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

    public void updateUserLoginDetails(User user) {
        userDao.updateUserLoginDetails(user);
    }
}