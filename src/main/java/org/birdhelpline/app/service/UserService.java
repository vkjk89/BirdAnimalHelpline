package org.birdhelpline.app.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.birdhelpline.app.dataaccess.UserDao;
import org.birdhelpline.app.model.PinCodeLandmarkInfo;
import org.birdhelpline.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("userService")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${cache.expiry.time.hours}")
    private long cacheExpiryHours;
    @Value("${cache.refresh.time.mins}")
    private long cacheRefreshMins;

    private LoadingCache<Long, Optional<User>> userCache;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private Runnable cacheStatsPrinter = () -> {
        CacheStats cacheStats = userCache.stats();
        logger.info(cacheStats.toString());
    };


    @PostConstruct
    public void init() {
        userCache = CacheBuilder.newBuilder()
                .expireAfterAccess(cacheExpiryHours, TimeUnit.HOURS)
                .refreshAfterWrite(cacheRefreshMins, TimeUnit.MINUTES)
                //.expireAfterWrite(5L, TimeUnit.MINUTES)
                .maximumSize(500L)
                .recordStats()
                //.removalListener(new TradeAccountRemovalListener())
                //.ticker(Ticker.systemTicker())
                .build(new CacheLoader<Long, Optional<User>>() {
                    @Override
                    public Optional<User> load(Long key) {
                        return userDao.getUser(key);
                    }
                });

        scheduledExecutorService.scheduleAtFixedRate(cacheStatsPrinter,0,15,TimeUnit.MINUTES);
    }

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
        try {
            Optional<User> value =  userCache.get(userId);
            if(value.isPresent()){
                return value.get();
            }
        } catch (ExecutionException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
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

    public List<User> getUsersPendingForActivation() {
        return userDao.getUsersPendingForActivation();
    }

    public boolean birdOrAnimalExists(String newBirdAnimal) {
        return userDao.birdOrAnimalExists(newBirdAnimal);
    }

    public void addBird(String newBirdAnimal) {
        userDao.addBird(newBirdAnimal);
    }

    public void addAnimal(String newBirdAnimal) {
        userDao.addAnimal(newBirdAnimal);
    }

    public List<User> getTop5Vol() {
        return userDao.getTop5Vol();
    }

    public List<User> getNearestVol(String locationPincode) {
        return userDao.getNearestVol(locationPincode);
    }
}