package rentasad.library.basicTools.emailSendTool;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import rentasad.library.basicTools.StringTool;
import rentasad.library.basicTools.emailSendTool.objects.EmailConfigObject;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * EmailSendTool provides functionality for sending emails, with or without attachments,
 * and supports sending exception notification emails.
 */
public class EmailSendTool
{
	/**
	 * Indicates whether debugging is enabled for the email sending tool.
	 * If true, additional debug information will be logged or printed to help in diagnosing issues.
	 * This can be useful during development or when troubleshooting email sending issues.
	 */
	@Setter @Getter
	private boolean debug = false;
	private final EmailConfigObject emailConfigObject;
	@Setter
	@Getter
	private int port = 25;

	@Setter
	@Getter
	public static int PORT = 25;

	/**
	 * Constructs a formal salutation string based on the provided title and last name.
	 *
	 * @param anrede   the title such as "Herr" or "Frau"
	 * @param nachname the last name
	 * @return a formatted salutation string based on the input title and last name, or a default greeting if the title is not recognized
	 */
	public static String getAnredeString(final String anrede, final String nachname)
	{
		String anredeString;
		if (anrede.equalsIgnoreCase("Herr"))
		{
			anredeString = String.format("Sehr geehrter Herr %s", nachname);
		}
		else if (anrede.equalsIgnoreCase("Frau"))
		{
			anredeString = String.format("Sehr geehrte Frau %s", nachname);
		}
		else
		{
			anredeString = "Sehr geehrte Damen und Herren,";
		}
		return anredeString;
	}



	/**
	 * Constructs an EmailSendTool with the provided EmailConfigObject.
	 *
	 * @param emailConfigObject an EmailConfigObject containing configuration settings for the email sending tool.
	 */
	public EmailSendTool(EmailConfigObject emailConfigObject)
	{
		super();
		this.emailConfigObject = emailConfigObject;
		if (this.emailConfigObject.getPort() != null)
		{
			this.port = this.emailConfigObject.getPort();
		}
	}

	/**
	 * Sends an email with an attachment.
	 *
	 * @param to             the recipient's email address
	 * @param from           the sender's email address
	 * @param subject        the subject of the email
	 * @param messageText    the text content of the email
	 * @param attachmentFile the file to be attached to the email
	 * @return a status message indicating the result of the email sending process
	 * @throws FileNotFoundException if the attachment file does not exist
	 * @throws EmailException        if there is an error in sending the email
	 */
	public String sendMailWithAttachment(final String to, final String from, final String subject, final String messageText, final File attachmentFile) throws FileNotFoundException, EmailException
	{
		if (attachmentFile.exists())
		{
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(attachmentFile.getAbsolutePath());
			attachment.setDisposition(EmailAttachment.ATTACHMENT);

			return sendMailWithAttachment(to, null, from, subject, messageText, attachment);
		}
		else
		{
			throw new FileNotFoundException(attachmentFile.getAbsolutePath());
		}
	}

	/**
	 * Sends an email with an attachment.
	 *
	 * @param toMultiple  a comma-separated list of recipient email addresses
	 * @param bcc         an email address to send a blind carbon copy (BCC) of the email, can be null
	 * @param from        the sender's email address
	 * @param subject     the subject of the email
	 * @param messageText the body text of the email
	 * @param attachment  the email attachment to include
	 * @return a status message indicating the result of the email sending process
	 * @throws EmailException if there is an error in sending the email
	 */
	public String sendMailWithAttachment(final String toMultiple, final String bcc, final String from, final String subject, final String messageText, final EmailAttachment attachment)
			throws EmailException
	{
		ArrayList<EmailAttachment> attachmentsList = new ArrayList<EmailAttachment>();
		attachmentsList.add(attachment);
		return sendMailWithAttachment(toMultiple, bcc, from, subject, messageText, attachmentsList);

	}

