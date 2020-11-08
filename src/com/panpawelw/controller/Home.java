package com.panpawelw.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.panpawelw.DAO.LastSolutionDAO;
import com.panpawelw.DAO.RealLastSolutionDAO;
import com.panpawelw.misc.DbUtils;
import com.panpawelw.model.LastSolution;

@WebServlet(value = "/")
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LastSolutionDAO lastSolutionDAO;

    public Home() {
        super();
    }

    public void init() throws ServletException {
        if(lastSolutionDAO == null) lastSolutionDAO = new RealLastSolutionDAO(DbUtils.initDB());
    }

    public void setLastSolutionDAO(LastSolutionDAO lastSolutionDAO) {
        this.lastSolutionDAO = lastSolutionDAO;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contextParam = request.getServletContext().getInitParameter("number-solutions");
        long recentSolutions = 5;
        if (contextParam != null & !Objects.equals(contextParam, "")) {
            try {
                recentSolutions = Long.parseLong(contextParam);
            } catch (NumberFormatException n) {
                System.out.println("Parameter must be an integer value, using default" +
                        " value of 5 instead!");
            }
        }
        List<LastSolution> lastSolutions = lastSolutionDAO.loadMostRecentSolutions(recentSolutions);
        request.setAttribute("lastsolutions", lastSolutions);
        getServletContext().getRequestDispatcher("/jsp/index.jsp").forward(request, response);
    }
}