package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ContainsDao;
import com.vtrk.videotracker.Database.Model.Content;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContainsDaoPostgres implements ContainsDao {

    Connection connection = null;
    public ContainsDaoPostgres(Connection connection){ this.connection = connection; }
    @Override
    public List<Content> findContentInList(int id_list) {
        List<Content> contents = new ArrayList<Content>();
        try {
            String query = "SELECT * FROM contains WHERE id_list = '"+id_list+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                Content content = new ContentDaoPostgres(connection).findById(rs.getString("id_content"));
                contents.add(content);
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return contents;
    }

    @Override
    public void add(int id_list, String id_content) {

    }

    @Override
    public void update(int id_list, String id_content, String state) {

    }

    @Override
    public void remove(int id_list, String id_content) {

    }
}
