package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.UserDao;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoPostgres implements UserDao {
    Connection connection = null;

    public UserDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<User>();
        try {
            Statement st = connection.createStatement();
            String query = "SELECT * FROM user_vt;";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                boolean admin = rs.getBoolean("admin");
                User user = new User(id, email, username, password, admin);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }

        return users;
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public void add(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(User user) {

    }
}
