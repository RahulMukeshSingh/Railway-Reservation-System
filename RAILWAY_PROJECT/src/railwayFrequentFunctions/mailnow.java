/**
 * 
 */
package railwayFrequentFunctions;

/**
 * @author Rahul Mukesh Singh
 *
 */
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
public class mailnow {
public boolean MailNowMsg(String to,String msg,String subject) throws UnsupportedEncodingException
	{
	    boolean result;
		PersonalData ed = new PersonalData();
	    String from = ed.user;
	    String host = "smtp.gmail.com";
	    String port="587";
	    String FromName="RAILWAY";
	    final String user=ed.user;
	    final String pass=ed.pass;
        Properties properties = System.getProperties();
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
	   Session mailSession = Session.getInstance(properties,authenticator);

	   try {
	      // Create a default MimeMessage object.
	      MimeMessage message = new MimeMessage(mailSession);
	      
	      // Set From: header field of the header.
	      message.setFrom(new InternetAddress(from,FromName));
	      
	      // Set To: header field of the header.
	      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//	      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to1));
	      // Set Subject: header field
	      message.setSubject(subject);
	     
	      // Send the actual HTML message, as big as you like
	      message.setContent(msg, "text/html" );
	      
	      // Send message
	      Transport.send(message);
	      result = true;
	   } 
	   catch (MessagingException mex) {
	      result = false;
	      System.out.println("Non Attach : "+mex);
	   }
	   return result;
	}
	}
