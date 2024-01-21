package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ContentDao;
import com.vtrk.videotracker.Database.Dao.Subject;
import com.vtrk.videotracker.Database.Model.Contains;
import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentDaoPostgres implements ContentDao, Subject {

    Connection connection = null;

    public ContentDaoPostgres(Connection connection) {this.connection = connection;}

    /**
     * Find all contents
     * @param id id
     * @return content
     */
    @Override
    public Content findById(String id) {
        Content content = new Content(id, "",0,0,"");
        try {
            String query = "SELECT * FROM content WHERE id = '"+id+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                content.setDuration(rs.getInt("duration"));
                content.setLink(rs.getString("link"));
                content.setTitle(rs.getString("title"));
                content.setN_episode(rs.getInt("n_episode"));
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll "+e);
        }
        return content;
    }

    /**
     * Add content
     * @param content
     */
    @Override
    public void add(Content content) {
        try{
            String query = "INSERT INTO public.content (id, title, duration, n_episode, link) VALUES('"+content.getId()+"', '"+content.getTitle()+"', "+content.getDuration()+", "+content.getN_episode()+", '"+content.getLink()+"');";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in add "+e);
        }

    }

    /**
     * Update content
     * @param content
     */
    @Override
    public void update(Content content) {
        try{
            String query = "UPDATE public.content SET title='"+content.getTitle()+"', duration= "+content.getDuration()+", n_episode= "+content.getN_episode()+" , link='"+content.getLink()+"' WHERE id='"+content.getId()+"';";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in update "+e);
        }
    }

    /**
     * Remove content
     * @param content content
     */
    @Override
    public void remove(Content content) {
        try{
            String query = "DELETE FROM public.content WHERE id = '"+content.getId()+"';";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in remove "+e);
        }
    }

    /**
     * Check if content exists
     * @param id
     * @return true if exists, false otherwise
     */
    @Override
    public boolean exists(String id) {
        try {
            String query = "SELECT * FROM content WHERE id = '"+id+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(!rs.isBeforeFirst())
                return false;
        } catch (SQLException e) {
            System.out.println("Error in findAll "+e);
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
        Content content = (Content) object;
        switch(choice){
            case 1:
                add(content);
                break;
            case 2:
                update(content);
                break;
            case 3:
                remove(content);
                break;
            default:
                break;
        }
    }
}
