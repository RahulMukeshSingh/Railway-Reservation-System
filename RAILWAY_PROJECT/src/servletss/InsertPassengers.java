package servletss;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import railwayFrequentFunctions.DatabaseConnection;
import railwayFrequentFunctions.DatabasePrice;
import railwayFrequentFunctions.PNR;
import railwayFrequentFunctions.SeatAllocation;

/**
 * Servlet implementation class InsertPassengers
 */
@WebServlet("/InsertPassengers")
public class InsertPassengers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertPassengers() {
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
			PrintWriter out = response.getWriter();
			out.print("<center><h2 style='color:#00788b'>40%</h2></center>");
			DatabasePrice dp=new DatabasePrice();
			Connection con=(new DatabaseConnection()).dbConnect();
			HttpSession session = request.getSession();
			String tno = (String) session.getAttribute("trainno");
			String treturn = (String) session.getAttribute("trainreturn");
			String src = (String) session.getAttribute("source");
			String dest = (String) session.getAttribute("destination");
			String userid = (String) session.getAttribute("userid");
			String classid = (String) session.getAttribute("classid");
			String routeid = (String) session.getAttribute("routeId");
			String sdate = (String) session.getAttribute("date");
			String pnr = (new PNR()).getPNR();
			session.setAttribute("pnr", pnr);
			
			String insertBookedCustomerQuery = "INSERT INTO booked_customer_details VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps;
			ResultSet rs;
			JSONArray dataInJSON = (JSONArray) session.getAttribute("dataInJSON");
			JSONArray dateTimeInJSON = (JSONArray) session.getAttribute("dateTimeInJSON");
			JSONObject srcOfUser = (JSONObject) dateTimeInJSON.get(1);
			JSONObject destOfUser = (JSONObject) dateTimeInJSON.get(2);
			JSONObject srcOfTrain = (JSONObject) dateTimeInJSON.get(0);
			JSONObject destOfTrain = (JSONObject) dateTimeInJSON.get(3);

			Date srcTime=(Date)srcOfUser.get("stationtime");
			Date destTime=(Date)destOfUser.get("stationtime");
			Date srcTrainTime=(Date)srcOfTrain.get("stationtime");
			Date destTrainTime=(Date)destOfTrain.get("stationtime");
			java.sql.Timestamp srcSqlTime=new java.sql.Timestamp(srcTime.getTime());
			java.sql.Timestamp destSqlTime=new java.sql.Timestamp(destTime.getTime());
			java.sql.Timestamp srcTrainSqlTime=new java.sql.Timestamp(srcTrainTime.getTime());
			java.sql.Timestamp destTrainSqlTime=new java.sql.Timestamp(destTrainTime.getTime());
			JSONObject dataINJSONObject;
			int counter=0;
			JSONArray newDataInJSON=new JSONArray();
			for (Object dataInObject : dataInJSON) {
				SeatAllocation sa=new SeatAllocation(tno, treturn, classid, routeid, src, dest,sdate);
				dataINJSONObject=(JSONObject)dataInObject;
				String quotaId=dataINJSONObject.get("quota").toString();
				String totalStatus=dataINJSONObject.get("status").toString();
				String pref=dataINJSONObject.get("pref").toString();
				Double priceWithoutTax=(Double)dataINJSONObject.get("priceWithoutTax");
				JSONArray taxNamePlusPercentPlusTaxedPrice = dp.getPriceWithTax(priceWithoutTax);
				JSONObject priceWithTax = (JSONObject) taxNamePlusPercentPlusTaxedPrice
						.get(taxNamePlusPercentPlusTaxedPrice.size() - 1);
				double taxedPrice = (double) priceWithTax.get("pricewithtax");
				String[] formattedStatus=sa.getFormattedStatus(totalStatus);
				String statusId=formattedStatus[1];
				if(formattedStatus[1].equalsIgnoreCase("TQWL")){
					statusId="WL";
				}
				ps=con.prepareStatement(insertBookedCustomerQuery);
				ps.setString(1, userid);
				ps.setString(2, pnr);
				ps.setString(3, dataINJSONObject.get("name").toString());
				ps.setInt(4,Integer.parseInt(dataINJSONObject.get("age").toString()));
				ps.setString(5,quotaId);
				ps.setString(6,classid);
				ps.setInt(7, (counter+1));
				ps.setString(8,statusId);
				ps.setInt(9,Integer.parseInt(formattedStatus[2].trim()));
				ps.setString(10,statusId);
				ps.setInt(11,Integer.parseInt(formattedStatus[2].trim()));
				ps.setString(12, dataINJSONObject.get("gender").toString());
				ps.setTimestamp(13, srcSqlTime);
				ps.setTimestamp(14, destSqlTime);
				ps.setDouble(15, taxedPrice);
				ps.executeUpdate();
				JSONObject newDataINJSONObject=sa.insertIntoStatusTable(formattedStatus, pnr, quotaId, pref,
						(counter+1), con, ps,dataINJSONObject,srcTrainSqlTime,destTrainSqlTime);
				newDataInJSON.add(newDataINJSONObject);
				counter++;
			}
			con.close();
			session.setAttribute("dataInJSON",newDataInJSON);
			
			RequestDispatcher rd=request.getRequestDispatcher("../railway/ticketGeneration");
			rd.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
