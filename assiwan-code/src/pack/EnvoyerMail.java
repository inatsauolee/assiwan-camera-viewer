package pack;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnvoyerMail {
	
	public EnvoyerMail(String recepteur, String name, String date, String heure) {
		send(recepteur, name, date, heure);
	}
	public void send(String recepteur, String name, String date, String heure){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "805");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"assiwan.camview@gmail.com",
								"159medomedo753MEDOMEDO@");
					}
				});

	     try {
	  	   // Create a default MimeMessage object.
	  	   Message message = new MimeMessage(session);
	  	
	  	   // Set From: header field of the header.
	  	   message.setFrom(new InternetAddress("assiwan.camview@gmail.com"));
	  	
	  	   // Set To: header field of the header.
	  	   message.setRecipients(Message.RecipientType.TO,
	                 InternetAddress.parse("muhammad.elwastani@gmail.com"));
	  	
	  	   // Set Subject: header field
	  	   message.setSubject("Détection");
	  	
	  	   // Now set the actual message
	  	   message.setText("<html> <h1>Une personne a été détectée !!</h1>date:");

	  	   // Send message
	  	   Transport.send(message);

	  	   System.out.println("Sent message successfully....");

	        } catch (MessagingException e) {
	           throw new RuntimeException(e);
	        }
	}
}