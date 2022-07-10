package railwayFrequentFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class quotaSeatStructure {
	private String quotaId;
	private int totalNoOfSeats;

	public quotaSeatStructure(String quotaId, int totalNoOfSeats) {
		this.setQuotaId(quotaId);
		this.setTotalNoOfSeats(totalNoOfSeats);
	}

	public String getQuotaId() {
		return quotaId;
	}

	public void setQuotaId(String quotaId) {
		this.quotaId = quotaId;
	}

	public int getTotalNoOfSeats() {
		return totalNoOfSeats;
	}

	public void setTotalNoOfSeats(int totalNoOfSeats) {
		this.totalNoOfSeats = totalNoOfSeats;
	}
}

class seatsForClass {
	private int coachSeatNo;
	private int coachNo;
	private String berthType;
	private String allotedQuotaId;
	private int coachSeatCounter;

	public seatsForClass(int coachNo, int coachSeatNo, String berthType) {
		this.setCoachSeatNo(coachSeatNo);
		this.setCoachNo(coachNo);
		this.setBerthType(berthType);
	}

	public seatsForClass(String allotedQuotaId, int coachNo, int coachSeatNo) {
		this.setCoachSeatNo(coachSeatNo);
		this.setCoachNo(coachNo);
		this.setAllotedQuotaId(allotedQuotaId);
	}

	public seatsForClass(int coachSeatNo, String berthType) {
		this.setCoachSeatNo(coachSeatNo);
		this.setBerthType(berthType);
	}

	public seatsForClass(int coachNo, int coachSeatNo, int coachSeatCounter) {
		this.setCoachSeatNo(coachSeatNo);
		this.setCoachNo(coachNo);
		this.setCoachSeatCounter(coachSeatCounter);
	}

	public int getCoachSeatNo() {
		return coachSeatNo;
	}

	public void setCoachSeatNo(int coachSeatNo) {
		this.coachSeatNo = coachSeatNo;
	}

	public int getCoachNo() {
		return coachNo;
	}

	public void setCoachNo(int coachNo) {
		this.coachNo = coachNo;
	}

	public String getBerthType() {
		return berthType;
	}

	public void setBerthType(String berthType) {
		this.berthType = berthType;
	}

	public String getAllotedQuotaId() {
		return allotedQuotaId;
	}

	public void setAllotedQuotaId(String allotedQuotaId) {
		this.allotedQuotaId = allotedQuotaId;
	}

	public int getCoachSeatCounter() {
		return coachSeatCounter;
	}

	public void setCoachSeatCounter(int coachSeatCounter) {
		this.coachSeatCounter = coachSeatCounter;
	}

} 

public class SeatAllocation {
	private Connection con;
	private int noOfCoaches = 0, noOfConfirmSeatsInPerClass = 0,
			noOfRACSeatsInPerClass = 0;
	private int trainno, trainreturn, routeid;
	private String classid, fromid, toid;
	private DatabaseConnection db = new DatabaseConnection();
	private PreparedStatement ps;
	private ResultSet rs;
	private int totalNoConfirmSeats = 0, totalNoRACSeats = 0;
	private ArrayList<quotaSeatStructure> totalQuotaSeatsList = new ArrayList<quotaSeatStructure>();
	private ArrayList<seatsForClass> totalConfirmSeatsFromMiddle = new ArrayList<seatsForClass>();
	private ArrayList<seatsForClass> totalRACSeatsFromMiddle = new ArrayList<seatsForClass>();
	private ArrayList<quotaSeatStructure> availableQuotaSeatsList = new ArrayList<quotaSeatStructure>();
	private ArrayList<seatsForClass> availableConfirmSeatsFromMiddle = new ArrayList<seatsForClass>();
	private ArrayList<seatsForClass> availableRACSeatsFromMiddle = new ArrayList<seatsForClass>();
	private int availableNoOfRACSeats = 0, totalWL = 0, totalTQWL = 0,
			availableWL = 0, availableTQWL = 0;
	private Timestamp startTimeStampSql;
	
