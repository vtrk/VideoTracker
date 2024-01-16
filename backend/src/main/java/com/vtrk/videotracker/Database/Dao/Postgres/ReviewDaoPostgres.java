package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ReviewDao;
import com.vtrk.videotracker.Database.Model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDaoPostgres implements ReviewDao {
    Connection connection = null;

    public ReviewDaoPostgres(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Review findById(int id) {
        Review review = new Review(id, 0,"", 0,"");
        try {
            /*String query = "SELECT * FROM review WHERE id = ?;";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, id);*/
            String query = "SELECT * FROM review WHERE id = "+id+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                review.setVote(rs.getInt("vote"));
                review.setUserComment(rs.getString("user_comment"));
                review.setIdUser(rs.getInt("id_user"));
                review.setIdContent(rs.getString("id_content"));
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return review;
    }

    /*
        This method finds the reviews of a content by its id. It returns a list of reviews.
        If there are no reviews, it returns an empty list.
    */

    @Override
    public List<Review> findByIdContent(String id_content) {
        List<Review> reviews = new ArrayList<Review>();
        try {
            String query = "SELECT * FROM review WHERE id_content = '"+id_content+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                int id = rs.getInt("id");
                int vote = rs.getInt("vote");
                String user_comment = rs.getString("user_comment");
                int id_user = rs.getInt("id_user");
                reviews.add(new Review(id, vote, user_comment, id_user, id_content));
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return reviews;
    }

    @Override
    public void add(Review review) {
        try{
            String query = "INSERT INTO public.review (id, vote, user_comment, id_user, id_content) VALUES(nextval('review_id_seq'::regclass), "+review.getVote()+", '"+review.getUserComment()+"', "+review.getIdUser()+", '"+review.getIdContent()+"');";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in add "+e);
        }
    }

    @Override
    public void update(Review review) {
        try{
            String query = "UPDATE public.review SET vote="+review.getVote()+", user_comment='"+review.getUserComment()+"', id_user="+review.getIdUser()+", id_content='"+review.getIdContent()+"' WHERE id= "+review.getId()+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in update "+e);
        }
    }

    @Override
    public void remove(Review review) {
        try{
            String query = "DELETE FROM public.review WHERE id= "+review.getId()+" ;";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in remove "+e);
        }
    }
}
