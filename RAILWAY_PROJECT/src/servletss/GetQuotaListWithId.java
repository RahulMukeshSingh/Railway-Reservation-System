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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import railwayFrequentFunctions.DatabaseConnection;
import railwayFrequentFunctions.DateFunctions;

/**
 * Servlet implementation class GetQuotaListWithId
 */
@WebServlet("/GetQuotaListWithId")
public class GetQuotaListWithId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetQuotaListWithId() {
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
		Connection con;
		response.setContentType("application/json");
		PrintWriter out=response.getWriter();
		try {
			HttpSession session = request.getSession();
			String sdate = (String) session.getAttribute("date");

			con=(new DatabaseConnection()).dbConnect();
			String query="select quota_id,quota_name from quota_details order by seats_quota desc";
			PreparedStatement ps=con.prepareStatement(query);
			ResultSet rs=ps.executeQuery();
			JSONArray jarr=new JSONArray();
			DateFunctions df=new DateFunctions();
			boolean tatkalAvailable=df.isTatkalAvailable(sdate);
			while(rs.next()){
				if(!tatkalAvailable && rs.getString(1).equalsIgnoreCase("TQ")){
					continue;
				}
				JSONObject jobj=new JSONObject();
				jobj.put("id",rs.getString(1));
				jobj.put("name",rs.getString(2));
				jarr.add(jobj);
				
			}
			con.close();
			out.print(jarr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
	
	}
}
