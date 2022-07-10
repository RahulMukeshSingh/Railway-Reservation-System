package railwayFrequentFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

class EligibleSeatStructure {
	private String pnr = "";
	private int pnrCounter = 0;
	public EligibleSeatStructure() {
	
	}

	public EligibleSeatStructure(String pnr, int pnrCounter) {
	
		this.pnr = pnr;
		this.pnrCounter = pnrCounter;
	}

	

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public int getPnrCounter() {
		return pnrCounter;
	}

	public void setPnrCounter(int pnrCounter) {
		this.pnrCounter = pnrCounter;
	}
}

public class SeatCancellation {
	@Override
	public String toString() {
		return "SeatCancellation [pnrCounter=" + pnrCounter + ", seatNo="
				+ seatNo + ", seatCounter=" + seatCounter + ", coachNo="
				+ coachNo + ", statusCounter=" + statusCounter + ", trainNo="
				+ trainNo + ", trainReturn=" + trainReturn + ", istatkal="
				+ istatkal + ", pnr=" + pnr + ", quotaId=" + quotaId
				+ ", classId=" + classId + ", fromId=" + fromId + ", toId="
				+ toId + ", statusId=" + statusId + ", allottedQuotaId="
				+ allottedQuotaId + ", con=" + con + ", query=" + query
				+ ", train_src_time=" + train_src_time + ", train_dest_time="
				+ train_dest_time + ", ps=" + ps + ", rs=" + rs
				+ ", extremeStartRoute=" + extremeStartRoute
				+ ", extremeEndRoute=" + extremeEndRoute + ", routeId="
				+ routeId + "]";
	}

