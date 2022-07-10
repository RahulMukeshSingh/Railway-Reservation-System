package servletss;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import railwayFrequentFunctions.DatabaseConnection;

/**
 * Servlet implementation class ValidationCheck
 */
@WebServlet("/ValidationCheck")
public class ValidationCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ValidationCheck() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try {
			if (request.getParameterNames().nextElement().toString()
					.equals("userid")) {
				String value = request.getParameter("userid").trim();
			 	DatabaseConnection db=new DatabaseConnection();
				
				con = db.dbConnect();
				Statement stmt = con.createStatement();
				String query = "select count(*) from user_personal where userid='" + value+"'";
				ResultSet rs = stmt.executeQuery(query);
				rs.next(); 
				int rowno=rs.getInt(1);
				if(rowno==0)
				{
				out.print("true");	
				}else{
				out.print("false");	
				}
				con.close();
			}
		} catch (Exception e) {

		}
	}

}
