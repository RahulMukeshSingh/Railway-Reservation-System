/**
 * 
 */
package railwayFrequentFunctions;

/**
 * @author Rahul Mukesh Singh
 *
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.activation.FileDataSource;
import javax.activation.DataSource;
import javax.activation.DataHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class emailattachmentrailway {
	public boolean createattachandemail(String to,  String subject,
			String attachfilename) {
		boolean result;
		PersonalData ed = new PersonalData();
		String from = ed.user;
		String host = "smtp.gmail.com";
		String port = "587";
		String FromName = "RAILWAY";
		String filename = "E:\\JSP\\RAILWAY_PROJECT\\WebContent\\WEB-INF\\GeneratedDocuments\\" + 
		to.replace(".", "")+ ".pdf";
		
		final String user = ed.user;
		final String pass = ed.pass;

		Properties properties = System.getProperties();
		// Setup mail server

		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		// creates a new session with an authenticator
		Authenticator authenticator = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		};
		// Get the default Session object.
		Session mailSession = Session.getInstance(properties, authenticator);

		try {
			
			MimeMessage message = new MimeMessage(mailSession);// Create-a-default-MimeMessage-object.
			message.setFrom(new InternetAddress(from, FromName));// Set-From:-header-field-of-the-header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));// Set-To:-header-field-of-the-header.

			message.setSubject(subject);// Set-Subject:-header-field
			BodyPart messageBodyPart = new MimeBodyPart();
			String bpart = "<center><h3 style='color:#00788b'>"
					+ "[Auto-Generated Mail with Attachment and PDF]</h3></center>";
			messageBodyPart.setContent(bpart, "text/html");
			Multipart multipart = new MimeMultipart();// Create-a-multipart-message
			multipart.addBodyPart(messageBodyPart);// Set-text-message-part
			messageBodyPart = new MimeBodyPart();// Part-two-is-attachment
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachfilename + ".pdf");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);// Send-the-complete-message-parts
			Transport.send(message);// Send-message
			result = true;
		} catch (MessagingException mex) {
			result = false;

		} catch (Exception e) {
			result = false;
			System.out.println("attach : "+e);

		}
		return result;
	}
}
