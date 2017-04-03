package com.mikemolenda.recommender.controllers;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@WebServlet(name="RecommendController", urlPatterns={"/recommend"} )
public class RecommendController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private @Resource ( name = "jdbc/taste",
                        lookup = "jdbc/taste",
                        authenticationType = Resource.AuthenticationType.APPLICATION,
                        shareable = false) DataSource tasteDS;

    public RecommendController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;

        int numRec =
                (request.getParameter("numRec") == null ? 10 : Integer.parseInt(request.getParameter("numRec"));

        try {

            // Get user data
            long userId = Long.parseLong(request.getParameter("userId"));
            DataModel dataModel = new PostgreSQLJDBCDataModel(tasteDS, "PREFERENCES.TASTE_PREFERENCES", "USER_ID", "ITEM_ID", "PREFERENCE", "TIMESTAMP");
            int itemsRated = dataModel.getItemIDsFromUser(userId).size();

            // Run recommendation engine
            UserSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, dataModel);
            Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

            // List results
            List<RecommendedItem> list = recommender.recommend(userId, numRec);

            url = "/views/show-recommendations.jsp";
            request.setAttribute("userId", userId);
            request.setAttribute("numRated", itemsRated);
            request.setAttribute("results", list);

            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(url);
            dispatcher.forward(request, response);

        } catch (TasteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}
