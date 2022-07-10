package servletss;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import railwayFrequentFunctions.DatabaseConnection;
import railwayFrequentFunctions.SecretKey;

/**
 * Servlet implementation class LoginAuthentication
 */
@WebServlet("/LoginAuthentication")
public class LoginAuthentication extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginAuthentication() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String userid=request.getParameter("userid");
		String password=request.getParameter("password");
		try {
		 	DatabaseConnection db=new DatabaseConnection();
			
			Connection con = db.dbConnect();
			String getFname="select full_name from user_details where userid=?";
			PreparedStatement ps=con.prepareStatement(getFname);
			ps.setString(1, userid);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
			String selectQuery="select AES_DECRYPT(password,?) as password,role from user_personal where userid=?";
			String fname=rs.getString(1);
			String key=(new SecretKey()).getSecretKey(userid, fname.length());
			ps=con.prepareStatement(selectQuery);
			ps.setString(1, key);
			ps.setString(2, userid);
			rs=ps.executeQuery();
			rs.next();
			String pass="";
			
			pass=rs.getString(1);
			String role=rs.getString(2);
			
			if(pass.equals(password) && role.equals("customer"))
			{
				HttpSession session=request.getSession();
				session.setAttribute("userid", userid);
				session.setAttribute("fname", fname);
				session.setAttribute("role", role);
				response.sendRedirect("../Phase2/viewdetails.jsp");
				
			}else{
				out.println("<script src='../js/jquery.js'></script>");
				out.println("<script src='../js/HtmlandCommon.js'></script>");
				out.println("<script>$(document).ready(function(){alertBox('Wrong Password!');});</script>");
				RequestDispatcher rd=request.getRequestDispatcher("../Phase1/index.jsp");
				rd.include(request, response);
				}
			}else{
				out.println("<script src='../js/jquery.js'></script>");
				out.println("<script src='../js/HtmlandCommon.js'></script>");
				out.println("<script>$(document).ready(function(){alertBox('Wrong Username!');});</script>");
				RequestDispatcher rd=request.getRequestDispatcher("../Phase1/index.jsp");
				rd.include(request, response);
			}
			
			con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
