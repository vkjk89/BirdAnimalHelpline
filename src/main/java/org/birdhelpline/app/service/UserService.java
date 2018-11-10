package org.birdhelpline.app.service;

import org.birdhelpline.app.dataaccess.JdbcUserDao;
import org.birdhelpline.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;


@Service("userService")
public class UserService {

    @Autowired
    private JdbcUserDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


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

    public User findUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    public boolean firstTimeLogin(String name) {
        Timestamp lastLoginDate = userDao.getLastLoginByUserName(name);
        if(lastLoginDate == null) {
            return true;
        }
        userDao.insertLastLoginDate(name);
        return false;
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
