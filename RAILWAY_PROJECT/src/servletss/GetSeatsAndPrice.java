package servletss;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import railwayFrequentFunctions.DatabasePrice;
import railwayFrequentFunctions.SeatAllocation;

/**
 * Servlet implementation class GetSeatsAndPrice
 */
@WebServlet("/GetSeatsAndPrice")
public class GetSeatsAndPrice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSeatsAndPrice() {
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
			HttpSession session = request.getSession();
			String trainno = (String) session.getAttribute("trainno");
			String trainreturn = (String) session.getAttribute("trainreturn");
			String classid = (String) session.getAttribute("classid");
			String routeid = (String) session.getAttribute("routeId");
			String fromid = (String) session.getAttribute("source");
			String toid = (String) session.getAttribute("destination");
			String quotaid = request.getParameter("quotaid");
			String sdate = (String) session.getAttribute("date");
			int cnfGn = Integer.parseInt(request.getParameter("cnfGn"));
			int rac = Integer.parseInt(request.getParameter("rac"));
			int wl = Integer.parseInt(request.getParameter("wl"));
			int tqwl = Integer.parseInt(request.getParameter("tqwl"));
			int cnfQuot = Integer.parseInt(request.getParameter("cnfQuot"));
			
			
			SeatAllocation sa = new SeatAllocation(trainno, trainreturn,
					classid, routeid, fromid, toid,sdate);
			
			
			DatabasePrice dp=new DatabasePrice();
			double price=dp.getPriceWithoutTax(fromid, toid, routeid, trainno, trainreturn, classid, quotaid);
			
			NumberFormat formatt = NumberFormat.getInstance();
			String priceFormatted = formatt.format(price);
			
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject jobj=new JSONObject();
			jobj.put("seatavailable", (sa.getAvailableSeats(quotaid,cnfGn,rac,wl,tqwl,cnfQuot).split("/////"))[0]);
			jobj.put("price", priceFormatted);
			
			out.print(jobj);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
