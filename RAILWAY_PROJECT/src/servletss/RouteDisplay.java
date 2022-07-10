package servletss;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import railwayFrequentFunctions.DatabaseConnection;

/**
 * Servlet implementation class RouteDisplay
 */
@WebServlet("/RouteDisplay")
public class RouteDisplay extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RouteDisplay() {
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
		response.setContentType("application/json");
		PrintWriter out=response.getWriter();
		Connection con;
		Date d;
		Calendar calendar = Calendar.getInstance();
		JSONArray data=new JSONArray();
		int trainno=Integer.parseInt(request.getParameter("trainNo").trim());
		int trainreturn=Integer.parseInt(request.getParameter("returnRoute"));
		String selectedDate=request.getParameter("selectedDate");
		try {
	con = (new DatabaseConnection()).dbConnect();
	String routedetails = "SELECT (SELECT station_name FROM station_master sr where "
			+ "sr.station_code = rm.station_code) AS station_name,distance_from_prev from route_master rm where "
			+ "rm.station_code IN (SELECT station_code FROM route_master WHERE route_id=(SELECT route_id from "
			+ "train_master where train_no=? AND train_return=?) AND counter >= (SELECT counter from route_master "
			+ "where route_id=(SELECT route_id from train_master where train_no=? AND train_return=?) and "
			+ "station_code=(SELECT source_station_code FROM `train_master` WHERE train_no=? AND train_return=?)) "
			+ "AND counter <= (SELECT counter from route_master where route_id=(SELECT route_id from train_master "
			+ "where train_no=? AND train_return=?) and station_code=(SELECT dest_station_code FROM train_master "
			+ "WHERE train_no=? AND train_return=?)) ORDER BY route_id,counter) AND route_id = (SELECT route_id "
			+ "from train_master where train_no=? AND train_return=?)";
	String traindetails="SELECT train_speed,train_time FROM train_normal_schedule"
			+ " WHERE train_no=? AND train_return=?"; 
	Double speed=0.0,distance=0.0;
	int timeinsecs=0;
	String timestamp="";
	String stationName="";
		PreparedStatement ps=con.prepareStatement(traindetails);
		ps.setInt(1, trainno);
		ps.setInt(2, trainreturn);
		ResultSet rs=ps.executeQuery();
		if(rs.next()){
			speed=rs.getDouble("train_speed");
			timestamp=rs.getString("train_time");
		}
		data.add("Station_name (Distance) [Expected Reaching Time]<br>With avg Train Speed = "+speed+"km/hr");
		timestamp=selectedDate+" "+timestamp;
		
		ps=con.prepareStatement(routedetails);
		ps.setInt(1, trainno);
		ps.setInt(2, trainreturn);
		ps.setInt(3, trainno);
		ps.setInt(4, trainreturn);
		ps.setInt(5, trainno);
		ps.setInt(6, trainreturn);
		ps.setInt(7, trainno);
		ps.setInt(8, trainreturn);
		ps.setInt(9, trainno);
		ps.setInt(10, trainreturn);
		ps.setInt(11, trainno);
		ps.setInt(12, trainreturn);
		rs=ps.executeQuery();
		int loopCounter=0;
		while(rs.next()){
			stationName=rs.getString("station_name");
			if(loopCounter==0){
			distance=0.0;
			}
			else{
			distance=rs.getDouble("distance_from_prev");
			}
			timeinsecs=(int)(Math.round((distance/speed)*3600));
			d=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
			calendar.setTime(d);
			calendar.add(Calendar.SECOND, timeinsecs);
			timestamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
			data.add(stationName+" ("+distance+" km) ["+calendar.getTime()+"]");
			loopCounter++;
		}
		con.close();
		out.print(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
