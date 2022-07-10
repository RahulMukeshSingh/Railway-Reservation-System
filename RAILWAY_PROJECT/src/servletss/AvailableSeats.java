package servletss;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;

import railwayFrequentFunctions.SeatAllocation;

/**
 * Servlet implementation class AvailableSeats
 */
@WebServlet("/AvailableSeats")
public class AvailableSeats extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AvailableSeats() {
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
			HttpSession session = request.getSession();
			String trainno = (String) session.getAttribute("trainno");
			String trainreturn = (String) session.getAttribute("trainreturn");
			String classid = (String) session.getAttribute("classid");
			String routeid = (String) session.getAttribute("routeId");
			String fromid = (String) session.getAttribute("source");
			String toid = (String) session.getAttribute("destination");
			String sdate = (String) session.getAttribute("date");
			SeatAllocation sa = new SeatAllocation(trainno, trainreturn,
					classid, routeid, fromid, toid,sdate);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONArray seats = new JSONArray();
			seats = sa.getTotalNoOfSeats();
			out.print(seats);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
