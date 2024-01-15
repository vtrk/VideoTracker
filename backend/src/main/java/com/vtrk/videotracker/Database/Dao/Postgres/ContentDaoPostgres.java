package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ContentDao;
import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentDaoPostgres implements ContentDao{

    Connection connection = null;

    public ContentDaoPostgres(Connection connection) {this.connection = connection;}

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
            System.out.println("Error in findAll"+e);
        }
        return content;
    }

    @Override
    public void add(Content content) {

    }

    @Override
    public void update(Content content) {

    }

    @Override
    public void remove(Content content) {

    }
}
