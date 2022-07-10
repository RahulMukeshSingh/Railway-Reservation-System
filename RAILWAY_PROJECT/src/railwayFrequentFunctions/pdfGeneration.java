/**
 * 
 */
package railwayFrequentFunctions;

/**
 * @author Rahul Mukesh Singh
 *
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class pdfGeneration {
	public pdfGeneration(String filename, String msg,String seatMapLocation, String qrCodeData,
			String qrLocation,String csss,int keyShift) throws IOException {
		if (new File(filename).exists()) {
			new File(filename).delete();
		}
		if(new File(qrLocation).exists()){
			new File(qrLocation).delete();
		}
		OutputStream file = null;
		Document document = null;
		String imgLocation="E:\\JSP\\RAILWAY_PROJECT\\WebContent\\images\\logo.png";
		try {
			File fileloc = null;

			fileloc = new File(filename);
			

			file = new FileOutputStream(fileloc);
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);
			document.open();
			
			// ----------------------------------ADD_LOGO_IMAGE------------------------------------------
			Image img = Image.getInstance(imgLocation);
			
			img.scaleAbsolute(100, 90);
			img.setAbsolutePosition(document.right()-60, document.top()-45);
			document.add(img);
			// ----------------------------------ADD_DATA_TABLE------------------------------------------
			InputStream is = new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8));
			InputStream cs = new ByteArrayInputStream(csss.getBytes());
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is, cs,Charset.forName("UTF-8"));
			// ----------------------------------ADD_SEAT_MAP---------------------------------------------
			img = Image.getInstance(seatMapLocation);
			img.setAlignment(Image.ALIGN_CENTER);
			img.scaleAbsolute(300, 400);
			document.add(img);
			// ----------------------------------ADD_QR_CODE---------------------------------------------

			
			String charset = "UTF-8"; // or "ISO-8859-1"
			Map hintMap = new HashMap();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			(new qrGeneration()).createQRCode(qrCodeData, qrLocation, charset,
					hintMap, 100, 100,keyShift);
			img = Image.getInstance(qrLocation);
			img.setAlignment(Image.ALIGN_CENTER);
			document.add(img);
			//-----------------------------------------------------------------------------------------

		} catch (Exception e) {

		} finally {
			document.close();
			file.close();
			if(new File(qrLocation).exists()){
				new File(qrLocation).delete();
			}
		}
	}
}
