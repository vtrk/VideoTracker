package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.Subject;
import com.vtrk.videotracker.Database.Dao.UserListDao;
import com.vtrk.videotracker.Database.Model.UserList;

import java.sql.*;

public class UserListDaoPostgres implements UserListDao, Subject {
    Connection connection = null;

    public UserListDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    /**
        This function finds the list of the user by the id of the user.
        Check id to see if the list is found.
        @param id_user id of the user
        @return UserList
    */
    @Override
    public UserList findByIdUser(int id_user) {
        UserList user_list = new UserList(0, id_user);
        try {
            String query = "SELECT * FROM list WHERE id_user = "+id_user+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                user_list.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            //System.out.println("Error in findAll "+e);
        }
        return user_list;
    }

    /**
        This function finds the list of the user by the id of the list.
        Check id_user to see if the list is found.
        @param id id of the list
        @return UserList
    */
    @Override
    public UserList findById(int id) {
        UserList user_list = new UserList(id, 0);
        try {
            String query = "SELECT * FROM list WHERE id = "+id+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                user_list.setIdUser(rs.getInt("id_user"));
            }
        } catch (SQLException e) {
            //System.out.println("Error in findAll "+e);
        }
        return user_list;
    }

    /**
     * This function adds a list to the database.
     * @param id_user id of the user
     */
    @Override
    public void add(int id_user) {
        try{
            String query = "INSERT INTO public.list (id, id_user) VALUES(nextval('list_id_seq'::regclass), "+id_user+");";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in add "+e);
        }
    }

    /**
     * This function removes a list from the database.
     * @param id_user id of the user
     */
    @Override
    public void remove(int id_user) {
        try{
            String query = "DELETE FROM public.list WHERE id_user="+id_user+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in add "+e);
        }
    }

    public boolean exists(int id_user) {
        try{
            String query = "SELECT * FROM public.list WHERE id_user="+id_user+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(!rs.isBeforeFirst())
                return false;
        }catch(SQLException e){
            //System.out.println("Error in exists "+e);
        }
        return true;
    }

    /**
     * This function adds or removes a list from the database based on the given choice.
     * @param choice choice
     * @param object object
     */
    @Override
    public void request(int choice, Object object) {
        int id_user = (int) object;
        switch(choice){
            case 1:
                add(id_user);
                break;
            case 2:
                remove(id_user);
                break;
        }
    }
}
