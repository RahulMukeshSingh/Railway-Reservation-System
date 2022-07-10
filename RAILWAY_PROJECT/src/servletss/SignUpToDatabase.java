package servletss;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import railwayFrequentFunctions.DatabaseConnection;
import railwayFrequentFunctions.SecretKey;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class SignUpToDatabase
 */
@WebServlet("/SignUpToDatabase")
public class SignUpToDatabase extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpToDatabase() {
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
		try {
		 	DatabaseConnection db=new DatabaseConnection();
			
			Connection con = db.dbConnect();
			String role="";
			String userid=request.getParameter("txt_userid");
			String pass=request.getParameter("txt_password");
			
			String ipaddress=request.getRemoteAddr();
			String fname=request.getParameter("txt_fname");
			String mobile=request.getParameter("mobile");
			String key=(new SecretKey()).getSecretKey(userid, fname.length());
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dobParse=formatter.parse(request.getParameter("DOB"));
			
			String address=request.getParameter("address");
			String email=request.getParameter("email");
			String district=request.getParameter("district");
			String state=request.getParameter("state");
			String gender=request.getParameter("gender");
			String marital=request.getParameter("marital");
			String insertUserTables="insert into user_personal values(?,AES_ENCRYPT(?,?),?,?)";
			
			PreparedStatement ps=con.prepareStatement(insertUserTables);
			ps.setString(1, userid);
			ps.setString(2, pass);
			ps.setString(3, key);
			ps.setString(4, ipaddress);
			if(request.getParameter("role")!=null)
			{
			role=request.getParameter("role");	
			ps.setString(5, role);
			}else{
			ps.setString(5, "customer");	
			}
			ps.executeUpdate();
			
			String insertDetailsTables="insert into user_details values(?,?,?,?,?,?,?,?,?,?)";
			ps=con.prepareStatement(insertDetailsTables);
			
			ps.setString(1, userid);
			ps.setString(2, fname);
			ps.setString(3, mobile);
			ps.setString(4, gender);
			ps.setString(5, marital);
			ps.setDate(6,new java.sql.Date(dobParse.getTime()) );
			ps.setString(7, state);
			ps.setString(8, district);
			ps.setString(9, address);
			ps.setString(10, email);
			
			ps.executeUpdate();
		
			con.close();
			RequestDispatcher rd=request.getRequestDispatcher("../Phase1/index.jsp?success=true");
			rd.forward(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