	/**
	 * Sends an email with multiple attachments.
	 *
	 * @param toMultiple     a comma-separated list of recipient email addresses
	 * @param bcc            an email address to send a blind carbon copy (BCC) of the email, can be null
	 * @param from           the sender's email address
	 * @param subject        the subject of the email
	 * @param messageText    the text content of the email
	 * @param attachmentList a list of email attachments to include
	 * @return a status message indicating the result of the email sending process
	 * @throws EmailException if there is an error in sending the email
	 */
	public String sendMailWithAttachment(final String toMultiple, final String bcc, final String from, final String subject, final String messageText, final ArrayList<EmailAttachment> attachmentList)
			throws EmailException
	{

		MultiPartEmail email = new MultiPartEmail();
		email.setSmtpPort(port);
		email.setHostName(emailConfigObject.getMailserver());

		String[] toArray = StringTool.splittStringWithCommas(toMultiple, ",");
		for (String to : toArray)
		{
			email.addTo(to);
		}

		if (bcc != null)
		{
			email.addBcc(bcc);
		}
		email.setFrom(from);
		email.setSubject(subject);
		email.setMsg(messageText);
		for (EmailAttachment attachment : attachmentList)
		{
			email.attach(attachment);
		}
		return email.send();
	}

	/**
	 * Sends an email notification containing details about an exception that occurred.
	 *
	 * @param to                 the recipient's email address
	 * @param from               the sender's email address
	 * @param subject            the subject of the email
	 * @param messageTextPraefix a prefix for the message text body
	 * @param e                  the exception whose details are to be included in the email
	 * @throws FileNotFoundException if an associated file required for sending the email is not found
	 * @throws EmailException        if there is an error in sending the email
	 */
	public void sendExceptionNotifyEmail(String to, String from, String subject, String messageTextPraefix, Throwable e) throws FileNotFoundException, EmailException
	{
		/*
		 * ExceptionMailbody aufbereiten
		 */
		String messageText = "Fehlerbericht Exception:";
		messageText = messageText.concat("\n");
		String exMessage = ExceptionUtils.getStackTrace(e);
		for (StackTraceElement stack : e.getStackTrace())
		{
			messageText = messageText.concat("\n Stack-Meldung:");
			messageText = messageText.concat(
					String.format("Klasse: %s \nFile: %s \nZeile: %s \nMethode: %s ", stack.getClassName(), stack.getFileName(), stack.getLineNumber(), stack.getMethodName()));

		}
		messageText = messageText.concat("\n\n Stackmeldung Message: \n" + exMessage);

		sendMail(to, from, subject, messageText);
	}

	/**
	 * Sends an email notification containing details about an exception that occurred.
	 * Uses the email configuration settings to determine the sender and recipient.
	 *
	 * @param subject            the subject of the email
	 * @param messageTextPraefix a prefix for the message text body
	 * @param e                  the exception whose details are to be included in the email
	 * @throws FileNotFoundException if an associated file required for sending the email is not found
	 * @throws EmailException        if there is an error in sending the email
	 */
	public void sendExceptionNotifyEmail(String subject, String messageTextPraefix, Throwable e) throws FileNotFoundException, EmailException
	{
		String to = this.emailConfigObject.getTo();
		String from = this.emailConfigObject.getFrom();
		sendExceptionNotifyEmail(to, from, subject, messageTextPraefix, e);
	}

	/**
	 * Sends an email to multiple recipients.
	 *
	 * @param toMultiple  a comma-separated list of recipient email addresses
	 * @param from        the sender's email address
	 * @param subject     the subject of the email
	 * @param messageText the text content of the email
	 * @return a status message indicating the result of the email sending process
	 * @throws EmailException        if there is an error in sending the email
	 */
	public String sendMail(String toMultiple, String from, String subject, String messageText) throws EmailException
	{
		StringBuilder sysoutString = new StringBuilder("Mail versendet an ");
		MultiPartEmail email = new MultiPartEmail();

		email.setSmtpPort(port);
		String[] toArray = StringTool.splittStringWithCommas(toMultiple, ",");
		for (String to : toArray)
		{
			email.addTo(to);
			sysoutString.append(to)
						.append(" ");
		}
		email.setHostName(emailConfigObject.getMailserver());
		email.setFrom(from);
		email.setSubject(subject);
		email.setMsg(messageText);
		if (debug)
		{
			System.out.println(sysoutString);
		}
		return email.send();
	}

