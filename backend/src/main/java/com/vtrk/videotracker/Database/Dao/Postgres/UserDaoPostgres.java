package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.UserDao;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoPostgres implements UserDao {
    Connection connection = null;

    public UserDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    /*
        This function finds all users in the database and returns a list of users.
        If there are no users, it returns an empty list.

        Still to decide if we need to leave this function here or not.
    */

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

    /*
        This function finds a user by id.
        If the user is found, it returns the user.
        Otherwise, it returns a user with email/username/password = "".
        Check the parameters above to see if the user is found.
    */

    @Override
    public User findById(int id) {
        User user = new User(id, "","", "",false);
        try {
            String query = "SELECT * FROM user_vt WHERE id = "+id+";";
            Statement prst = connection.createStatement();
            ResultSet rs = prst.executeQuery(query);
            while (rs.next()){
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setIs_admin(rs.getBoolean("admin"));
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return user;
    }

    /*
        This function finds a user by email or username and password.
        If the user is found, it returns the user.
        Otherwise, it returns a user with id = 0.
        Check id to see if the user is found.
    */
    @Override
    public User findByEmailOrUsername(String email_username, String password) {
        User user = new User(0, email_username,email_username, password,false);
        try {
            String query = "SELECT * FROM user_vt WHERE (email = '"+email_username+"' or username = '"+email_username+"') and password = '"+password+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setIs_admin(rs.getBoolean("admin"));
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return user;
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