	public SeatAllocation(String trainno, String trainreturn, String classid,
			String routeid, String fromid, String toid,String date) {
		this.trainno = Integer.parseInt(trainno);
		this.trainreturn = Integer.parseInt(trainreturn);
		this.routeid = Integer.parseInt(routeid);
		this.classid = classid;
		this.fromid = fromid;
		this.toid = toid;
		try {
			con = db.dbConnect();
			String noOfCoachesQuery = "SELECT no_of_class FROM train_coach_details WHERE class_id=? and train_no=?";
			ps = con.prepareStatement(noOfCoachesQuery);
			ps.setString(1, this.classid);
			ps.setInt(2, this.trainno);
			rs = ps.executeQuery();
			rs.next();
			noOfCoaches = rs.getInt(1);
			String startTimeStampQuery="SELECT train_time FROM train_normal_schedule WHERE train_no=? "
					+ "AND train_return=?";
			ps=con.prepareStatement(startTimeStampQuery);
			ps.setInt(1,this.trainno);
			ps.setInt(2,this.trainreturn);
			rs=ps.executeQuery();
			rs.next();
			String startTimeStamp=date+" "+rs.getString(1);
			//System.out.println(startTimeStamp);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDateTimeStamp=format.parse(startTimeStamp);
			//System.out.println("sa "+startDateTimeStamp);
			startTimeStampSql=new Timestamp(startDateTimeStamp.getTime()); 
			con.close();
			getCNFTotalSeats();
			getRACTotalSeats();
			getWLTotalSeats();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<seatsForClass> arrayListExclude(
			ArrayList<seatsForClass> totalList,
			ArrayList<seatsForClass> excludeThisList) {
		ArrayList<seatsForClass> excluded = new ArrayList<seatsForClass>();
		boolean found = false;
		for (seatsForClass tl : totalList) {
			found = false;
			for (seatsForClass etl : excludeThisList) {
				if (tl.getCoachNo() == etl.getCoachNo()
						&& tl.getCoachSeatNo() == etl.getCoachSeatNo()) {
					found = true;
					break;
				}
			}
			if (!found) {
				excluded.add(new seatsForClass(tl.getCoachNo(), tl
						.getCoachSeatNo(), tl.getBerthType()));
			}
		}
		return excluded;
	}

	public ArrayList<quotaSeatStructure> arrayListDeduct(
			ArrayList<quotaSeatStructure> totalQuotaList,
			ArrayList<seatsForClass> deductQuotaList) {
		ArrayList<quotaSeatStructure> deducted = new ArrayList<quotaSeatStructure>();
		for (quotaSeatStructure tql : totalQuotaList) {
			deducted.add(new quotaSeatStructure(tql.getQuotaId(), tql
					.getTotalNoOfSeats()));
		}
		for (seatsForClass dql : deductQuotaList) {
			for (quotaSeatStructure d : deducted) {
				if (d.getQuotaId().equals(dql.getAllotedQuotaId())) {
					d.setTotalNoOfSeats(d.getTotalNoOfSeats() - 1);
					break;
				}
			}
		}

		return deducted;
	}

	public void getAvailableQuotaAndConfirmList(
			ArrayList<quotaSeatStructure> totalQuotaSeats,
			ArrayList<seatsForClass> totalMiddleConfirmSeats) {
		ArrayList<seatsForClass> notAvailableSeats = new ArrayList<seatsForClass>();
		try {
			String NotAvailableSeatsQuery = "SELECT coach_counter,seat_no,seat_allotted_quota_id from cnf c "
					+ "where (SELECT count(*) from route_master where route_id=? and "
					+ "(counter BETWEEN (select counter from route_master where station_code=c.from_station and route_id=?) AND "
					+ "(SELECT counter from route_master where station_code=c.to_station and route_id=?)) and "
					+ "counter IN (SELECT counter from route_master where "
					+ "(counter BETWEEN (select counter from route_master where station_code=? and route_id=?) AND "
					+ "(SELECT counter from route_master where station_code=? and route_id=?)) AND route_id=?)) > 1 "
					+ "and train_no=? and train_return=? AND class_id=? AND train_src_time = ?";
			con = db.dbConnect();
			ps = con.prepareStatement(NotAvailableSeatsQuery);
			ps.setInt(1, routeid);
			ps.setInt(2, routeid);
			ps.setInt(3, routeid);
			ps.setString(4, fromid);
			ps.setInt(5, routeid);
			ps.setString(6, toid);
			ps.setInt(7, routeid);
			ps.setInt(8, routeid);
			ps.setInt(9, trainno);
			ps.setInt(10, trainreturn);
			ps.setString(11, classid);
			ps.setTimestamp(12, startTimeStampSql);
			rs = ps.executeQuery();
			while (rs.next()) {
				notAvailableSeats.add(new seatsForClass(rs.getString(3), rs
						.getInt(1), rs.getInt(2)));
			//	System.out.println(rs.getString(3)+" "+ rs.getInt(1)+" "+ rs.getInt(2));
			}
			availableConfirmSeatsFromMiddle = arrayListExclude(
					totalMiddleConfirmSeats, notAvailableSeats);
			availableQuotaSeatsList = arrayListDeduct(totalQuotaSeats,
					notAvailableSeats);
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<seatsForClass> getMiddleSeatsPerClass(
			ArrayList<seatsForClass> seatsInAscendingOrder) {
		int length = seatsInAscendingOrder.size();
		int startIndex = length / 2;
		int minIndex = startIndex - 1;
		int maxIndex = startIndex + 1;
		ArrayList<seatsForClass> middleSeatsPerClass = new ArrayList<seatsForClass>();
		
		middleSeatsPerClass.add(seatsInAscendingOrder.get(startIndex));
		for (int i = 0; i < startIndex; i++) {
			if (minIndex >= 0) {
				middleSeatsPerClass.add(seatsInAscendingOrder.get(minIndex));
				minIndex--;
			}
			if (maxIndex <= (length - 1)) {
				middleSeatsPerClass.add(seatsInAscendingOrder.get(maxIndex));
				maxIndex++;
			}
		}
		return middleSeatsPerClass;
	}

	public ArrayList<Integer> getMiddleSeatsPerClassRAC(
			ArrayList<Integer> seatsInAscendingOrder) {
		int length = seatsInAscendingOrder.size();
		ArrayList<Integer> middleSeatsPerClass = new ArrayList<Integer>();
		if (length > 0) {
			int startIndex = length / 2;
			int minIndex = startIndex - 1;
			int maxIndex = startIndex + 1;
			middleSeatsPerClass.add(seatsInAscendingOrder.get(startIndex));
			for (int i = 0; i < startIndex; i++) {
				if (minIndex >= 0) {
					middleSeatsPerClass
							.add(seatsInAscendingOrder.get(minIndex));
					minIndex--;
				}
				if (maxIndex <= (length - 1)) {
					middleSeatsPerClass
							.add(seatsInAscendingOrder.get(maxIndex));
					maxIndex++;
				}
			}
		}
		return middleSeatsPerClass;
	}

	public void getCNFTotalSeats() {

		try {
			con = db.dbConnect();
			String noOfConfirmSeatsInPerClassQuery = "SELECT seat_no,berth_id FROM "
					+ classid + " WHERE berth_id <> ? order by seat_no";
			ps = con.prepareStatement(noOfConfirmSeatsInPerClassQuery);
			ps.setString(1, "SLB");
			rs = ps.executeQuery();
			ArrayList<seatsForClass> seatsForConfirmPerCoach = new ArrayList<seatsForClass>();
			while (rs.next()) {
				seatsForConfirmPerCoach.add(new seatsForClass(rs.getInt(1), rs
						.getString(2)));
			}
			noOfConfirmSeatsInPerClass = seatsForConfirmPerCoach.size();
			seatsForConfirmPerCoach = getMiddleSeatsPerClass(seatsForConfirmPerCoach);
			for (int i = 1; i <= noOfCoaches; i++) {
				for (seatsForClass seatsDetails : seatsForConfirmPerCoach) {
					totalConfirmSeatsFromMiddle.add(new seatsForClass(i,
							seatsDetails.getCoachSeatNo(), seatsDetails
									.getBerthType()));
				}
			}

			totalNoConfirmSeats = totalConfirmSeatsFromMiddle.size();
			String getQuotaSeatsQuery = "SELECT quota_id,seats_quota FROM quota_details order by seats_quota desc";
			ps = con.prepareStatement(getQuotaSeatsQuery);
			rs = ps.executeQuery();
			int quotaSeats = 0;
			int remQuotaSeats = totalNoConfirmSeats;
			while (rs.next()) {
				String quotaid = rs.getString(1);
				quotaSeats = (int) (Math.floor(totalNoConfirmSeats
						* (rs.getDouble(2) / 100)));
				totalQuotaSeatsList.add(new quotaSeatStructure(quotaid,
						quotaSeats));
				remQuotaSeats -= quotaSeats;
			}
			for (quotaSeatStructure qs : totalQuotaSeatsList) {
				if (qs.getQuotaId().equalsIgnoreCase("GN")) {
					qs.setTotalNoOfSeats(qs.getTotalNoOfSeats() + remQuotaSeats);
				}
			}
			con.close();
			getAvailableQuotaAndConfirmList(totalQuotaSeatsList,
					totalConfirmSeatsFromMiddle);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getRACTotalSeats() {
		ArrayList<Integer> RACSeatsAscendingOrder = new ArrayList<Integer>();
		ArrayList<Integer> RACSeatsMiddleOrder = new ArrayList<Integer>();
		try {
			con = db.dbConnect();
			String noOfRACSeatsInPerClassQuery = "SELECT seat_no FROM "
					+ classid + " WHERE berth_id = ?";
			ps = con.prepareStatement(noOfRACSeatsInPerClassQuery);
			ps.setString(1, "SLB");
			rs = ps.executeQuery();
			while (rs.next()) {
				RACSeatsAscendingOrder.add(rs.getInt(1));
			}
			noOfRACSeatsInPerClass = RACSeatsAscendingOrder.size();
			RACSeatsMiddleOrder = getMiddleSeatsPerClassRAC(RACSeatsAscendingOrder);
			for (int i = 1; i <= noOfCoaches; i++) {
				for (Integer rsmo : RACSeatsMiddleOrder) {
					for (int j = 1; j <= 2; j++) {
						totalRACSeatsFromMiddle.add(new seatsForClass(i, rsmo,
								j));
					}
				}
			}
			totalNoRACSeats = totalRACSeatsFromMiddle.size();
			getAvailableRACList(totalRACSeatsFromMiddle);
			con.close();
			availableNoOfRACSeats = availableRACSeatsFromMiddle.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAvailableRACList(ArrayList<seatsForClass> totalMiddleRACSeats) {
		ArrayList<seatsForClass> notAvailableSeats = new ArrayList<seatsForClass>();
		try {
			String NotAvailableSeatsQuery = "SELECT coach_counter,seat_no,seat_counter from rac r "
					+ "where (SELECT count(*) from route_master where route_id=? and "
					+ "(counter BETWEEN (select counter from route_master where station_code=r.from_station and route_id=?) AND "
					+ "(SELECT counter from route_master where station_code=r.to_station and route_id=?)) and "
					+ "counter IN (SELECT counter from route_master where "
					+ "(counter BETWEEN (select counter from route_master where station_code=? and route_id=?) AND "
					+ "(SELECT counter from route_master where station_code=? and route_id=?)) AND route_id=?)) > 1 "
					+ "and train_no=? and train_return=? AND class_id=? AND train_src_time = ?";
			con = db.dbConnect();
			ps = con.prepareStatement(NotAvailableSeatsQuery);
			ps.setInt(1, routeid);
			ps.setInt(2, routeid);
			ps.setInt(3, routeid);
			ps.setString(4, fromid);
			ps.setInt(5, routeid);
			ps.setString(6, toid);
			ps.setInt(7, routeid);
			ps.setInt(8, routeid);
			ps.setInt(9, trainno);
			ps.setInt(10, trainreturn);
			ps.setString(11, classid);
			ps.setTimestamp(12, startTimeStampSql);
			rs = ps.executeQuery();
			while (rs.next()) {
				notAvailableSeats.add(new seatsForClass(rs.getInt(1), rs
						.getInt(2), rs.getInt(3)));
			}
			con.close();
			availableRACSeatsFromMiddle = arrayListRACExclude(
					totalMiddleRACSeats, notAvailableSeats);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<seatsForClass> arrayListRACExclude(
			ArrayList<seatsForClass> totalList,
			ArrayList<seatsForClass> excludeThisList) {
		ArrayList<seatsForClass> excluded = new ArrayList<seatsForClass>();
		boolean found = false;
		for (seatsForClass tl : totalList) {
			found = false;
			for (seatsForClass etl : excludeThisList) {
				if (tl.getCoachNo() == etl.getCoachNo()
						&& tl.getCoachSeatNo() == etl.getCoachSeatNo()
						&& tl.getCoachSeatCounter() == etl
								.getCoachSeatCounter()) {
					found = true;
					break;
				}
			}
			if (!found) {
				excluded.add(new seatsForClass(tl.getCoachNo(), tl
						.getCoachSeatNo(), tl.getCoachSeatCounter()));
			}
		}
		return excluded;
	}

	public void getWLTotalSeats() {
		try {
			con = db.dbConnect();
			String totalSeatsQuery = "SELECT wait_limit,(SELECT seats_quota FROM quota_details WHERE "
					+ "quota_id=?) from waiting_limit where class_id=?";
			ps = con.prepareStatement(totalSeatsQuery);
			ps.setString(1, "TQ");
			ps.setString(2, classid);
			rs = ps.executeQuery();
			rs.next();
			int seatsQuota = rs.getInt(2);
			totalWL = rs.getInt(1) - (rs.getInt(1) * seatsQuota / 100);
			totalTQWL = rs.getInt(1) - totalWL;
			String notAvailableNormalSeatsQuery = "SELECT COUNT(*) FROM wl WHERE "
					+ "train_no=? AND train_return=? AND class_id=? AND tatkal=? AND train_src_time = ?";
			ps = con.prepareStatement(notAvailableNormalSeatsQuery);
			ps.setInt(1, trainno);
			ps.setInt(2, trainreturn);
			ps.setString(3, classid);
			ps.setInt(4, 0);
			ps.setTimestamp(5, startTimeStampSql);
			rs = ps.executeQuery();
			rs.next();
			availableWL = totalWL - rs.getInt(1);
			String notAvailableTatkalSeatsQuery = "SELECT COUNT(*) FROM wl WHERE "
					+ "train_no=? AND train_return=? AND class_id=? AND tatkal=? AND train_src_time = ?";
			ps = con.prepareStatement(notAvailableTatkalSeatsQuery);
			ps.setInt(1, trainno);
			ps.setInt(2, trainreturn);
			ps.setString(3, classid);
			ps.setInt(4, 1);
			ps.setTimestamp(5, startTimeStampSql);
			rs = ps.executeQuery();
			rs.next();
			availableTQWL = totalTQWL - rs.getInt(1);
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONArray getTotalNoOfSeats() {
		

		JSONArray seats = new JSONArray();

		for (quotaSeatStructure aqsl : availableQuotaSeatsList) {
			JSONObject seat = new JSONObject();
			seat.put("cnf", aqsl.getTotalNoOfSeats());
			if (!aqsl.getQuotaId().equals("TQ")) {
				seat.put("rac", availableNoOfRACSeats);
				seat.put("wl", availableWL);
			} else {
				seat.put("rac", 0);
				seat.put("wl", availableTQWL);
			}
			seats.add(seat);
		}
		return seats;
	}
			
	public String getAvailableSeats(String quotaId,int cnfGn,int rac,int wl,int tqwl,int cnfQuot){
		String allotedSeat="";

		//System.out.println(cnfGn+" "+ rac+" "+ wl+" "+ tqwl+" "+ cnfQuot);
		if(quotaId.equals("GN")){
			allotedSeat=getAvailableSeatForGN(quotaId, rac, wl,cnfQuot);	
		}else{
			allotedSeat=getAvailableSeatForOther(quotaId, cnfGn, rac, wl, tqwl,cnfQuot);
		}
		return allotedSeat;
	}
	public String getAvailableSeatForGN(String quotaId,int rac,int wl,int cnfQuot){
		String allotedSeat="";
		for (quotaSeatStructure aqsl : availableQuotaSeatsList) {
			if(aqsl.getQuotaId().equals(quotaId)){
				if((aqsl.getTotalNoOfSeats()-cnfQuot)>0){
					allotedSeat="Confirm(CNF)/////"+quotaId;
				}
				break;
			}
		}
		if(allotedSeat.length()==0 && (availableNoOfRACSeats-rac)>0){
			allotedSeat="Reserve Against Cancellation(RAC "+(getRACLatestListNo()+rac)+")/////"+quotaId;
		}else if(allotedSeat.length()==0 && (availableWL-wl)>0){
			allotedSeat="Waiting(WL "+(getWLLatestListNo()+wl)+")/////"+quotaId;
		}else if(allotedSeat.length()==0){
			allotedSeat="No Ticket Available";
		}
		
		return allotedSeat;
	}
	public String getAvailableSeatForOther(String quotaId,int cnfGn,int rac,int wl,int tqwl,int cnfQuot){
		String allotedSeat="";
		for (quotaSeatStructure aqsl : availableQuotaSeatsList) {
			if(aqsl.getQuotaId().equals(quotaId)){
				if((aqsl.getTotalNoOfSeats()-cnfQuot)>0){
					allotedSeat="Confirm(CNF)/////"+quotaId;
				}
				break;
			}
		}
		if(allotedSeat.length()==0){
			for (quotaSeatStructure aqsl : availableQuotaSeatsList) {
				if(aqsl.getQuotaId().equals("GN")){
					if((aqsl.getTotalNoOfSeats()-cnfGn)>0){
						allotedSeat="Confirm(CNF)/////GN";
					}
					break;
				}
			}
		}
		if(allotedSeat.length()==0 && (availableNoOfRACSeats - rac)>0 && !(quotaId.equals("TQ"))){
			allotedSeat="Reserve Against Cancellation(RAC "+(getRACLatestListNo()+rac)+")/////"+quotaId;
		}else if(allotedSeat.length()==0){
			//System.out.println("Hello");
			if(quotaId.equals("TQ") && (availableTQWL - tqwl)>0){
			allotedSeat="Tatkal Waiting(TQWL "+(getTQWLLatestListNo()+tqwl)+")/////"+quotaId;
			}
			if((!(quotaId.equals("TQ"))) && (availableWL -wl)>0){
				allotedSeat="Waiting(WL "+(getWLLatestListNo()+wl)+")/////"+quotaId;	
			}
		}
		if(allotedSeat.length()==0){
			allotedSeat="No Ticket Available";
		}
		
		return allotedSeat;
	}
	
	public int getRACLatestListNo(){
		return totalNoRACSeats-availableNoOfRACSeats+1;
	}
	public int getWLLatestListNo(){
		return totalWL-availableWL+1;
	}
	public int getTQWLLatestListNo(){
		return totalTQWL-availableTQWL+1;
	}
	public JSONArray getAvailableBerth(){
		
		JSONArray availableBerth=new JSONArray();
		JSONArray availableBerthWithName=new JSONArray();
		
		int i=0;
		for (seatsForClass acsfm : availableConfirmSeatsFromMiddle) {
			if(i==0){
				availableBerth.add(acsfm.getBerthType());
			}else{
				boolean found=false;
				for (Object ab : availableBerth) {
					if(ab.toString().equals(acsfm.getBerthType())){
						found=true;
						break;
					}
				}
				if(!found){
					availableBerth.add(acsfm.getBerthType());
				}
			}
			
			i++;
		}
		String ab=availableBerth.toString().replace("[", "").replace("]", "").replace("\"", "\'");
		String queryBerthName="SELECT berth_name,berth_id FROM berth_details WHERE berth_id in ("+ab+")";
		try{
		con=db.dbConnect();
		ps=con.prepareStatement(queryBerthName);
		rs=ps.executeQuery();
		i=0;
		JSONObject jobj=new JSONObject();
		jobj.put("name", "No Preference");
		jobj.put("id", "NP");
		availableBerthWithName.add(jobj);
		while(rs.next()){
			jobj=new JSONObject();
			jobj.put("name", rs.getString(1));
			jobj.put("id", rs.getString(2));
			availableBerthWithName.add(jobj);
			i++;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return availableBerthWithName;
	}
	
	public String[] getFormattedStatus(String totalStatusWithId) {
		String[] totalStatusWithIdArray=totalStatusWithId.split("/////");
		String totalStatus=totalStatusWithIdArray[0];
		String[] formattedStatusArray=new String[4];
		int startBracket=totalStatus.indexOf('(');
		int endBracket=totalStatus.indexOf(')');
		formattedStatusArray[0]=totalStatus.substring(0, startBracket);
		String insideBracket=totalStatus.substring(startBracket+1, endBracket);
		String[] idAndNumber=insideBracket.split(" ");
		int count=1;
		for (String ian : idAndNumber) {
			formattedStatusArray[count]=ian;
			count++;
		}
		if(formattedStatusArray[1].equalsIgnoreCase("CNF")){
			formattedStatusArray[2]="0";
		}
		formattedStatusArray[3]=totalStatusWithIdArray[1];
		return formattedStatusArray;
	}
	public seatsForClass getCNFSeatsForPassenger(String pref){
		seatsForClass sfc=null;
		boolean found=false;
		for (seatsForClass acsfm : availableConfirmSeatsFromMiddle) {
			if(acsfm.getBerthType().equalsIgnoreCase(pref)){
				sfc=acsfm;
				found=true;
				break;
			}
		}
		if(!found){
			sfc=availableConfirmSeatsFromMiddle.get(0);
		}
		return sfc;
	}

	public seatsForClass getRACSeatsForPassenger(){
		seatsForClass sfc=availableRACSeatsFromMiddle.get(0);
		return sfc;
	}
	public JSONObject insertIntoCNFTable(String[] formattedStatus,String pnr,String quotaId,String pref,int pnrCounter,
			Connection c,PreparedStatement p,JSONObject dataINJSONObject,Timestamp trainSrcTime,
			Timestamp trainDestTime){
		try {
			String cnfQuery="INSERT INTO cnf VALUES (?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			p=c.prepareStatement(cnfQuery);
			p.setString(1, formattedStatus[1]);
			p.setString(2, pnr);
			p.setInt(3, pnrCounter);
			p.setString(4, quotaId);
			p.setInt(5, trainno);
			p.setInt(6, trainreturn);
			p.setString(7, fromid);
			p.setString(8, toid);
			p.setString(9, classid);
			seatsForClass sfc=getCNFSeatsForPassenger(pref);
			p.setInt(10, sfc.getCoachNo());
			p.setString(11, formattedStatus[3]);
			p.setInt(12, sfc.getCoachSeatNo());
			p.setTimestamp(13, trainSrcTime);
			p.setTimestamp(14, trainDestTime);

			dataINJSONObject.put("coachNo", sfc.getCoachNo());
			dataINJSONObject.put("berthId", sfc.getBerthType());
			dataINJSONObject.put("seatNo", sfc.getCoachSeatNo());
			p.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataINJSONObject;
	}
	public JSONObject insertIntoRACTable(String[] formattedStatus,String pnr,String quotaId,int pnrCounter,
			Connection c,PreparedStatement p,JSONObject dataINJSONObject,Timestamp trainSrcTime,
			Timestamp trainDestTime){
		try {
			String racQuery="INSERT INTO rac VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			p=c.prepareStatement(racQuery);
			p.setString(1, formattedStatus[1]);
			p.setInt(2,Integer.parseInt(formattedStatus[2].trim()));
			p.setString(3, pnr);
			p.setInt(4, pnrCounter);
			p.setString(5, quotaId);
			p.setInt(6, trainno);
			p.setInt(7, trainreturn);
			p.setString(8, fromid);
			p.setString(9, toid);
			p.setString(10, classid);
			seatsForClass sfc=getRACSeatsForPassenger();
			p.setInt(11, sfc.getCoachNo());
			p.setInt(12, sfc.getCoachSeatNo());
			p.setInt(13, sfc.getCoachSeatCounter());
			p.setTimestamp(14, trainSrcTime);
			p.setTimestamp(15, trainDestTime);
			p.executeUpdate();
			dataINJSONObject.put("coachNo", sfc.getCoachNo());
			dataINJSONObject.put("berthId", "SLB");
			dataINJSONObject.put("seatNo", sfc.getCoachSeatNo());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataINJSONObject;
	}
	public JSONObject insertIntoWLTable(String[] formattedStatus,String pnr,String quotaId,int pnrCounter,int tatkal,
			Connection c,PreparedStatement p,JSONObject dataINJSONObject,Timestamp trainSrcTime,
			Timestamp trainDestTime){
		try {
			String racQuery="INSERT INTO wl VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
			p=c.prepareStatement(racQuery);
			p.setString(1, formattedStatus[1]);
			p.setInt(2,Integer.parseInt(formattedStatus[2].trim()));
			p.setString(3, pnr);
			p.setInt(4, pnrCounter);
			p.setString(5, quotaId);
			p.setInt(6, trainno);
			p.setInt(7, trainreturn);
			p.setString(8, fromid);
			p.setString(9, toid);
			p.setString(10, classid);
			p.setInt(11, tatkal);
			p.setTimestamp(12, trainSrcTime);
			p.setTimestamp(13, trainDestTime);
			p.executeUpdate();
			dataINJSONObject.put("coachNo", "-");
			dataINJSONObject.put("berthId", "-");
			dataINJSONObject.put("seatNo", "-");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataINJSONObject;
	}
	
	public JSONObject insertIntoStatusTable(String[] formattedStatus,String pnr,String quotaId,String pref,
			int pnrCounter,Connection c,PreparedStatement p,JSONObject dataINJSONObject,Timestamp trainSrcTime,
			Timestamp trainDestTime){
		JSONObject newDataINJSONObject=new JSONObject();
		if(formattedStatus[1].equalsIgnoreCase("CNF")){
			newDataINJSONObject=insertIntoCNFTable(formattedStatus,pnr,quotaId,pref,pnrCounter,c,p,dataINJSONObject,trainSrcTime,trainDestTime);
		}else if(formattedStatus[1].equalsIgnoreCase("RAC")){
			newDataINJSONObject=insertIntoRACTable(formattedStatus,pnr,quotaId,pnrCounter,c,p,dataINJSONObject,trainSrcTime,trainDestTime);
		}else if(formattedStatus[1].equalsIgnoreCase("WL")){
			newDataINJSONObject=insertIntoWLTable(formattedStatus,pnr,quotaId,pnrCounter,0,c,p,dataINJSONObject,trainSrcTime,trainDestTime);
		}else if(formattedStatus[1].equalsIgnoreCase("TQWL")){
			formattedStatus[1]="WL";
			newDataINJSONObject=insertIntoWLTable(formattedStatus,pnr,quotaId,pnrCounter,1,c,p,dataINJSONObject,trainSrcTime,trainDestTime);
		} 
		return newDataINJSONObject;
	}

}
