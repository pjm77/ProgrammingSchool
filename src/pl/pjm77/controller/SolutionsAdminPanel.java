package pl.pjm77.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.pjm77.DAO.RealSolutionDAO;
import pl.pjm77.model.Solution;

@WebServlet("/solutionsadminpanel")
public class SolutionsAdminPanel extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SolutionsAdminPanel() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Solution[] solutionsList = new RealSolutionDAO().loadAllSolutions();
        request.setAttribute("solutionslist", solutionsList);
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
        }
        getServletContext().getRequestDispatcher("/jsp/solutionsadminview.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}