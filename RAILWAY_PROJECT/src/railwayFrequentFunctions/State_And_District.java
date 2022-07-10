/**
 * 
 */
package railwayFrequentFunctions;

/**
 * @author Rahul Mukesh Singh
 *
 */

import org.geonames.WebService;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import java.util.Collections;
import org.geonames.Toponym;
import org.json.simple.JSONArray;


public class State_And_District {
	public JSONArray getData(String q, String countrycode, String featurecode) {
		JSONArray data = new JSONArray();
		try {
			PersonalData pd = new PersonalData();
			WebService.setUserName(pd.geouser); // add your username
															// here
			ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
			searchCriteria.setQ(q);
			searchCriteria.setCountryCode(countrycode);
			searchCriteria.setFeatureCode(featurecode);
			ToponymSearchResult searchResult = WebService.search(searchCriteria);

			for (Toponym toponym : searchResult.getToponyms()) {
				data.add(toponym.getName().replace((char)257, 'a').replace((char)363, 'u'));
			}
			Collections.sort(data);
		} catch (Exception e) {
			data = null;
		}
		return data;
	}
}