	/**
	 * Sends an email to multiple recipients with optional BCC and specified sender, subject, and message text.
	 *
	 * @param toMultiple  a comma-separated list of recipient email addresses
	 * @param bcc         a BCC (Blind Carbon Copy) recipient email address, can be null
	 * @param from        the sender's email address
	 * @param subject     the subject of the email
	 * @param messageText the body text of the email
	 * @return a status message indicating the result of the email sending process
	 * @throws FileNotFoundException if there is an error in finding a necessary file
	 * @throws EmailException        if there is an error in sending the email
	 */
	public String sendMail(String toMultiple, String bcc, String from, String subject, String messageText) throws FileNotFoundException, EmailException
	{
		MultiPartEmail email = new MultiPartEmail();
		email.setSmtpPort(port);
		email.setHostName(emailConfigObject.getMailserver());
		String[] toArray = StringTool.splittStringWithCommas(toMultiple, ",");
		for (String to : toArray)
		{
			email.addTo(to);
		}
		email.addBcc(bcc);
		email.setFrom(from);
		email.setSubject(subject);
		email.setMsg(messageText);
		return email.send();
	}

	/**
	 * Sends an email using the provided SMTP server and credentials.
	 *
	 * @param mailserver the SMTP server to connect to for sending the email
	 * @param username   the username for the SMTP server, can be null if not required
	 * @param password   the password for the SMTP server, can be null if not required
	 * @param absender   the sender's email address
	 * @param toMultiple a comma-separated list of recipient email addresses
	 * @param betreff    the subject of the email
	 * @param text       the text content of the email
	 * @return a status message indicating the result of the email sending process
	 * @throws IOException    if there is a network-related error
	 * @throws EmailException if there is an error in sending the email
	 */
	public static String sendeEmail(String mailserver, String username, String password, String absender, String toMultiple, String betreff, String text) throws IOException, EmailException
	{

		MultiPartEmail email = new MultiPartEmail();
		email.setSmtpPort(PORT);
		if (username != null && password != null)
		{
			DefaultAuthenticator defaultAuthenticator = new DefaultAuthenticator(username, password);
			email.setAuthenticator(defaultAuthenticator);
			email.setSSLOnConnect(false);
		}
		String[] toArray = StringTool.splittStringWithCommas(toMultiple, ",");
		for (String to : toArray)
		{
			email.addTo(to);
		}
		email.setHostName(mailserver);
		email.setFrom(absender);
		email.setCharset("UTF-8");
		email.setSubject(betreff);
		email.setMsg(text);
		// email.attach(new ByteArrayDataSource(anhangInputStream, anhangContentType),
		// anhangDateiName, anhangBeschreibung, EmailAttachment.ATTACHMENT);
		return email.send();
	}

	/**
	 * Checks if the provided email address is valid.
	 *
	 * @param emailAddress the email address to be validated
	 * @return true if the email address is valid, false otherwise
	 * @throws AddressException if there is an error in the email address format
	 */
	public static boolean isValidEmailAddress(final String emailAddress) throws AddressException
	{
		if (emailAddress == null)
		{
			return false;
		}
		return validateEmailAddress(emailAddress);
	}

	/**
	 * Validates the provided email address.
	 *
	 * @param emailAddress the email address to be validated
	 * @return true if the email address is valid, false otherwise
	 * @throws AddressException if there is an error in the email address format
	 */
	private static boolean validateEmailAddress(final String emailAddress) throws AddressException
	{
		try
		{
			InternetAddress emailAddr = new InternetAddress(emailAddress);
			emailAddr.validate();
			return true;
		} catch (AddressException e)
		{
			return false;
		}
	}

	/**
	 * Factory method to create an instance of MultiPartEmail.
	 * This method can be mocked during tests to avoid sending real emails.
	 *
	 * @return a new instance of MultiPartEmail
	 */
	protected MultiPartEmail createMultiPartEmailInstance() {
		return new MultiPartEmail();
	}
}
