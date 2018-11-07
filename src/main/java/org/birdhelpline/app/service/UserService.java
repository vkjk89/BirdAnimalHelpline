package org.birdhelpline.app.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.birdhelpline.app.dataaccess.JdbcUserDao;
import org.birdhelpline.app.model.Role;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.repository.RoleRepository;
import org.birdhelpline.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserService {

    @Autowired
    private JdbcUserDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserByEmail(String email) {
        return  null;
    }

    public void saveUser(User user) {
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            userDao.createUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getSecurityQs(User user) {
        //TODO vimal
        // userDao.getSecurityQs()

    }

    public boolean getUser(long mobile) {
        return userDao.getUserByMobile(mobile);
    }

    public boolean findUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }


//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

//    public List<User> findAll() {
//        List<User> users = new ArrayList<>();
//        users = userRepository.findAll();
//        return users;
//    }

//    public User findUser(int id) {
//        return userRepository.findOne(id);
//    }

//    public void delete(int id) {
//        userRepository.delete(id);
//
//    }

    /*public void save(User user) {
        userRepository.save(user);
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void saveUser(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        //user.setRole(userRole);
        userRepository.save(user);
    }

    public List<User> findUserbyRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User findUser(int id) {

        return null;
    }

    public void delete(int id) {
    }*/
}
