package railwayFrequentFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DatabasePrice {
	public double getTrainRoutePrice(String fromId, String toId,
			String routeId, String trainNo, String trainReturn, String classId) {
		Double trainCostWithoutQuota = 0.0;
		try {
			Connection con = (new DatabaseConnection()).dbConnect();
			String trainCostQuery = "SELECT cost_per_km FROM train_master WHERE train_no=? and train_return=?";
			PreparedStatement ps = con.prepareStatement(trainCostQuery);
			ps.setInt(1, Integer.parseInt(trainNo));
			ps.setInt(2, Integer.parseInt(trainReturn));
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double traincost = rs.getDouble(1);

			String totalFromToRouteDistanceQuery = "select sum(distance_from_prev) from route_master where route_id=? and "
					+ "counter>(select counter from route_master where route_id=? and station_code=?) and "
					+ "counter<=(select counter from route_master where route_id=? and station_code=?)";
			ps = con.prepareStatement(totalFromToRouteDistanceQuery);
			ps.setInt(1, Integer.parseInt(routeId));
			ps.setInt(2, Integer.parseInt(routeId));
			ps.setInt(4, Integer.parseInt(routeId));
			ps.setString(3, fromId);
			ps.setString(5, toId);
			rs = ps.executeQuery();
			rs.next();
			Double totalfromtoroutedistance = rs.getDouble(1);
			Double trainCostWithoutQuotaAndClass = traincost
					* totalfromtoroutedistance;

			String classCostQuery = "SELECT additional_cost FROM train_class_cost WHERE train_no=? and "
					+ "train_return=? and class_id=?";
			ps = con.prepareStatement(classCostQuery);
			ps.setInt(1, Integer.parseInt(trainNo));
			ps.setInt(2, Integer.parseInt(trainReturn));
			ps.setString(3, classId);
			rs = ps.executeQuery();
			rs.next();
			Double classCostPercentage = rs.getDouble(1);

			trainCostWithoutQuota = trainCostWithoutQuotaAndClass
					+ (trainCostWithoutQuotaAndClass * (classCostPercentage / 100));

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return trainCostWithoutQuota;
	}

	public String formatCurrency(double price) {
		String priceWithDecimalString = String.valueOf(price);
		String[] priceArray = priceWithDecimalString.split("\\.");
		String priceString = priceArray[0];
		String formatPrice = "";
		int counter = 0;
		for (int i = priceString.length() - 1; i >= 0; i--) {
			if ((counter - 3) >= 0 && (counter - 3) % 2 == 0) {
				formatPrice = "," + formatPrice;
			}
			char priceAtLocation = priceString.charAt(i);
			formatPrice = priceAtLocation + formatPrice;
			counter++;
		}
		String decimall = "";
		if (priceArray.length > 1) {
			decimall = "." + priceArray[1];
		}
		return "\u20B9" + formatPrice + decimall;
	}

	public double getPriceWithoutTax(String fromid, String toid,
			String routeid, String trainno, String trainreturn, String classid,
			String quotaid)  {
		double price=0.0;
		try {
			double priceWithoutQuota = getTrainRoutePrice(fromid, toid, routeid,
					trainno, trainreturn, classid);
			Connection con = (new DatabaseConnection()).dbConnect();
			String query = "SELECT price_discount FROM quota_details where quota_id=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, quotaid);
			ResultSet rs = ps.executeQuery();
			rs.next();
			double quotaDiscount = rs.getDouble(1);
			price = priceWithoutQuota
					- (priceWithoutQuota * quotaDiscount / 100);
			price = Math.round(price * 10) / 10D;
			con.close();
	
		} catch (Exception e) {
			// TODO: handle exception
		} 
				return price;
	}

	public JSONArray getPriceWithTax(double priceWithoutTax) {
		JSONArray jarr = new JSONArray();
		try {
			Connection con = (new DatabaseConnection()).dbConnect();
			String taxQuery = "SELECT tax_id,tax_price from tax;";
			PreparedStatement ps = con.prepareStatement(taxQuery);
			ResultSet rs = ps.executeQuery();
			double sumTax = 0.0;

			JSONObject jobj = new JSONObject();
			while (rs.next()) {
				jobj = new JSONObject();
				jobj.put("taxid", rs.getString(1));
				jobj.put("taxpercent", rs.getDouble(2));
				jarr.add(jobj);
				sumTax+=rs.getDouble(2);
			}
			double priceWithTax = priceWithoutTax
					+ (priceWithoutTax * sumTax / 100);
			jobj = new JSONObject();
			jobj.put("pricewithtax", Math.round(priceWithTax * 100D) / 100D);
			jarr.add(jobj);
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return jarr;
	}
}
