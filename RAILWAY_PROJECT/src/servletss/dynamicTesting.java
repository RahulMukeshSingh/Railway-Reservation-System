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

import railwayFrequentFunctions.DatabasePrice;
import railwayFrequentFunctions.DateTimeofStation;
import railwayFrequentFunctions.SeatAllocation;

/**
 * Servlet implementation class dynamicTesting
 */
@WebServlet("/dynamicTesting")
public class dynamicTesting extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public dynamicTesting() {
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
		PrintWriter out = response.getWriter();
		out.print("<center><h2 style='color:#00788b'>20%</h2></center>");
		HttpSession session = request.getSession();
		
		response.setContentType("application/json");
		out.print("Hello");
		String tno = (String) session.getAttribute("trainno");
		String treturn = (String) session.getAttribute("trainreturn");
		String sdate = (String) session.getAttribute("date");
		String routeid = (String) session.getAttribute("routeId");
		String src = (String) session.getAttribute("source");
		String dest = (String) session.getAttribute("destination");
		String classid = (String) session.getAttribute("classid");
		session.setAttribute("dateTimeInJSON", (new DateTimeofStation())
				.getDateAndTimeOfStation(src, dest, tno, treturn, sdate,
						routeid));
		DatabasePrice dp = new DatabasePrice();
		SeatAllocation sa = new SeatAllocation(tno, treturn, classid, routeid,
				src, dest,sdate);
		String quotaid = "";
		double totalPriceWithoutTax = 0.0;
		//out.print(request.getParameter("ticketData"));
		String[] datas = new String[6];
		for (int i = 0; i < datas.length; i++) {
			
			datas[i]="LMN"+i+";145;Female;GN;LB";
		}
		JSONArray jarr = new JSONArray();
		
		int count=0;
		for (String data : datas) {
			JSONObject jobj = new JSONObject();
			String[] d =null;
			try{
			d = data.split(";");
			out.print(data);
			}catch(Exception e){
				out.print("error");	
			}
			quotaid=d[3];
			int cnfGn=0, rac=0, wl=0, tqwl=0, cnfQuot=0;
			for (int i = (count-1); i >=0; i--) {
				String quotaColumnValue=((JSONObject)jarr.get(i)).get("quota").toString();
				String statusColumnValue=((JSONObject)jarr.get(i)).get("status").toString();
				if(statusColumnValue.contains("(CNF") && quotaColumnValue.equalsIgnoreCase(quotaid)){
					cnfQuot=cnfQuot+1;
				}
				if(statusColumnValue.contains("(CNF") && statusColumnValue.contains("GN")){
					
					cnfGn=cnfGn+1;
				}
				if(statusColumnValue.contains("(RAC")){
					rac=rac+1;
				}
				if(statusColumnValue.contains("(WL")){
					wl=wl+1;
				}
				if(statusColumnValue.contains("(TQWL")){
					tqwl=tqwl+1;
				}
			}
			//System.out.println(quotaid+"--> "+cnfGn+" "+ rac+" "+ wl+" "+ tqwl+" "+ cnfQuot);
			String status = sa.getAvailableSeats(quotaid,cnfGn, rac, wl, tqwl, cnfQuot);
			//out.print("status : "+status+" "+count+"\n");
			
			if (!status.equalsIgnoreCase("No Ticket Available")) {
				double priceWithoutTax = dp.getPriceWithoutTax(src, dest,
						routeid, tno, treturn, classid, quotaid);
				totalPriceWithoutTax += priceWithoutTax;
				jobj.put("name", d[0]);
				jobj.put("age", d[1]);
				jobj.put("gender", d[2]);
				jobj.put("quota", quotaid);
				jobj.put("pref", d[4]);
				jobj.put("status", status);
				jobj.put("priceWithoutTax", priceWithoutTax);
				jarr.add(jobj);
				count++;
			}
			
		}
				
		session.setAttribute("dataInJSON", jarr);
		JSONArray taxNamePlusPercentPlusTaxedPrice = dp
				.getPriceWithTax(totalPriceWithoutTax);
		session.setAttribute("taxNamePlusPercentPlusTaxedPriceInJSON",
				taxNamePlusPercentPlusTaxedPrice);
		JSONObject totalPriceWithTax = (JSONObject) taxNamePlusPercentPlusTaxedPrice
				.get(taxNamePlusPercentPlusTaxedPrice.size() - 1);
		double totalTaxedPrice = (double) totalPriceWithTax.get("pricewithtax");
		//out.print(jarr);
		//out.print(taxNamePlusPercentPlusTaxedPrice);
		//out.print((JSONArray) session.getAttribute("dateTimeInJSON"));
		RequestDispatcher rd=request.getRequestDispatcher("../railway/dynamicinsert");
		rd.forward(request, response);

	}

}
