/**
 * 
 */
package railwayFrequentFunctions;

/**
 * @author Rahul Mukesh Singh
 *
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DateFunctions {
public String dateforMinAge(int minAge)
{
	DateFormat dateFormat = new SimpleDateFormat("MM-dd");
	DateFormat adult=new SimpleDateFormat("yyyy");
	Date date = new Date();
	int adultMinYear=Integer.parseInt(adult.format(date))-minAge;
	return (adultMinYear+"-"+dateFormat.format(date));
}
public boolean isTatkalAvailable(String sdate){
	boolean tatkalAvailable=false;
	sdate+=" 10:00:00";
	try {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTimeStamp=format.parse(sdate);
		long nowww=System.currentTimeMillis();
		long startTrainTime=startTimeStamp.getTime();
		long range=1000*60*60*24; // 1 day
		if((startTrainTime-nowww) <= range){
			tatkalAvailable=true;
		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return tatkalAvailable;
}
}
