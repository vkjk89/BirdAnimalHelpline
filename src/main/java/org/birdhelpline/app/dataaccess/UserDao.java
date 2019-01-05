package org.birdhelpline.app.dataaccess;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.birdhelpline.app.model.PinCodeLandmarkInfo;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.model.UserServiceTimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@Repository
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private Map<String, Integer> userRoleVsRoleId = new HashMap<>();
    private Map<Integer, String> securityQIdVSSecurityQ = new LinkedHashMap<>();
    private List<PinCodeLandmarkInfo> listPincodeLandMarks = new ArrayList<>();
    private List<String> listBirds = new ArrayList<>();
    private List<String> listAnimals = new ArrayList<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${user.all.query}")
    private String userAllQ;

    @Value("${user.all.with.address.query}")
    private String userAllWithAddressQ;

    @Value("${user.basic.query}")
    private String userBasicQ;

    @Value("${user.nearest.pincode.query}")
    private String nearestUserQ;

    @PostConstruct
    private void init() {
        jdbcTemplate.query("select authority_name,authority_id from authority",
                (ResultSet resultSet) ->
                {
                    userRoleVsRoleId.put(
                            resultSet.getString("authority_name"),
                            resultSet.getInt("authority_id"));
                });

        logger.info("VKJ : " + userRoleVsRoleId);

        jdbcTemplate.query("select * from pincode_landmark ",
                (ResultSet resultSet) ->
                {
                    long pinCode = resultSet.getLong("pincode");
                    long pinCodeId = resultSet.getLong("pin_land_id");
                    String landMark = resultSet.getString("landmark");
                    listPincodeLandMarks.add(new PinCodeLandmarkInfo(pinCodeId, pinCode, landMark));
                }
        );

        //logger.info("VKJ : " + listPincodeLandMarks);

        jdbcTemplate.query("select security_q_id, security_q_text from security_q order by 1 ",
                (ResultSet resultSet) ->
                {
                    securityQIdVSSecurityQ.put(
                            resultSet.getInt("security_q_id"),
                            resultSet.getString("security_q_text"));
                }
        );

        logger.info("VKJ : " + securityQIdVSSecurityQ);

        listBirds = jdbcTemplate.queryForList("select bird_animal_name from bird_animal where type ='B'", String.class);
        listAnimals = jdbcTemplate.queryForList("select bird_animal_name from bird_animal where type ='A'", String.class);

        logger.info("VKJ : " + listBirds);
        logger.info("VKJ : " + listAnimals);

    }

    @Transactional(readOnly = true)
    public User getUser(long id) {
        try {
            return jdbcTemplate.queryForObject(userAllQ + " and u.user_id = ?", new UserRowMapper("BI"), id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public User findUserByMobile(int mobile) {
        try {
            return jdbcTemplate.queryForObject(userAllQ + " and ui.mobile = ?", new UserRowMapper("BI"), mobile);
        } catch (EmptyResultDataAccessException notFound) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public User getUserWithForgotPasswd(String dob, String mobile, String securityQ, String securityA) {
        try {
            return jdbcTemplate.queryForObject(userBasicQ + " and ui.dob = ? and u.mobile= ? and ui.security_id =? and ui.security_ans = ?", new UserRowMapper("B"), dob, mobile, securityQ, securityA);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional
    public long createUser(final User userToAdd) throws SQLException {
        if (userToAdd == null) {
            throw new IllegalArgumentException("userToAdd cannot be null");
        }
        if (userToAdd.getUserId() != null && userToAdd.getUserId() != 0) {
            throw new IllegalArgumentException("userToAdd.getId() must be null when creating a " + User.class.getName());
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        long userId = insertUser(userToAdd, keyHolder);
        insertUserInfo(userId, userToAdd);
        insertUserImage(userId, userToAdd);
        //insertUserAuthority(userId, userToAdd.getRole());
        return userId;
    }

    private void insertUserImage(long userId, User userToAdd) {
        this.jdbcTemplate.update((Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into user_image (user_id,image) values (?,?)"
            );
            ps.setLong(1, userId);
            ps.setBlob(2, new ByteArrayInputStream(userToAdd.getUserImage().getImage()));
            return ps;
        });


    }

    public void insertUserAuthority(long userId, String role) {
        this.jdbcTemplate.update((Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into user_authority (user_id,authority_id) values (?,?)"
            );

            ps.setLong(1, userId);
            ps.setInt(2, userRoleVsRoleId.get(role));
            return ps;

        });


    }

    private void insertUserInfo(long userId, User userToAdd) throws SQLException {

        this.jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into user_info (user_id,first_name,last_name,address,gender,dob,role,security_id,security_ans) values (?,?,?,?,?,?,?,?,?)"
            );

            ps.setLong(1, userId);
            ps.setString(2, userToAdd.getFirstName());
            ps.setString(3, userToAdd.getLastName());
            ps.setString(4, userToAdd.getAddress());
            ps.setString(5, userToAdd.getGender());
            ps.setString(6, userToAdd.getDob());
            ps.setString(7, userToAdd.getRole());
            ps.setInt(8, userToAdd.getSecurityQId());
            ps.setString(9, userToAdd.getSecurityQAns());
            return ps;
        });

    }

    private long insertUser(User userToAdd, KeyHolder keyHolder) throws SQLException {
        this.jdbcTemplate.update((connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into user (user_name,password,email,mobile) values (?,?,?,?)",
                    new String[]{"user_id"});

            ps.setString(1, userToAdd.getUserName());
            ps.setString(2, userToAdd.getEncryptedPassword());
            ps.setString(3, userToAdd.getEmail());
            ps.setLong(4, userToAdd.getMobile());
            return ps;
        }), keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean getUserByMobile(long mobile) {
        int count = jdbcTemplate.queryForObject("select count(1) from user where mobile = ?", Integer.class, mobile);
        return count > 0;
    }

    public User getUserByUserName(String userName) {
        User user = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userName", userName);
            user = namedParameterJdbcTemplate.queryForObject(
                    userAllQ + " and u.user_name = :userName ",
                    params, new UserRowMapper("BI")
            );
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
        return user;
    }


    public void enableUser(User user) {
        this.jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "update user u set enabled =  1 where u.user_name=?");

                    ps.setString(1, user.getUserName());
                    return ps;
                }
        );
    }

    @Transactional
    public void saveUserAddrPinDetails(User user) {

        jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into user_addr_info (user_id,full_name,address_line1,address_line2,pincode,alternate_contact,address_type,nature_business) values (?,?,?,?,?,?,?,?)"
            );

            ps.setLong(1, user.getUserId());
            ps.setString(2, user.getHomeAddr().getFullName());
            ps.setString(3, user.getHomeAddr().getAddrLine1());
            ps.setString(4, user.getHomeAddr().getAddrLine2());
            ps.setLong(5, user.getHomeAddr().getPincode());
            ps.setString(6, user.getHomeAddr().getContactPrefix() + "-" + user.getHomeAddr().getContact());
            ps.setString(7, "H");
            ps.setString(8, "");
            return ps;
        });

        jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into user_addr_info (user_id,full_name,address_line1,address_line2,pincode,alternate_contact,address_type,nature_business) values (?,?,?,?,?,?,?,?)"
            );

            ps.setLong(1, user.getUserId());
            ps.setString(2, user.getOfficeAddr().getFullName());
            ps.setString(3, user.getOfficeAddr().getAddrLine1());
            ps.setString(4, user.getOfficeAddr().getAddrLine2());
            ps.setLong(5, user.getOfficeAddr().getPincode());
            ps.setString(6, "");
            // ps.setString(6, user.getOfficeAddr().getContactPrefix() + "-" + user.getOfficeAddr().getContact());
            ps.setString(7, "O");
            ps.setString(8, user.getOfficeAddr().getNatureBusiness());
            return ps;
        });

        for (UserServiceTimeInfo serviceTimeInfo : user.getUserServiceTimeInfos()) {
            jdbcTemplate.update((Connection con) -> {
                PreparedStatement ps = con.prepareStatement(
                        "insert into user_service_time_info (user_id,pin_land_id,from_time,to_time) values (?,?,?,?)"
                );
                ps.setLong(1, user.getUserId());
                ps.setLong(2, serviceTimeInfo.getPincodeId());
                ps.setInt(3, serviceTimeInfo.getFromTime());
                ps.setInt(4, serviceTimeInfo.getToTime());
                return ps;
            });
        }


        jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(
                    "update user_info u set u.last_login_date = now() , u.login_count = u.login_count+1 where u.user_id = ?"
            );
            ps.setLong(1, user.getUserId());
            return ps;
        });


    }

    public void setNewPassword(Long userId, String encode) {
        this.jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "update user u set u.password =  ? where u.user_id = ?");

                    ps.setString(1, encode);
                    ps.setLong(2, userId);
                    return ps;
                }
        );
    }

    public List<User> getUserByTerm(String term) {
        List<User> users = null;
        try {
            UserRowMapper rowMapper = new UserRowMapper("BIA");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userName", "%" + term + "%");
            namedParameterJdbcTemplate.query(
                    userAllWithAddressQ + " where u.user_name like :userName ",
                    params, rowMapper
            );
            return new ArrayList<>(rowMapper.map.values());
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
    }

    public List<User> getTop5Vol() {
        List<User> users = null;
        try {
            UserRowMapper rowMapper = new UserRowMapper("BI");
            namedParameterJdbcTemplate.query(
                    userAllQ + "  order by ui.case_accepted_count desc limit 5", rowMapper
            );
            return new ArrayList<>(rowMapper.map.values());
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
    }

    public List<User> getNearestVol(String locationPincode) {
        List<User> users = null;
        try {
            UserRowMapper rowMapper = new UserRowMapper("BI");
            namedParameterJdbcTemplate.query(
                    nearestUserQ + locationPincode, rowMapper
            );
            return new ArrayList<>(rowMapper.map.values());
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
    }

    static class UserRowMapper implements RowMapper<User> {
        private final String type;
        private Map<Long, User> map = new HashMap<>();

        //B->Basic, I->Image, A->Addr, S->ServiceTime
        public UserRowMapper(String type) {
            this.type = type;
        }


        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long userId = rs.getLong("user_id");
            User user = map.get(userId);
            if (user == null) {
                user = new User();
                map.put(userId, user);
            }
            user.setUserId(userId);
            user.setEmail(rs.getString("email"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setPassword(rs.getString("password"));
            user.setMobile(rs.getLong("mobile"));
            user.setUserName(rs.getString("user_name"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setAddress(rs.getString("address"));
            user.setGender(rs.getString("gender"));
            user.setDob(rs.getString("dob"));
            user.setCreationDate(rs.getTimestamp("create_date"));
            user.setLastLoginDate(rs.getTimestamp("last_login_date"));
            user.setRole(rs.getString("role"));
            user.setSecurityQId(rs.getInt("security_id"));
            user.setSecurityQAns(rs.getString("security_ans"));
            user.setLoginCount(rs.getInt("login_count"));
            user.setCaseAcceptedCount(rs.getLong("case_accepted_count"));
            user.setCaseRejectedCount(rs.getLong("case_rejected_count"));
            if (type.contains("I")) {
                try {
                    Blob image = rs.getBlob("image");
                    if (image != null) {
                        user.getUserImage().setImage(IOUtils.toByteArray(image.getBinaryStream()));
                    }
                    image = rs.getBlob("old_image");
                    if (image != null) {
                        user.getUserImage().setOldImage(IOUtils.toByteArray(image.getBinaryStream()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (type.contains("A")) {
                String typeAddr = rs.getString("address_type");
                if (StringUtils.isNotBlank(typeAddr)) {
                    switch (typeAddr) {
                        case "H":
                            user.getHomeAddr().setAddressType(typeAddr);
                            user.getHomeAddr().setAddrLine1(rs.getString("address_line1"));
                            user.getHomeAddr().setAddrLine2(rs.getString("address_line2"));
                            user.getHomeAddr().setFullName(rs.getString("full_name"));
                            user.getHomeAddr().setPincode(rs.getLong("pincode"));
                            user.getHomeAddr().setContactPrefix(rs.getString("alternate_contact_prefix"));
                            user.getHomeAddr().setContact(rs.getString("alternate_contact"));
                            break;
                        case "O":
                            user.getOfficeAddr().setAddressType(typeAddr);
                            user.getOfficeAddr().setAddrLine1(rs.getString("address_line1"));
                            user.getOfficeAddr().setAddrLine2(rs.getString("address_line2"));
                            user.getOfficeAddr().setFullName(rs.getString("full_name"));
                            user.getOfficeAddr().setPincode(rs.getLong("pincode"));
                            user.getOfficeAddr().setContactPrefix(rs.getString("alternate_contact_prefix"));
                            user.getOfficeAddr().setContact(rs.getString("alternate_contact"));
                            user.getOfficeAddr().setNatureBusiness(rs.getString("nature_business"));
                            break;
                        default:
                            logger.warn("Unknown type Addr : " + typeAddr);
                    }
                }
            }
            return user;
        }
    }

    public List<PinCodeLandmarkInfo> getPinCodeLandMarks() {
        return listPincodeLandMarks;
    }

    public Map<Integer, String> getSecurityQs() {
        return securityQIdVSSecurityQ;
    }

    public List<String> getListBirds() {
        return listBirds;
    }

    public List<String> getListAnimals() {
        return listAnimals;
    }
}