package servletss;

import java.io.IOException;
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
 * Servlet implementation class TrainSelectionToClassSelection
 */
@WebServlet("/TrainSelectionToClassSelection")
public class TrainSelectionToClassSelection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrainSelectionToClassSelection() {
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
		String date=request.getParameter("selectedDatee");
		String srcStation=request.getParameter("selectedSrcStation1");
		String destStation=request.getParameter("selectedDestStation1");
		String trainNo=request.getParameter("selectedTrainno");
		String trainReturn=request.getParameter("selectedTrainreturn");
		session.setAttribute("date",date);
		session.setAttribute("trainno",trainNo);
		session.setAttribute("trainreturn",trainReturn);
		try{
			Connection con=(new DatabaseConnection()).dbConnect();
			String routeidquery="SELECT route_id FROM train_master WHERE train_no=? and train_return=?";
			PreparedStatement ps=con.prepareStatement(routeidquery);
			ps.setInt(1,Integer.parseInt(trainNo));
			ps.setInt(2,Integer.parseInt(trainReturn));
			ResultSet rs=ps.executeQuery();
			rs.next();
			String routeId=""+rs.getInt(1);
			
			String fromIdQuery="SELECT station_code FROM station_master WHERE station_name=?";
			ps=con.prepareStatement(fromIdQuery);
			ps.setString(1, srcStation);
			rs=ps.executeQuery();
			rs.next();
			String fromId=rs.getString(1);

			String toIdQuery="SELECT station_code FROM station_master WHERE station_name=?";
			ps=con.prepareStatement(toIdQuery);
			ps.setString(1, destStation);
			rs=ps.executeQuery();
			rs.next();
			String toId=rs.getString(1);
			
			session.setAttribute("routeId",routeId);
			session.setAttribute("source",fromId);
			session.setAttribute("destination",toId);
			
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		response.sendRedirect("../Phase2/classselection.jsp");
	}

}
