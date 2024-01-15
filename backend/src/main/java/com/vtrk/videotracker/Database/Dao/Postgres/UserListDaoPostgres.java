package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.UserListDao;
import com.vtrk.videotracker.Database.Model.UserList;

import java.sql.*;

public class UserListDaoPostgres implements UserListDao {
    Connection connection = null;

    public UserListDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    /*
        This function finds the list of the user by the id of the user.
        Check id to see if the list is found.
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
            System.out.println("Error in findAll"+e);
        }
        return user_list;
    }

    /*
        This function finds the list of the user by the id of the list.
        Check id_user to see if the list is found.
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
            System.out.println("Error in findAll"+e);
        }
        return user_list;
    }

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
}
