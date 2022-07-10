package railwayFrequentFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DateTimeofStation {
	public JSONArray getDateAndTimeOfStation(String src, String dest,
			String tno, String treturn, String sdate, String rid) {
		Connection con;
		Date d;
		Calendar calendar = Calendar.getInstance();
		JSONArray data = new JSONArray();

		int trainno = Integer.parseInt(tno);
		int trainreturn = Integer.parseInt(treturn);
		int routeid = Integer.parseInt(rid);
		String selectedDate = sdate;
		try {
			con = (new DatabaseConnection()).dbConnect();
			String routedetails = "SELECT (SELECT station_name FROM station_master sr where "
					+ "sr.station_code = rm.station_code) AS station_name,distance_from_prev,station_code from route_master rm where "
					+ "rm.station_code IN (SELECT station_code FROM route_master WHERE route_id=? "
					+ "AND counter >= (SELECT counter from route_master where route_id=? "
					+ "and station_code=(SELECT source_station_code FROM `train_master` WHERE train_no=? AND train_return=?)) "
					+ "AND counter <= (SELECT counter from route_master where route_id=? "
					+ "and station_code=(SELECT dest_station_code FROM train_master WHERE train_no=? AND train_return=?)) "
					+ "ORDER BY route_id,counter) AND route_id = ?;";
			String traindetails = "SELECT train_speed,train_time FROM train_normal_schedule"
					+ " WHERE train_no=? AND train_return=?";
			Double speed = 0.0, distance = 0.0;
			int timeinsecs = 0;
			String timestamp = "";
			String stationName = "";
			PreparedStatement ps = con.prepareStatement(traindetails);
			ps.setInt(1, trainno);
			ps.setInt(2, trainreturn);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				speed = rs.getDouble("train_speed");
				timestamp = rs.getString("train_time");
			}

			timestamp = selectedDate + " " + timestamp;

			ps = con.prepareStatement(routedetails);
			ps.setInt(1, routeid);
			ps.setInt(2, routeid);
			ps.setInt(3, trainno);
			ps.setInt(4, trainreturn);
			ps.setInt(5, routeid);
			ps.setInt(6, trainno);
			ps.setInt(7, trainreturn);
			ps.setInt(8, routeid);

			rs = ps.executeQuery();
			int loopCounter = 0;
			double sumDistance = 0.0;
			while (rs.next()) {
				stationName = rs.getString("station_name");
				if (loopCounter == 0) {
					distance = 0.0;
				} else {
					distance = rs.getDouble("distance_from_prev");
				}
				sumDistance += distance;
				timeinsecs = (int) (Math.round((distance / speed) * 3600));
				d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(timestamp);
				calendar.setTime(d);
				calendar.add(Calendar.SECOND, timeinsecs);
				timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(calendar.getTime());
				if (rs.getString("station_code").equals(src)
						|| rs.getString("station_code").equals(dest)
						|| loopCounter == 0) {
					JSONObject jobj = new JSONObject();
					jobj.put("stationname", stationName);
					jobj.put("stationtime", calendar.getTime());
					jobj.put("distance", sumDistance+" km");
					data.add(jobj);
				}
				loopCounter++;
			}
			JSONObject jobj = new JSONObject();
			jobj.put("stationname", stationName);
			jobj.put("stationtime", calendar.getTime());
			jobj.put("distance", sumDistance+" km");
			data.add(jobj);
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
