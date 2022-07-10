package servletss;

import java.io.FileInputStream;
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

/**
 * Servlet implementation class DownloadTicket
 */
@WebServlet("/DownloadTicket")
public class DownloadTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadTicket() {
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
		try{
		Connection con = (new DatabaseConnection()).dbConnect();
		String emailQuery = "SELECT email FROM user_details WHERE userid=?";
		PreparedStatement ps = con.prepareStatement(emailQuery);
		ps.setString(1, userid);
		ResultSet rs = ps.executeQuery();
		rs.next();
		to = rs.getString(1);
		con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		response.setContentType("application/pdf");  
		PrintWriter out = response.getWriter();  
		String filepath = "E:\\JSP\\RAILWAY_PROJECT\\WebContent\\WEB-INF\\GeneratedDocuments\\" + 
				to.replace(".", "")+ ".pdf";   
		response.setContentType("APPLICATION/OCTET-STREAM");   
		response.setHeader("Content-Disposition","inline; filename=\"RAILWAY TICKET.pdf\"");   
		  
		FileInputStream fileInputStream = new FileInputStream(filepath);  
		            
		int i;   
		while ((i=fileInputStream.read()) != -1) {  
		out.write(i);   
		}   
		fileInputStream.close();   
		out.close();   
	}

}
