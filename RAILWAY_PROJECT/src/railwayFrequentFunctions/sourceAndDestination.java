package railwayFrequentFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONArray;



public class sourceAndDestination {
 public JSONArray src_dest(String src){
	 JSONArray result = new JSONArray();
	 try {
		 	DatabaseConnection db=new DatabaseConnection();
			
			Connection con = db.dbConnect();
			String query="";
			
			if (src.trim().equals("")) {
				query="SELECT Distinct station_name FROM station_master ORDER BY station_name ASC";
			} else {
				
				query="SELECT DISTINCT station_name FROM station_master WHERE station_code IN "
						+ "(select DISTINCT station_code from route_master s2 where counter > "
						+ "(select counter from route_master where station_code IN "
						+ "(SELECT station_code from station_master where station_name=?) "
						+ "and route_id=s2.route_id)) ORDER BY station_name ASC ";
				
				
			}
			PreparedStatement ps=con.prepareStatement(query);
			
			if(!src.trim().equals(""))
			{
				ps.setString(1, src);
			}
			
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
				
			}
			con.close();
			
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 return result;
 }
}