	private int pnrCounter = 0, seatNo = 0, seatCounter = 0, coachNo = 0,
			statusCounter = 0, trainNo = 0, trainReturn = 0, istatkal=0;
	private String pnr = "";
	private String quotaId = "", classId = "", fromId = "", toId = "",statusId="",allottedQuotaId="";
	private Connection con;
	private String query;
	private Timestamp train_src_time, train_dest_time;
	private PreparedStatement ps;
	private ResultSet rs;
	private int extremeStartRoute=0,extremeEndRoute=0,routeId=0;
	private int beforeChangeSeatNo=0,beforeChangeSeatCounter=0,beforeChangeCoachCounter=0;
	public SeatCancellation(String pnr, int pnrCounter) {
		this.pnr = pnr;
		this.pnrCounter = pnrCounter;
		con=(new DatabaseConnection()).dbConnect();
		initializeVariable(pnr, pnrCounter);
	}
	public void initializeVariable(String pnr,int pnrCounter){
	try{
		query="SELECT `status_id` FROM `booked_customer_details` WHERE `PNR`= ? AND `PNR_counter`= ?;";
		ps=con.prepareStatement(query);
		ps.setString(1, pnr);
		ps.setInt(2, pnrCounter);
		rs=ps.executeQuery();
		rs.next();
		if(rs.getString(1).equalsIgnoreCase("CNF")){
			query="SELECT * FROM `cnf` WHERE `PNR`= ? AND `PNR_counter`= ?;";
			ps=con.prepareStatement(query);
			ps.setString(1, pnr);
			ps.setInt(2, pnrCounter);
			rs=ps.executeQuery();
			rs.next();
			statusId=rs.getString(1);
			pnr=rs.getString(2);
			pnrCounter=rs.getInt(3);
			quotaId=rs.getString(4);
			trainNo=rs.getInt(5);
			trainReturn=rs.getInt(6);
			fromId=rs.getString(7);
			toId=rs.getString(8);
			classId=rs.getString(9);
			coachNo=rs.getInt(10);
			allottedQuotaId=rs.getString(11);
			seatNo=rs.getInt(12);
			train_src_time=rs.getTimestamp(13);
			train_dest_time=rs.getTimestamp(14);
		}else if(rs.getString(1).equalsIgnoreCase("RAC")){
			query="SELECT * FROM `rac` WHERE `PNR`= ? AND `PNR_counter`= ?;";
			ps=con.prepareStatement(query);
			ps.setString(1, pnr);
			ps.setInt(2, pnrCounter);
			rs=ps.executeQuery();
			rs.next();
			statusId=rs.getString(1);
			statusCounter=rs.getInt(2);
			pnr=rs.getString(3);
			pnrCounter=rs.getInt(4);
			quotaId=rs.getString(5);
			trainNo=rs.getInt(6);
			trainReturn=rs.getInt(7);
			fromId=rs.getString(8);
			toId=rs.getString(9);
			classId=rs.getString(10);
			coachNo=rs.getInt(11);
			seatNo=rs.getInt(12);
			seatCounter=rs.getInt(13);
			train_src_time=rs.getTimestamp(14);
			train_dest_time=rs.getTimestamp(15);

		}else if(rs.getString(1).equalsIgnoreCase("WL")){
			query="SELECT * FROM `wl` WHERE `PNR`= ? AND `PNR_counter`= ?;";
			ps=con.prepareStatement(query);
			ps.setString(1, pnr);
			ps.setInt(2, pnrCounter);
			rs=ps.executeQuery();
			rs.next();
			statusId=rs.getString(1);
			statusCounter=rs.getInt(2);
			pnr=rs.getString(3);
			pnrCounter=rs.getInt(4);
			quotaId=rs.getString(5);
			trainNo=rs.getInt(6);
			trainReturn=rs.getInt(7);
			fromId=rs.getString(8);
			toId=rs.getString(9);
			classId=rs.getString(10);
			istatkal=rs.getInt(11);
			train_src_time=rs.getTimestamp(12);
			train_dest_time=rs.getTimestamp(13);

		}
		query="SELECT `route_id` FROM `train_master` WHERE `train_no`= ? AND `train_return`=? ;";
		ps=con.prepareStatement(query);
		ps.setInt(1, trainNo);
		ps.setInt(2, trainReturn);
		rs=ps.executeQuery();
		rs.next();
		routeId=rs.getInt(1);
		query="SELECT counter FROM `route_master` WHERE `station_code`= ? AND `route_id`=?";
		ps=con.prepareStatement(query);
		ps.setString(1, fromId);
		ps.setInt(2, routeId);
		rs=ps.executeQuery();
		rs.next();
		extremeStartRoute=rs.getInt(1);
		query="SELECT counter FROM `route_master` WHERE `station_code`= ? AND `route_id`=?";
		ps=con.prepareStatement(query);
		ps.setString(1, toId);
		ps.setInt(2, routeId);
		rs=ps.executeQuery();
		rs.next();
		extremeEndRoute=rs.getInt(1);
		beforeChangeSeatNo=seatNo;
		beforeChangeSeatCounter=seatCounter;
		beforeChangeCoachCounter=coachNo;
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	public boolean cancelTicket(boolean passengerCancelled,String pastStatusId) {
		try{
		if(passengerCancelled){
		query="UPDATE booked_customer_details SET PNR=? WHERE PNR=? and PNR_counter=?";
		ps=con.prepareStatement(query);
		ps.setString(1, pnr+"<br>(CANCELLED)");
		ps.setString(2, pnr);
		ps.setInt(3, pnrCounter);
		ps.executeUpdate();
		pnr=pnr+"<br>(CANCELLED)";
		query="DELETE FROM booked_customer_details WHERE PNR=? and PNR_counter=?";
		ps=con.prepareStatement(query);
		ps.setString(1, pnr);
		ps.setInt(2, pnrCounter);
		ps.executeUpdate();
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		if(pastStatusId.equalsIgnoreCase("CNF")){
			System.out.println(pnr+" "+pnrCounter+" Cancelled From CNF");
			return true;
		}else if(pastStatusId.equalsIgnoreCase("RAC")){
			cancelRACTicket();
			return true;
		}else if(pastStatusId.equalsIgnoreCase("WL")){
			cancelWLTicket();
			return true;
		}else if(pastStatusId.equalsIgnoreCase("TQWL")){
			
			cancelTQWLTicket();
			return true;
		}
		return false;
	}
	public boolean cancelRACTicket(){
		try{
			System.out.println(pnr+" "+pnrCounter+" Cancelled From RAC");
			query="DELETE FROM rac WHERE PNR=? and PNR_counter=?";
			ps=con.prepareStatement(query);
			ps.setString(1, pnr);
			ps.setInt(2, pnrCounter);
			ps.executeUpdate();

			query="UPDATE `rac` SET `status_counter`=`status_counter`- 1 WHERE `status_counter` > ?";
			ps=con.prepareStatement(query);
			ps.setInt(1, statusCounter);
			ps.executeUpdate();
				return true;
			}catch(Exception e){
				return false;
			}
			
	}
	public boolean cancelTQWLTicket(){
		try{
			System.out.println(pnr+" "+pnrCounter+" Cancelled From TQWL");
			query="DELETE FROM wl WHERE PNR=? and PNR_counter=?";
			ps=con.prepareStatement(query);
			ps.setString(1, pnr);
			ps.setInt(2, pnrCounter);
			ps.executeUpdate();
			query="UPDATE `wl` SET `status_counter`=`status_counter`- 1 WHERE `status_counter` > ? AND `tatkal`=?";
			ps=con.prepareStatement(query);
			ps.setInt(1, statusCounter);
			ps.setInt(2, 1);
			ps.executeUpdate();
				return true;
			}catch(Exception e){
				return false;
			}
	}
	public boolean cancelWLTicket(){
		try{
			System.out.println(pnr+" "+pnrCounter+" Cancelled From Just WL");
			query="DELETE FROM wl WHERE PNR=? and PNR_counter=?";
			ps=con.prepareStatement(query);
			ps.setString(1, pnr);
			ps.setInt(2, pnrCounter);
			ps.executeUpdate();
			System.out.println(pnr+" "+pnrCounter+" Cancelled From Just WL");
			query="UPDATE `wl` SET `status_counter`=`status_counter`- 1 WHERE `status_counter` > ? AND `tatkal`=?";
			ps=con.prepareStatement(query);
			ps.setInt(1, statusCounter);
			ps.setInt(2, 0);
			ps.executeUpdate();
				return true;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
	}
	public EligibleSeatStructure getTQWLEligiblePassenger() {
		try{
			query="SELECT `PNR`, `PNR_counter` FROM `wl`  WHERE from_station IN "
					+ "(SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND `counter`>=?) "
					+ "AND to_station IN (SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND "
					+ "`counter`<=?) AND `train_src_time`=? AND `tatkal`=? ORDER BY status_counter;";
			ps=con.prepareStatement(query);
			ps.setInt(1, routeId);
			ps.setInt(2, extremeStartRoute);
			ps.setInt(3, routeId);
			ps.setInt(4, extremeEndRoute);
			ps.setTimestamp(5, train_src_time);
			ps.setInt(6, 1);
			rs=ps.executeQuery();
			rs.next();
			EligibleSeatStructure eligible=new EligibleSeatStructure(rs.getString(1),rs.getInt(2));
			
			return eligible;
			}catch(Exception e){
			return null;	
			}
	}

	public EligibleSeatStructure getWLEligiblePassenger() {
		try{
			query="SELECT `PNR`, `PNR_counter` FROM `wl`  WHERE from_station IN "
					+ "(SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND `counter`>=?) "
					+ "AND to_station IN (SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND "
					+ "`counter`<=?) AND `train_src_time`=? AND `tatkal`=? ORDER BY status_counter;";
			ps=con.prepareStatement(query);
			ps.setInt(1, routeId);
			ps.setInt(2, extremeStartRoute);
			ps.setInt(3, routeId);
			ps.setInt(4, extremeEndRoute);
			ps.setTimestamp(5, train_src_time);
			ps.setInt(6, 0);
			rs=ps.executeQuery();
			rs.next();
			EligibleSeatStructure eligible=new EligibleSeatStructure(rs.getString(1),rs.getInt(2));
			return eligible;
			}catch(Exception e){
			return null;	
			}
	}

	public EligibleSeatStructure getRACEligiblePassengerWithQuota(String cancelledquotaid) {
		try{
			query="SELECT `PNR`, `PNR_counter` FROM `rac`  WHERE from_station IN "
					+ "(SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND `counter`>=?) "
					+ "AND to_station IN (SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND "
					+ "`counter`<=?) AND `train_src_time`=? AND `quota_id`=? ORDER BY status_counter;";
			ps=con.prepareStatement(query);
			ps.setInt(1, routeId);
			ps.setInt(2, extremeStartRoute);
			ps.setInt(3, routeId);
			ps.setInt(4, extremeEndRoute);
			ps.setTimestamp(5, train_src_time);
			ps.setString(6, cancelledquotaid);
			rs=ps.executeQuery();
			rs.next();
			EligibleSeatStructure eligible=new EligibleSeatStructure(rs.getString(1),rs.getInt(2));
			return eligible;
			}catch(Exception e){
			return null;	
			}
			
	}

	public EligibleSeatStructure getRACEligiblePassengerWithOutQuota() {
		try{
		query="SELECT `PNR`, `PNR_counter` FROM `rac`  WHERE from_station IN "
				+ "(SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND `counter`>=?) "
				+ "AND to_station IN (SELECT `station_code` FROM `route_master` WHERE `route_id`=? AND "
				+ "`counter`<=?) AND `train_src_time`=? ORDER BY status_counter;";
		ps=con.prepareStatement(query);
		ps.setInt(1, routeId);
		ps.setInt(2, extremeStartRoute);
		ps.setInt(3, routeId);
		ps.setInt(4, extremeEndRoute);
		ps.setTimestamp(5, train_src_time);
		rs=ps.executeQuery();
		rs.next();
		EligibleSeatStructure eligible=new EligibleSeatStructure(rs.getString(1),rs.getInt(2));
		return eligible;
		}catch(Exception e){
		return null;	
		}
		
	}
	
	public boolean allocateEligibleInCNF(int cancelledSeatNo,
			int cancelledCoachNo, String cancelledAllottedQuotaId) {
		try{
			query="UPDATE `booked_customer_details` SET `status_id`=?,`status_counter`=? "
					+ "WHERE PNR=? and PNR_counter=?";
			ps=con.prepareStatement(query);
			statusId="CNF";
			ps.setString(1, statusId);
			statusCounter=0;
			ps.setInt(2, statusCounter);
			ps.setString(3, pnr);
			ps.setInt(4, pnrCounter);
			ps.executeUpdate();
			query="INSERT INTO `cnf` VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
			ps=con.prepareStatement(query);
			ps.setString(1, statusId);
			ps.setString(2, pnr);
			ps.setInt(3,pnrCounter);
			ps.setString(4, quotaId);
			ps.setInt(5, trainNo);
			ps.setInt(6, trainReturn);
			ps.setString(7, fromId);
			ps.setString(8, toId);
			ps.setString(9, classId);
			coachNo=cancelledCoachNo;
			ps.setInt(10, coachNo);
			allottedQuotaId=cancelledAllottedQuotaId;
			ps.setString(11, allottedQuotaId);
			seatNo=cancelledSeatNo;
			ps.setInt(12, seatNo);
			ps.setTimestamp(13, train_src_time);
			ps.setTimestamp(14, train_dest_time);
			ps.executeUpdate();
				return true;
			}catch(Exception e){
				return false;
			}
	}

	public boolean allocateEligibleInRAC(int cancelledSeatNo,
			int cancelledSeatCounter, int cancelledCoachNo) {
		try{
			query="SELECT count(*) from rac where `train_no`=? and `train_return`=? and `class_id`=?";
			ps=con.prepareStatement(query);
			ps.setInt(1, trainNo);
			ps.setInt(2, trainReturn);
			ps.setString(3, classId);
			rs=ps.executeQuery();
			rs.next();
			statusCounter=rs.getInt(1)+1;
			query="UPDATE `booked_customer_details` SET `status_id`=?,`status_counter`=? "
					+ "WHERE PNR=? and PNR_counter=?";
			ps=con.prepareStatement(query);
			statusId="RAC";
			ps.setString(1, statusId);
			ps.setInt(2, statusCounter);
			ps.setString(3, pnr);
			ps.setInt(4, pnrCounter);
			ps.executeUpdate();
			query="INSERT INTO `rac` VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
			ps=con.prepareStatement(query);
			ps.setString(1, statusId);
			ps.setInt(2, statusCounter);
			ps.setString(3, pnr);
			ps.setInt(4,pnrCounter);
			ps.setString(5, quotaId);
			ps.setInt(6, trainNo);
			ps.setInt(7, trainReturn);
			ps.setString(8, fromId);
			ps.setString(9, toId);
			ps.setString(10, classId);
			coachNo=cancelledCoachNo;
			ps.setInt(11, coachNo);
			seatNo=cancelledSeatNo;
			ps.setInt(12, seatNo);
			seatCounter=cancelledSeatCounter;
			ps.setInt(13, seatCounter);
			ps.setTimestamp(14, train_src_time);
			ps.setTimestamp(15, train_dest_time);
			ps.executeUpdate();
				return true;
			}catch(Exception e){
				return false;
			}
	}

	public boolean WLCancelProcess(boolean isPassengerCancelled) {
		if (cancelTicket(isPassengerCancelled,"WL")) {
			return true;
		}
		return false;
	}

	public boolean TQWLCancelProcess(boolean isPassengerCancelled) {
		if (cancelTicket(isPassengerCancelled,"TQWL")) {
			
			return true;
		}
		return false;
	}

	public boolean RACCancelProcess(boolean isPassengerCancelled) {
		EligibleSeatStructure eligible;
		if (cancelTicket(isPassengerCancelled,"RAC")) {
			eligible = getWLEligiblePassenger();
			if (eligible != null) {
				SeatCancellation eligibleSeatCancel = new SeatCancellation(
						eligible.getPnr(), eligible.getPnrCounter());
				System.out.println(beforeChangeSeatNo+" "+beforeChangeCoachCounter+" "+beforeChangeSeatCounter);
				if (eligibleSeatCancel.allocateEligibleInRAC(beforeChangeSeatNo,
						beforeChangeSeatCounter, beforeChangeCoachCounter)) {
					eligibleSeatCancel.WLCancelProcess(false);
					return true;
				}
			}
		}
		return false;
	}

	public boolean CNFCancelProcess(boolean isPassengerCancelled) {
		EligibleSeatStructure eligible;
		if (cancelTicket(isPassengerCancelled,"CNF")) {
			if (allottedQuotaId.equalsIgnoreCase("TQ")) {
				eligible = getTQWLEligiblePassenger();
				if (eligible != null) {
					SeatCancellation eligibleSeatCancel = new SeatCancellation(
							eligible.getPnr(), eligible.getPnrCounter());
					
					if (eligibleSeatCancel.allocateEligibleInCNF(seatNo,
							coachNo,allottedQuotaId)) {
						eligibleSeatCancel.TQWLCancelProcess(false);
						return true;
					}
				}
			} else if (allottedQuotaId.equalsIgnoreCase("GN")) {
				eligible = getTQWLEligiblePassenger();
				if (eligible != null) {
					SeatCancellation eligibleSeatCancel = new SeatCancellation(
							eligible.getPnr(), eligible.getPnrCounter());
					if (eligibleSeatCancel.allocateEligibleInCNF(seatNo,
							coachNo,allottedQuotaId)) {
						eligibleSeatCancel.TQWLCancelProcess(false);
						return true;
					}
				} else {
					eligible = getRACEligiblePassengerWithOutQuota();
					if (eligible != null) {
						SeatCancellation eligibleSeatCancel = new SeatCancellation(
								eligible.getPnr(), eligible.getPnrCounter());
						System.out.println(eligibleSeatCancel.toString());
						if (eligibleSeatCancel.allocateEligibleInCNF(
								seatNo, coachNo,allottedQuotaId)) {
							
							eligibleSeatCancel.RACCancelProcess(false);
							return true;
						}
					}
				}
			} else {
				eligible = getRACEligiblePassengerWithQuota(allottedQuotaId);
				if (eligible != null) {
					SeatCancellation eligibleSeatCancel = new SeatCancellation(
							eligible.getPnr(), eligible.getPnrCounter());
					if (eligibleSeatCancel.allocateEligibleInCNF(seatNo,
							coachNo,allottedQuotaId)) {
						eligibleSeatCancel.RACCancelProcess(false);
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		try{
		SeatCancellation sc=new SeatCancellation("522133436839", 60);
		System.out.println(sc.toString());
		sc.WLCancelProcess(true);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
