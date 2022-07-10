package railwayFrequentFunctions;

public class SecretKey {
public final String getSecretKey(String plain,int shift){
	String key="";
	for (int i = 0; i < plain.length(); i++) {
	key+=(char)(plain.charAt(i)+shift);
	}
	return key;
}
}
