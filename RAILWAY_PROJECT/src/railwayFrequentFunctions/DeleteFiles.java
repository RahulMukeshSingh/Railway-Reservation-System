package railwayFrequentFunctions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DeleteFiles {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		String pathLocation = "E:\\JSP\\RAILWAY_PROJECT\\WebContent\\WEB-INF\\GeneratedDocuments\\";
		TimerTask timertask = new TimerTask() {
			

			public void run() {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if (hour == 12) {
					
					Path paths;
					BasicFileAttributes bfa;
					try {
						File folder = new File(pathLocation);
						File[] listOfFiles = folder.listFiles();
						for (File file : listOfFiles) {
							if (file.isFile()) {
								paths = Paths.get(pathLocation + file.getName());
								bfa = Files.readAttributes(paths, BasicFileAttributes.class);
								long fileTimeInDays=bfa.lastModifiedTime().toMillis()/(60*60*24*1000);
								long currentTimeInDays=System.currentTimeMillis()/(60*60*24*1000);
								long periodToKeepFileInDays=31*4; //(31 days * 4 = 4 months)
								if((currentTimeInDays-fileTimeInDays)>periodToKeepFileInDays){
									if(new File(pathLocation + file.getName()).exists()){
									new File(pathLocation + file.getName()).delete();
									}
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		timer.schedule(timertask, 0, 1000*60*60*24);// delay 0 and then run task every 1 day

	}

}
