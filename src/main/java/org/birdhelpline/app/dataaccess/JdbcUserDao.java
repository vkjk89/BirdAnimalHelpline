package org.birdhelpline.app.dataaccess;

import org.birdhelpline.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcUserDao implements UserDao {
    Logger logger = LoggerFactory.getLogger(JdbcUserDao.class);
    private static final String USER_QUERY = "select u.user_id as user_id , ui.first_name,ui.last_name,u.email,u.mobile from user u , user_info ui where ";
    private static final RowMapper<User> USER_MAPPER = new UserRowMapper();
    private final JdbcOperations jdbcOperations;
    //private final JdbcTemplate jdbcTemplate;
    private Map<String,Integer> userRoleVsRoleId = new HashMap<>();
    private Map<Integer,String> securityQIdVSSecurityQ = new HashMap<>();

    //@Autowired
    //private DataSource dataSource;

    @PostConstruct
    private  void init() {
        jdbcOperations.query("select authority_name,authority_id from authority", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                userRoleVsRoleId.put(resultSet.getString("authority_name"),resultSet.getInt("authority_id"));
            }
        });

        logger.info("VKJ : "+userRoleVsRoleId);

        jdbcOperations.query("select authority_name,authority_id from authority", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                userRoleVsRoleId.put(resultSet.getString("authority_name"),resultSet.getInt("authority_id"));
            }
        });

        logger.info("VKJ : "+userRoleVsRoleId);

    }

    @Autowired
    public JdbcUserDao(JdbcOperations jdbcOperations) {
        if (jdbcOperations == null) {
            throw new IllegalArgumentException("jdbcOperations cannot be null");
        }
        this.jdbcOperations = jdbcOperations;
        //this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(long id) {
        return jdbcOperations.queryForObject(USER_QUERY + "u.user_id = ui.user_id and u.user_id = ?", USER_MAPPER, id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByMobile(int mobile) {
        try {
            return jdbcOperations.queryForObject(USER_QUERY + "u.user_id = ui.user_id and ui.mobile = ?", USER_MAPPER, mobile);
        } catch (EmptyResultDataAccessException notFound) {
            return null;
        }
    }

    @Override
    @Transactional
    public long createUser(final User userToAdd) throws SQLException {
        if (userToAdd == null) {
            throw new IllegalArgumentException("userToAdd cannot be null");
        }
        if (userToAdd.getUserId() != 0) {
            throw new IllegalArgumentException("userToAdd.getId() must be null when creating a " + User.class.getName());
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        long userId = insertUser(userToAdd, keyHolder);
        insertUserInfo(userId, userToAdd);
        //insertUserAuthority(userId, userToAdd.getRole());
        return userId;
    }

    private void insertUserAuthority(long userId, String role) {
        this.jdbcOperations.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into user_authority (user_id,authority_id) values (?,?)"
                );

                ps.setLong(1, userId);
                ps.setInt(2,userRoleVsRoleId.get(role));
                return ps;
            }
        });


    }

    private void insertUserInfo(long userId, User userToAdd) throws SQLException {

        this.jdbcOperations.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into user_info (user_id,first_name,last_name,address,gender,dob,role,image,security_id,security_ans) values (?,?, ?,?,?,?,?,?,?,?)"
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
            }
        });

    }

    private long insertUser(User userToAdd, KeyHolder keyHolder) throws SQLException {
        this.jdbcOperations.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into user (user_name,password,email,mobile) values (?,?, ?, ?)",
                        new String[]{"user_id"});

                ps.setString(1, userToAdd.getUserName());
                ps.setString(2, userToAdd.getEncryptedPassword());
                ps.setString(3, userToAdd.getEmail());
                ps.setLong(4, userToAdd.getMobile());
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean getUserByMobile(long mobile) {
        int count = jdbcOperations.queryForObject("select count(1) from user where mobile = ?",Integer.class,mobile);
        return count > 0 ? true:false;
    }

    public boolean getUserByUserName(String userName) {
        int count = jdbcOperations.queryForObject("select count(1) from user where user_name = ?",Integer.class,userName);
        return count > 0 ? true:false;
    }

    static class UserRowMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setMobile(rs.getInt("mobile"));
            return user;
        }
    }

    ;
}