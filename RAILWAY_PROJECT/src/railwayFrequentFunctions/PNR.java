package railwayFrequentFunctions;

public class PNR {
public String getPNR(){
	long nowww=System.currentTimeMillis() % (long)(1000000000000.0);
	String pnr=String.valueOf(nowww);
	return pnr;
}
}
