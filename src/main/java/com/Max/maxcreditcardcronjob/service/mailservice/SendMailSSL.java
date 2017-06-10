package com.Max.maxcreditcardcronjob.service.mailservice;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * This class is used for sending emails to the creditcard applicants about the
 * status of the application.
 * 
 * @author Bootcamp User 021
 *
 */
class Mailer {

	/**
	 * This method contains the detials about the mail server and sender email
	 * and password.
	 * 
	 * @param from
	 * @param password
	 * @param to
	 * @param sub
	 * @param msg
	 */
	public static void send(String from, String password, String to, String sub, String msg) {

		final Logger logger = Logger.getLogger(Mailer.class);

		// Get properties object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		// get Session
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("crrcreditcard@gmail.com", "WelcomeToJava");
			}
		});
		// compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(sub);
			message.setText(msg);
			// send message
			Transport.send(message);
			System.out.println("message sent successfully");
		} catch (MessagingException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

	}
}

/**
 * This method is used to send email to the customer.
 * 
 * @author Bootcamp User 021
 *
 */
public class SendMailSSL {
	public void sendMail(String mailid, String subject, String message) {
		// from,password,to,subject,message
		Mailer.send(" crrcreditcard@gmail.com", "WelcomeToJava", mailid, subject, message);
		// change from, password and to
	}
}