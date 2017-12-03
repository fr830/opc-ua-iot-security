package org.dfki.iot.attack.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

	private static String username, password = null;

	private static final Logger myLogger = LoggerFactory.getLogger(EmailUtil.class);

	public static void main(String[] args) {

		readPropertieFile("config.properties");

		EmailUtil emailUtil = new EmailUtil();
		emailUtil.sendEmail("Testing Subject", "herleraja@gmail.com", "herleraja@gmail.com",
				"Dear Mail Crawler," + "\n\n No spam to my email, please!");
		emailUtil.sendEmailWithAttachment("Testing Subject", "herleraja@gmail.com", "herleraja@gmail.com",
				"Dear Mail Crawler," + "\n\n No spam to my email, please!", "./src/main/resources/Data.xls",
				"Data.xls");

	}

	private synchronized void sendEmail(String subject, String toAddress, String fromAddress, String text) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);

			myLogger.info("e-mail sent to -->  " + toAddress);

		} catch (MessagingException e) {
			myLogger.info(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
		}

	}

	private synchronized void sendEmailWithAttachment(String subject, String toAddress, String fromAddress, String text,
			String filePath, String fileName) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
			message.setSubject(subject);

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("This is message body");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(filePath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Transport.send(message);

			myLogger.info("e-mail sent to -->  " + toAddress);

		} catch (MessagingException e) {
			myLogger.info(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
		}

	}

	private static void readPropertieFile(String filename) {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = EmailUtil.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				myLogger.info("Sorry, unable to find " + filename);
				return;
			}

			prop.load(input);

			username = prop.getProperty("email.user");
			password = prop.getProperty("email.pwd");

			myLogger.info("Property file read successful");

		} catch (IOException ex) {
			myLogger.info(
					"\n LocalizedMessage : " + ex.getLocalizedMessage() + "\n  		 Message :: " + ex.getMessage()
							+ "\n toString :: " + ex.toString() + "\n:		 StackTrace :: " + ex.getStackTrace());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					myLogger.info("\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: "
							+ e.getMessage() + "\n toString :: " + e.toString() + "\n:		 StackTrace :: "
							+ e.getStackTrace());
				}
			}
		}

	}

}
