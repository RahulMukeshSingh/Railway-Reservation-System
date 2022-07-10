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
import javax.servlet.http.HttpSession;

import railwayFrequentFunctions.DatabaseConnection;
import railwayFrequentFunctions.emailattachmentrailway;

/**
 * Servlet implementation class SendPDFMail
 */
@WebServlet("/SendPDFMail")
public class SendPDFMail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendPDFMail() {
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
		HttpSession session=request.getSession();
		String userid = (String) session.getAttribute("userid");
		String to ="";
		String subject="AUTO GENERATED RAILWAY TICKET";
		String attachfilename="RAILWAY TICKET";
		boolean flag=false;
		try{
		Connection con = (new DatabaseConnection()).dbConnect();
		String emailQuery = "SELECT email FROM user_details WHERE userid=?";
		PreparedStatement ps = con.prepareStatement(emailQuery);
		ps.setString(1, userid);
		ResultSet rs = ps.executeQuery();
		rs.next();
		to = rs.getString(1);
		con.close();
		flag=(new emailattachmentrailway()).createattachandemail(to, subject, attachfilename);
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.print(String.valueOf(flag));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
