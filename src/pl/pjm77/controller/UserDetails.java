package pl.pjm77.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.pjm77.DAO.LastSolutionDAO;
import pl.pjm77.DAO.UserDAO;
import pl.pjm77.DAO.UserGroupDAO;
import pl.pjm77.model.LastSolution;
import pl.pjm77.model.User;
import pl.pjm77.model.UserGroup;

@WebServlet("/userdetails")
public class UserDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserDetails() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        int userId = 0;
        if(idParam!=null & !idParam.equals("")) {
            try {
                userId = Integer.parseInt(idParam);
            }catch(NumberFormatException e) {
                System.out.println("Incorrect user Id!");
                e.printStackTrace();
            }
        }
        User user = new UserDAO().loadUserById(userId);
        request.setAttribute("user", user);
        UserGroup userGroup = UserGroupDAO.loadUserGroupById(user.getGroup_id());
        request.setAttribute("groupname", userGroup.getName());
        LastSolution[] usersSolutions = LastSolutionDAO.loadAllSolutionsByUserId(userId);
        request.setAttribute("userssolutions", usersSolutions);
        getServletContext().getRequestDispatcher("/jsp/userdetailsview.jsp").forward(request, response);
        response.getWriter().append("" + userId).append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}