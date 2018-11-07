package org.birdhelpline.app.dataaccess;


import org.birdhelpline.app.model.User;

import java.sql.SQLException;

public interface UserDao {

    User getUser(long id);

    User findUserByMobile(int mobile);

    long createUser(User user) throws SQLException;
}
