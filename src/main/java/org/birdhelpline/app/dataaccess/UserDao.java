package org.birdhelpline.app.dataaccess;

import org.apache.commons.io.IOUtils;
import org.birdhelpline.app.model.PinCodeLandmarkInfo;
import org.birdhelpline.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
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
    Logger logger = LoggerFactory.getLogger(UserDao.class);
    private static final String USER_QUERY = "select u.user_id as user_id , ui.first_name,ui.last_name,u.email,u.mobile from user u , user_info ui where ";
    private static final RowMapper<User> USER_MAPPER = new UserRowMapper();
    private Map<String, Integer> userRoleVsRoleId = new HashMap<>();
    private Map<Integer, String> securityQIdVSSecurityQ = new LinkedHashMap<>();
    private Map<Long, List<String>> pinCodeVsLandMarks = new HashMap<>();
    private List<PinCodeLandmarkInfo> listPincodeLandMarks = new ArrayList<>();
    private List<String> listBirdAnimals = new ArrayList<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private String insertUserInfoQ;
    private String insertUserQ;
    private String getUsersByMobileQ;
    private String getUserByUserNameQ;
    private String updateLastLoginQ;
    private String enableUserQ;

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
                    String landMark = resultSet.getString("landmark");
                    List<String> landMarks = pinCodeVsLandMarks.get(pinCode);
                    if (landMarks == null) {
                        landMarks = new ArrayList<>();
                        pinCodeVsLandMarks.put(pinCode, landMarks);
                    }
                    landMarks.add(landMark);
                    listPincodeLandMarks.add(new PinCodeLandmarkInfo(pinCode, landMark));
                }
        );

        logger.info("VKJ : " + pinCodeVsLandMarks);

        jdbcTemplate.query("select security_q_id, security_q_text from security_q order by 1 ",
                (ResultSet resultSet) ->
                {
                    securityQIdVSSecurityQ.put(
                            resultSet.getInt("security_q_id"),
                            resultSet.getString("security_q_text"));
                }
        );

        logger.info("VKJ : " + securityQIdVSSecurityQ);

        listBirdAnimals = jdbcTemplate.queryForList("select bird_animal_name from bird_animal", String.class);

        logger.info("VKJ : " + listBirdAnimals);

    }

    public List<String> getListBirdAnimals() {
        return listBirdAnimals;
    }



    public Map<Long, List<String>> getPinCodeVsLandMarks() {
        return pinCodeVsLandMarks;
    }

    public List<PinCodeLandmarkInfo> getPinCodeLandMarks() {
        return listPincodeLandMarks;
    }

    @Transactional(readOnly = true)
    public User getUser(long id) {
        return jdbcTemplate.queryForObject(USER_QUERY + "u.user_id = ui.user_id and u.user_id = ?", USER_MAPPER, id);
    }

    @Transactional(readOnly = true)
    public User findUserByMobile(int mobile) {
        try {
            return jdbcTemplate.queryForObject(USER_QUERY + "u.user_id = ui.user_id and ui.mobile = ?", USER_MAPPER, mobile);
        } catch (EmptyResultDataAccessException notFound) {
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
        //insertUserAuthority(userId, userToAdd.getRole());
        return userId;
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
            insertUserInfoQ = "insert into user_info (user_id,first_name,last_name,address,gender,dob,role,image,security_id,security_ans) values (?,?, ?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(
                    insertUserInfoQ
            );

            ps.setLong(1, userId);
            ps.setString(2, userToAdd.getFirstName());
            ps.setString(3, userToAdd.getLastName());
            ps.setString(4, userToAdd.getAddress());
            ps.setString(5, userToAdd.getGender());
            ps.setString(6, userToAdd.getDob());
            ps.setString(7, userToAdd.getRole());
            ps.setBlob(8, new ByteArrayInputStream(userToAdd.getImage()));
            ps.setInt(9, userToAdd.getSecurityQId());
            ps.setString(10, userToAdd.getSecurityQAns());
            return ps;
        });

    }

    private long insertUser(User userToAdd, KeyHolder keyHolder) throws SQLException {
        this.jdbcTemplate.update((connection -> {
            insertUserQ = "insert into user (user_name,password,email,mobile) values (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(
                    insertUserQ,
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
        getUsersByMobileQ = "select count(1) from user where mobile = ?";
        int count = jdbcTemplate.queryForObject(getUsersByMobileQ, Integer.class, mobile);
        return count > 0 ? true : false;
    }

    public User getUserByUserName(String userName) {
        User user = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userName", userName);
            getUserByUserNameQ = "select * from user u , user_info ui where u.user_name = :userName and u.user_id = ui.user_id";
            user = namedParameterJdbcTemplate.queryForObject(
                    getUserByUserNameQ,
                    params, new UserRowMapper()
            );
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
        return user;
        //return jdbcOperations.queryForObject("select * from user where user_name = ?",User.class, userName,new UserRowMapper());
    }


    public void updateLastLoginDate(String name) {
        this.jdbcTemplate.update(connection -> {
                    updateLastLoginQ = "update user_info ui, user u set ui.last_login_date =  now() where u.user_id = ui.user_id and u.user_name=?";
                    PreparedStatement ps = connection.prepareStatement(
                            updateLastLoginQ);

                    ps.setString(1, name);
                    return ps;
                }
        );
    }

    public void enableUser(User user) {
        this.jdbcTemplate.update(connection -> {
                    enableUserQ = "update user u set enabled =  1 where u.user_name=?";
                    PreparedStatement ps = connection.prepareStatement(
                            enableUserQ);

                    ps.setString(1, user.getUserName());
                    return ps;
                }
        );
    }

    public Map<Integer, String> getSecurityQs() {
        return securityQIdVSSecurityQ;
    }

    static class UserRowMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
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
            try {
                Blob image = rs.getBlob("image");
                if (image != null) {
                    user.setImage(IOUtils.toByteArray(image.getBinaryStream()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setSecurityQId(rs.getInt("security_id"));
            user.setSecurityQAns(rs.getString("security_ans"));

            return user;
        }
    }

    ;
}