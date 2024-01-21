package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.ContainsDao;
import com.vtrk.videotracker.Database.Dao.Subject;
import com.vtrk.videotracker.Database.Model.Contains;
import com.vtrk.videotracker.utils.Properties;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContainsDaoPostgres implements ContainsDao, Subject {

    Connection connection = null;
    public ContainsDaoPostgres(Connection connection){ this.connection = connection; }

    /**
     * This function finds all contents in a list and returns a list of contents.
     * @param id_list id_list
     * @return content list
     */
    @Override
    public List<Contains> findContentInList(int id_list) {
        List<Contains> contents = new ArrayList<Contains>();
        try {
            String query = "SELECT * FROM contains WHERE id_list = '"+id_list+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                Contains content = new Contains(id_list, DBManager.getInstance().getContentDao().findById(rs.getString("id_content")),rs.getString("state"));
                contents.add(content);
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll "+e);
        }
        return contents;
    }

    /**
     * This function counts all contents in a list by state and returns the number of contents.
     * @param id_list id_list
     * @param state state
     * @return counted
     */
    @Override
    public String countByState(int id_list, String state) {
        String counted = "";
        String apiUsed = "";
        if(Properties.getInstance().getProperty("API").equals("Kitsu")){
            apiUsed = "AND id_content like '%_anime'";
        }else{
            apiUsed = "AND (id_content like '%_tv' or id_content like '%_movie')";
        }
        try{
            String query = "SELECT COUNT(*) as count FROM contains WHERE id_list = "+id_list+" AND state = '"+state+"' "+apiUsed+" ;";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                counted = rs.getString("count");
            }
        }catch(SQLException e){
            System.out.println("Error in countByState "+e);
        }
        return counted;
    }

    /**
     * This function adds a content to a list.
     * @param id_list id_list
     * @param id_content id_content
     * @param state state
     */
    @Override
    public void add(int id_list, String id_content, String state) {
        try{
            String query = "INSERT INTO public.contains (id_list, id_content, state) VALUES("+id_list+", '"+id_content+"', '"+state+"');";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in add "+e);
        }
    }

    /**
     * This function updates the state of a content in a list.
     * @param id_list id_list
     * @param id_content id_content
     * @param state state
     */
    @Override
    public void update(int id_list, String id_content, String state) {
        try{
            String query = "UPDATE public.contains SET state='"+state+"' WHERE id_list = "+id_list+" AND id_content = '"+id_content+"' ;";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in update "+e);
        }
    }

    /**
     * This function removes content from a list.
     * @param id_list id_list
     * @param id_content id_content
     */
    @Override
    public void remove(int id_list, String id_content) {
        try{
            String query = "DELETE FROM public.contains WHERE id_list = "+id_list+" AND id_content='"+id_content+"';";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in remove "+e);
        }
    }

    /**
     * This function removes all contents from a list.
     * @param id_list id_list
     */
    @Override
    public void removeWholeList(int id_list) {
        try{
            String query = "DELETE FROM public.contains WHERE id_list = "+id_list+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in removeWholeList "+e);
        }
    }

    /**
     * This function checks if a content is in a list.
     * @param id_list id_list
     * @param id_content id_content
     * @return true if exists, false otherwise
     */
    @Override
    public boolean exists(int id_list, String id_content) {
        try {
            String query = "SELECT * FROM contains WHERE id_list = "+id_list+" AND id_content like '"+id_content+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(!rs.isBeforeFirst())
                return false;
        } catch (SQLException e) {
            System.out.println("Error in findAll "+ e);
            return false;
        }
        return true;
    }

    /**
     * Request action to be performed
     * @param choice choice
     * @param object object
     */
    @Override
    public void request(int choice, Object object) {
        if(choice == 4){
            removeWholeList((int) object);
        }else{
            Contains content = (Contains) object;
            switch(choice){
                case 1:
                    add(content.getId_list(), content.getContent().getId(), content.getState());
                    break;
                case 2:
                    update(content.getId_list(), content.getContent().getId(), content.getState());
                    break;
                case 3:
                    remove(content.getId_list(), content.getContent().getId());
                    break;
                default:
                    break;
            }
        }

    }
}
