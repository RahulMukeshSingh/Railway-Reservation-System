/**
 * 
 */
package railwayFrequentFunctions;

/**
 * @author Rahul Mukesh Singh
 *
 */

public class TableGenerator {
	public String MsgGeneratortable(String[] headers, String[][] data,
			String tableClass, boolean isPDFMail) {
		String msg = "";
		if (isPDFMail) {
			msg += "<table class='" + tableClass + "'><tr>";
			for (String head : headers) {
				msg += "<th>" + head + "</th>";

			}
			msg += "</tr>";
			int count = 0;
			for (String[] row : data) {
				if ((count % 2) == 0) {
					msg += "<tr class='even'>";
				} else {
					msg += "<tr class='odd'>";
				}
				
				for (String col : row) {
					msg += "<td>" + col + "</td>";
				}
				msg += "</tr>";
				count++;
			}
			msg += "</table>";
		} else {
			msg += "<table style='border-collapse: collapse;width: 100%;'><tr>";
			for (String head : headers) {
				msg += "<th style='text-align: left;padding:8px;background-color: #00788b;color: white;'>"
						+ head + "</th>";

			}
			msg += "</tr>";
			int count = 0;
			for (String[] row : data) {
				if ((count % 2) == 0) {
					msg += "<tr style='background-color:#f2f2f2;color:#404040'>";
				} else {
					msg += "<tr style='color:#404040'>";
				}
				for (String col : row) {

					msg += "<td style='text-align: left;padding: 8px;'>" + col
							+ "</td>";
				}
				msg += "</tr>";
				count++;
			}
			msg += "</table>";

		}

		return msg;
	}
}
