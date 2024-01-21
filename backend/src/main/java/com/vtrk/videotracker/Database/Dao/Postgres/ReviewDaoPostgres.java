package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ReviewDao;
import com.vtrk.videotracker.Database.Dao.Subject;
import com.vtrk.videotracker.Database.Model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDaoPostgres implements ReviewDao, Subject {
    Connection connection = null;

    public ReviewDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    /**
     * Find a review by its id
     * @param id id of the review
     * @return review
     */
    @Override
    public Review findById(int id) {
        Review review = new Review(id, 0,"", 0,"");
        try {
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
            //System.out.println("Error in findAll "+e);
        }
        return review;
    }

    /**
     * Find id of a review by its id_user and id_content
     * @param id_user id of the user
     * @param id_content id of the content
     * @return id of the review
     */
    @Override
    public int findByIdUserAndContent(int id_user, String id_content) {
        int id = -1;
        try {
            String query = "SELECT * FROM review WHERE id_user = " + id_user + " AND id_content = '"+id_content+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            //System.out.println("Error in findAll "+e);
        }
        return id;
    }

    /**
        This method finds the reviews of a content by its id. It returns a list of reviews.
        If there are no reviews, it returns an empty list.
        @param id_content id of the content
        @return list of reviews
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
            //System.out.println("Error in findAll "+e);
        }
        return reviews;
    }

    /**
     * Add a review to the database
     * @param review review
     */
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

    /**
     * Update a review
     * @param review review
     */
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

    /**
     * Remove a review
     * @param id id of the review
     */
    @Override
    public void remove(int id) {
        try{
            String query = "DELETE FROM public.review WHERE id= "+id+" ;";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in remove "+e);
        }
    }

    /**
     * Remove all reviews of a user
     * @param id_user id of the user
     */
    public void removeAllReviewsOfAUser(int id_user) {
        try{
            String query = "DELETE FROM public.review WHERE id_user= "+id_user+" ;";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            //System.out.println("Error in remove "+e);
        }
    }

    /**
     * Check if a review exists
     * @param id_user id of the review
     * @return true if exists, false if not
     */
    @Override
    public boolean exists(int id_user, String id_content) {
        try {
            String query = "SELECT * FROM review WHERE id_user = " + id_user + " AND id_content = '"+id_content+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(!rs.isBeforeFirst())
                return false;
        } catch (SQLException e) {
            //System.out.println("Error in findAll "+e);
            return false;
        }
        return true;
    }

    /**
     * Request based on the given choice
     * @param choice choice
     * @param object object
     */
    @Override
    public void request(int choice, Object object) {
        switch(choice){
            case 1:
                Review review = (Review) object;
                add(review);
                break;
            case 2:
                Review reviewUp = (Review) object;
                update(reviewUp);
                break;
            case 3:
                remove((int)object);
                break;
            case 4:
                removeAllReviewsOfAUser((int) object);
                break;
            default:
                break;
        }
    }
}
