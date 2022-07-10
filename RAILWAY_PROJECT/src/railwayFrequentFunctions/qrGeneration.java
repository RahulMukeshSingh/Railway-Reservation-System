/**
 * 
 */
package railwayFrequentFunctions;

/**
 * @author Rahul Mukesh Singh
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
public class qrGeneration {

		public static void createQRCode(String qrCodeData, String filePath,
			String charset, Map hintMap, int qrCodeheight, int qrCodewidth,int keyShift)
			throws WriterException, IOException {
			qrCodeData=(new SecretKey()).getSecretKey(qrCodeData, keyShift);
		BitMatrix matrix = new MultiFormatWriter().encode(
				new String(qrCodeData.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
		
	MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
				.lastIndexOf('.') + 1), new File(filePath));	
	}

}