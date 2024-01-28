package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.Subject;
import com.vtrk.videotracker.Database.Dao.UserDao;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoPostgres implements UserDao, Subject {
    Connection connection = null;

    public UserDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    /**
        This function finds all users in the database and returns a list of users.<br>
        If there are no users, it returns an empty list.
        @return list of users
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
                boolean banned = rs.getBoolean("banned");
                boolean notification_by_email = rs.getBoolean("notificationByEmail");
                User user = new User(id, email, username, password, admin, banned,notification_by_email);
                users.add(user);
            }
        } catch (SQLException e) {
            //System.out.println("Error in findAll "+ e);
        }
        return users;
    }

    /**
        This function finds a user by id.<br>
        If the user is found, it returns the user.<br>
        Otherwise, it returns a user with email/username/password = "".<br>
        Check the parameters above to see if the user is found.
        @param id id
    */
    @Override
    public User findById(int id) {
        User user = new User(id, "","", "",false,true);
        try {
            String query = "SELECT * FROM user_vt WHERE id = "+id+";";
            Statement prst = connection.createStatement();
            ResultSet rs = prst.executeQuery(query);
            while (rs.next()){
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setIs_admin(rs.getBoolean("admin"));
                user.setIs_banned(rs.getBoolean("banned"));
                user.setNotification_by_email(rs.getBoolean("notificationByEmail"));
            }
        } catch (SQLException e) {
            //System.out.println("Error in findById "+ e);
        }
        return user;
    }

    /**
        This function finds a user by email and password.<br>
        If the user is found, it returns the user.<br>
        Otherwise, it returns a user with id = 0.<br>
        Check id to see if the user is found.<br>
        @param email email
        @param password password
        @return user
    */
    @Override
    public User findByEmail(String email, String password) {
        User user = new User(0, email,"", password,false, true);
        try {
            String query = "SELECT * FROM user_vt WHERE email = '"+email+"' and password = '"+password+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setIs_admin(rs.getBoolean("admin"));
                user.setIs_banned(rs.getBoolean("banned"));
                user.setNotification_by_email(rs.getBoolean("notificationByEmail"));
            }
        } catch (SQLException e) {
            //System.out.println("Error in findByEmail "+ e);
        }
        return user;
    }

    /**
     This function return the value of notificationByEmail by user id.<br>
     If the user is found, it returns the value.<br>
     Otherwise, it returns false.<br>
     @param id id
     @return notification_by_email
     */

    @Override
    public boolean getNotificationByEmail(int id) {
        boolean notification_by_email = false;
        try {
            String query = "SELECT * FROM user_vt WHERE id = "+id+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                notification_by_email = rs.getBoolean("notificationByEmail");
            }
        } catch (SQLException e) {
            //System.out.println("Error in getNotificationByEmail "+ e);
        }
        return notification_by_email;
    }

    @Override
    public List<User> findWhoWantsNotificationByEmail() {
        List<User> users = new ArrayList<User>();
        try {
            Statement st = connection.createStatement();
            String query = "SELECT * FROM user_vt WHERE notificationbyemail = true and banned = false;";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                boolean admin = rs.getBoolean("admin");
                boolean notification_by_email = rs.getBoolean("notificationByEmail");
                User user = new User(id, email, username, password, admin, false, notification_by_email);
                users.add(user);
            }
        } catch (SQLException e) {
            //System.out.println("Error in findAll "+ e);
        }
        return users;
    }

    /**
        This function finds a user by email.<br>
        If the user is found, it returns the user.<br>
        Otherwise, it returns a user with id = 0.<br>
        Check id to see if the user is found.<br>
        @param email email
        @return user
    */
    @Override
    public User findByEmailOnly(String email) {
        User user = new User(0, email,"", "",false,true);
        try {
            String query = "SELECT * FROM user_vt WHERE email = '"+email+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setIs_admin(rs.getBoolean("admin"));
                user.setIs_banned(rs.getBoolean("banned"));
                user.setNotification_by_email(rs.getBoolean("notificationByEmail"));
            }
        } catch (SQLException e) {
            //System.out.println("Error in findByEmail "+ e);
        }
        return user;
    }

    /**
     * Add user
     * @param user user
     */
    @Override
    public void add(User user) {
        try{
            String query = "INSERT INTO public.user_vt (id, email, password, username, admin) VALUES(nextval('user_vt_id_seq'::regclass), '"+user.getEmail()+"', '"+user.getPassword()+"', '"+user.getUsername()+"', "+user.isIs_admin()+");";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in add "+e);
        }
    }

    /**
     * Update user
     * @param user user
     */
    @Override
    public void update(User user) {
        try{
            String query = "UPDATE public.user_vt SET email='"+user.getEmail()+"', password='"+user.getPassword()+"', username='"+user.getUsername()+"', admin="+user.isIs_admin()+" WHERE id="+user.getId()+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in update "+e);
        }
    }

    /**
     * Update user from settings
     * @param id user id
     * @param credential new credential
     * @param choice choice
     */
    @Override
    public void updateFromSettings(int id, String credential, int choice) {
        String query = "";
        switch (choice){
            case 1:
                query = "UPDATE public.user_vt SET email='"+credential+"' WHERE id="+id+";";
                break;
            case 2:
                query = "UPDATE public.user_vt SET password='"+credential+"' WHERE id="+id+";";
                break;
            case 3:
                query = "UPDATE public.user_vt SET username='"+credential+"' WHERE id="+id+";";
                break;
            case 4:
                query = "UPDATE public.user_vt SET notificationbyemail = NOT notificationbyemail WHERE id="+id+";";
                break;
            default:
                break;
        }
        try{
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in updateFromSettings "+e);
        }
    }

    /**
     * Remove user
     * @param id user id
     */
    @Override
    public void remove(int id) {
        try{
            String query = "DELETE FROM public.user_vt WHERE id="+id+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in remove "+e);
        }
    }

    /**
     * Check if given email is already in use
     * @param email email to check
     * @return true if email is already in use, false otherwise
     */
    @Override
    public boolean emailInUse(String email) {
        try {
            String query = "SELECT * FROM user_vt WHERE email = '"+email+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst())
                return false;
        } catch (SQLException e) {
            //System.out.println("Error in emailInUse "+ e);
        }
        return true;
    }

    /**
     * Check if user exists
     * @param id user id
     * @return true if user exists, false otherwise
     */
    @Override
    public boolean exists(int id) {
        try {
            String query = "SELECT * FROM user_vt WHERE id = "+id+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(!rs.isBeforeFirst())
                return false;
        } catch (SQLException e) {
            //System.out.println("Error in findAll "+ e);
        }
        return true;
    }

    /**
     * Request action to be performed
     * @param choice action
     * @param object object
     */
    @Override
    public void request(int choice, Object object) {
        if (choice == 3){
            remove((int)object);
        }else{
            User user = (User) object;
            switch(choice){
                case 1:
                    add(user);
                    break;
                case 2:
                    update(user);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Ban or unban user
     * @param id user id
     */
    @Override
    public void ban(int id) {
        try{
            String query = "UPDATE public.user_vt SET banned= NOT banned WHERE id="+id+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in ban "+e);
        }
    }

    /**
     * Check if user is banned
     * @param id user id
     * @return true if user is banned, false otherwise
     */
    @Override
    public boolean isBanned(int id) {
        try {
            String query = "SELECT * FROM user_vt WHERE id = "+id+" AND banned = true;";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(!rs.isBeforeFirst())
                return false;
        } catch (SQLException e) {
            //System.out.println("Error in isBanned "+ e);
        }
        return true;
    }
}
