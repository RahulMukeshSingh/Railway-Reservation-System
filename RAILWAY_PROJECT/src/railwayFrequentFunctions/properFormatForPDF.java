package railwayFrequentFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class properFormatForPDF {
public String[] getProperFormatForPDF(String quotaid,String classId,String berthType,String coachCounter){
	String[] properFormatForPDFF=new String[4];
	
	try {
		Connection con=(new DatabaseConnection()).dbConnect();
		PreparedStatement ps;
		ResultSet rs;
		String quotaNameQuery="SELECT quota_name FROM quota_details WHERE quota_id=?";
		String classCoachNameQuery="SELECT class_name,coach_code FROM train_class_details WHERE class_id=?";
		String berthNameQuery="SELECT berth_name FROM berth_details WHERE berth_id=?";
		ps=con.prepareStatement(quotaNameQuery);
		ps.setString(1, quotaid);
		rs=ps.executeQuery();
		rs.next();
		properFormatForPDFF[0]=rs.getString(1)+"("+quotaid+")";
		ps=con.prepareStatement(classCoachNameQuery);
		ps.setString(1, classId);
		rs=ps.executeQuery();
		rs.next();
		properFormatForPDFF[1]=rs.getString(1)+"("+classId+")";
		if(!berthType.equalsIgnoreCase("-")){
		properFormatForPDFF[3]=rs.getString(2)+coachCounter;
		ps=con.prepareStatement(berthNameQuery);
		ps.setString(1, berthType);
		rs=ps.executeQuery();
		rs.next();
		properFormatForPDFF[2]=rs.getString(1)+"("+berthType+")";
		}
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	return properFormatForPDFF;
}
}
