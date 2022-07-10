package servletss;

import java.io.IOException;
import java.io.PrintWriter;

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
import railwayFrequentFunctions.emailattachmentrailway;
import railwayFrequentFunctions.pdfGeneration;
import railwayFrequentFunctions.properFormatForPDF;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import railwayFrequentFunctions.TableGenerator;

/**
 * Servlet implementation class TicketGeneration
 */
@WebServlet("/TicketGeneration")
public class TicketGeneration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TicketGeneration() {
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
		try {
			PrintWriter out = response.getWriter();
			out.print("<center><h2 style='color:#00788b'>80%</h2></center>");
			HttpSession session = request.getSession();
			String tno = (String) session.getAttribute("trainno");
			String treturn = (String) session.getAttribute("trainreturn");
			String src = (String) session.getAttribute("source");
			String dest = (String) session.getAttribute("destination");
			String userid = (String) session.getAttribute("userid");
			String fname = (String) session.getAttribute("fname");
			String pnr = (String) session.getAttribute("pnr");
			String classid = (String) session.getAttribute("classid");
			Connection con = (new DatabaseConnection()).dbConnect();
			String userDetailQuery = "SELECT email,address,mobile_no FROM user_details WHERE userid=?";
			PreparedStatement ps = con.prepareStatement(userDetailQuery);
			ps.setString(1, userid);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String to = rs.getString(1);
			String address = rs.getString(2);
			String mobile = rs.getString(3);

			
			
			String trainDetailQuery = "SELECT train_name FROM train_master WHERE train_no=? and train_return=?";
			ps = con.prepareStatement(trainDetailQuery);
			ps.setInt(1, Integer.parseInt(tno));
			ps.setInt(2, Integer.parseInt(treturn));
			rs = ps.executeQuery();
			rs.next();
			String tname = rs.getString(1);

			String filename = "E:\\JSP\\RAILWAY_PROJECT\\WebContent\\WEB-INF\\GeneratedDocuments\\"
					+ to.replace(".", "") + ".pdf";
			String qrLocation = "E:\\JSP\\RAILWAY_PROJECT\\WebContent\\WEB-INF\\GeneratedDocuments\\"
					+ to.replace(".", "") + ".png";
			String seatMapLocation = "E:\\JSP\\RAILWAY_PROJECT\\WebContent\\images\\"
					+ classid + ".gif";
			if (new File(filename).exists()) {
				new File(filename).delete();
			}

			String[] headers;
			String[][] data;
			String pdfData = "";
			TableGenerator tg = new TableGenerator();
			DatabasePrice dp = new DatabasePrice();
			String css = ".tabularDesign{border-collapse: collapse;width: 100%;font-size: 10px;font-family:Times New Roman;}"
					+ ".tabularDesign th,.tabularDesign td {text-align: left;padding: 8px;}"
					+ ".tabularDesign .even{background-color: #f2f2f2}"
					+ ".tabularDesign th{background-color: #00788b;color: white;}"
					+ ".tabularDesign tr{color:#404040;}";

			css += ".nonTabularDesign{border-collapse: collapse;width: 100%;font-size: 10px;float:right;}"
					+ ".nonTabularDesign td {text-align: left;padding: 8px;}"
					+ "table td p {text-align: right;color: #07788b;font-weight: bold;}";
			css += "p {color: #07788b;font-weight: bold;text-align: center;}";

			pdfData = "<p>Ticket and Login User Details : </p><br/>";
			headers = new String[] {};
			data = new String[][] { { "<p>PNR No : </p>", pnr },
					{ "<p>User Id : </p>", userid },
					{ "<p>Name : </p>", fname },
					{ "<p>Address : </p>", address },
					{ "<p>Mobile : </p>", mobile },
					{ "<p>Email Id : </p>", to } };
			pdfData += tg.MsgGeneratortable(headers, data, "nonTabularDesign",
					true);

			JSONArray dateTimeInJSON = (JSONArray) session
					.getAttribute("dateTimeInJSON");
			JSONObject srcOfTrain = (JSONObject) dateTimeInJSON.get(0);
			JSONObject srcOfUser = (JSONObject) dateTimeInJSON.get(1);
			JSONObject destOfUser = (JSONObject) dateTimeInJSON.get(2);
			JSONObject destOfTrain = (JSONObject) dateTimeInJSON.get(3);
			pdfData += "<p><br/>Train Details :</p><br/>";
			headers = new String[] {};
			data = new String[][] {
					{ "<p>Train --> </p>", "<p>No : </p>", tno,
							"<p>Name : </p>", tname, "", "" },
					{ "<p>Source --> </p>", "<p>Station Name : </p>",
							srcOfTrain.get("stationname").toString(),
							"<p>Departure Time : </p>",
							srcOfTrain.get("stationtime").toString(),
							"<p>Distance : </p>",
							srcOfTrain.get("distance").toString() },
					{ "<p>Destination --> </p>", "<p>Station Name : </p>",
							destOfTrain.get("stationname").toString(),
							"<p>Arrival Time : </p>",
							destOfTrain.get("stationtime").toString(),
							"<p>Distance : </p>",
							destOfTrain.get("distance").toString() } };
			pdfData += tg.MsgGeneratortable(headers, data, "nonTabularDesign",
					true);
			
			pdfData += "<p><br/>Passeneger Travel Details :</p><br/>";
			headers = new String[] {};
			data = new String[][] {
					{ "<p>Source --> </p>", "<p>Station Name : </p>",
						srcOfUser.get("stationname").toString(),
						"<p>Departure Time : </p>",
						srcOfUser.get("stationtime").toString(),
						"<p>Distance From <br/>"+srcOfTrain.get("stationname").toString()+" : </p>",
						srcOfUser.get("distance").toString() },
				{ "<p>Destination --> </p>", "<p>Station Name : </p>",
						destOfUser.get("stationname").toString(),
						"<p>Arrival Time : </p>",
						destOfUser.get("stationtime").toString(),
						"<p>Distance From <br/>"+srcOfTrain.get("stationname").toString()+" : </p>",
						destOfUser.get("distance").toString() } };

			pdfData += tg.MsgGeneratortable(headers, data, "nonTabularDesign",
					true);

			pdfData += "<br/><p>Passenger Details :</p><br/>";

			headers = new String[] { "Status", "Quota","Class", "Seat No", "Berth",
					"Coach", "Name", "Age", "Gender", "Ticket Cost" };
			JSONArray dataInJSON = (JSONArray) session
					.getAttribute("dataInJSON");
			JSONArray taxNamePlusPercentPlusTaxedPriceInJSON = (JSONArray) session
					.getAttribute("taxNamePlusPercentPlusTaxedPriceInJSON");
			data = new String[dataInJSON.size()
					+ taxNamePlusPercentPlusTaxedPriceInJSON.size()][];
			
			
			int count = 0;
			for (Object userData : dataInJSON) {
				JSONObject userDataObj = (JSONObject) userData;
				String[] formattedPDFData=(new properFormatForPDF())
						.getProperFormatForPDF(userDataObj.get("quota").toString().trim(), classid.trim(),
								userDataObj.get("berthId").toString().trim(),
								userDataObj.get("coachNo").toString().trim()); 
				data[count] = new String[] {
						(userDataObj.get("status").toString().split("/////"))[0],
						formattedPDFData[0],
						formattedPDFData[1],
						userDataObj.get("seatNo").toString(),
						formattedPDFData[2],
						formattedPDFData[3],
						userDataObj.get("name").toString(),
						userDataObj.get("age").toString(),
						userDataObj.get("gender").toString(),
						dp.formatCurrency(Double.parseDouble(userDataObj.get(
								"priceWithoutTax").toString())) };
				count++;
			}
			
			for (Object userPriceData : taxNamePlusPercentPlusTaxedPriceInJSON) {
				JSONObject userPriceDataObj = (JSONObject) userPriceData;
				if ((data.length - 1) == count) {
					data[count] = new String[] {
							"",
							"",
							"",
							"",
							"",
							"",
							"",
							"",
							"<p>Total Cost : </p>",
							dp.formatCurrency(Double
									.parseDouble(userPriceDataObj.get(
											"pricewithtax").toString())) };
				} else {
					data[count] = new String[] {
							"",
							"",
							"",
							"",
							"",
							"",
							"",
							"",
							"<p>" + userPriceDataObj.get("taxid").toString()
									+ " : </p>",
							userPriceDataObj.get("taxpercent").toString() + "%" };

				}
				count++;
			}
			pdfData += tg.MsgGeneratortable(headers, data, "tabularDesign",
					true);
			//out.print(pdfData);
			pdfGeneration pg = new pdfGeneration(filename, pdfData, seatMapLocation ,pnr,
					qrLocation, css,to.length());
			
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("../Phase5/booked.jsp");
		/*RequestDispatcher rd=request.getRequestDispatcher("../Phase5/booked.jsp");
		rd.forward(request, response);*/
	}

}
