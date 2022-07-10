package servletss;

import railwayFrequentFunctions.DatabaseConnection;
import railwayFrequentFunctions.mailnow;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class GetOTP
 */
@WebServlet("/GetOTP")
public class GetOTP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOTP() {
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
		String otpDigits="qw1ertyu72iopas0dfghj3klzxcv8bnm4QWERT96YUI5OPASDFGHJKLZXCVBNM";
		Random random=new Random();
		String otp="";
		for (int i = 0; i < 6; i++) {
			otp+=otpDigits.charAt(random.nextInt(200)%otpDigits.length());
		}
		try {
		 	DatabaseConnection db=new DatabaseConnection();
			
			Connection con = db.dbConnect();
			String checkExists="select count(*) from otp_transactional where emailid=?";
			PreparedStatement ps=con.prepareStatement(checkExists);
			ps.setString(1, email);
			ResultSet rs=ps.executeQuery();
			rs.next();
			int exists=rs.getInt(1);
			if(exists > 0)
			{
			String deleteQuery="delete from otp_transactional where emailid=?";	
			ps=con.prepareStatement(deleteQuery);
			ps.setString(1, email);
			ps.executeUpdate();
			}
			String insertQuery="insert into otp_transactional values(?,?,now())";
			ps=con.prepareStatement(insertQuery);
			ps.setString(1, email);
			ps.setString(2, otp);
			ps.executeUpdate();
			con.close();
			mailnow mn=new mailnow();
			String msg="Your OTP is "+otp;
			String sub="Railway Sign Up OTP";
			if(mn.MailNowMsg(email, msg, sub))
			{
				out.print("true");
			}else{
				out.print("false");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
