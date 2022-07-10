package servletss;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import railwayFrequentFunctions.DatabaseConnection;

/**
 * Servlet implementation class CheckOTP
 */
@WebServlet("/CheckOTP")
public class CheckOTP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckOTP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String email=request.getParameter("emailid");
		String otpByUser=request.getParameter("otp");
		try {
		 	DatabaseConnection db=new DatabaseConnection();
			
			Connection con = db.dbConnect();
			String checkExists="select otp from otp_transactional where emailid=?";
			PreparedStatement ps=con.prepareStatement(checkExists);
			ps.setString(1, email);
			ResultSet rs=ps.executeQuery();
			String otp="";
			if(rs.next())
			{
			otp=rs.getString(1);
			if(otp.equals(otpByUser))
			{
				out.print("true");
			}else{
				out.print("false");
			}
			}else{
				out.print("Time Out! Request OTP Again");
			}
			
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
