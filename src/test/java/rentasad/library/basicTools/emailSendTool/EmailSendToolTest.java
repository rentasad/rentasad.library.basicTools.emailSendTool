package rentasad.library.basicTools.emailSendTool;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rentasad.library.basicTools.emailSendTool.objects.EmailConfigObject;

import javax.mail.internet.AddressException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class EmailSendToolTest
{
	private EmailConfigObject emailConfigObject;
	private EmailSendTool emailSendTool;

	@BeforeEach
	public void setUp() {
		emailConfigObject = new EmailConfigObject("smtp.example.com", "user", "password");
		emailSendTool = new EmailSendTool(emailConfigObject);
	}

	@Test
	public void testGetAnredeString() {
		String resultHerr = EmailSendTool.getAnredeString("Herr", "Müller");
		assertEquals("Sehr geehrter Herr Müller", resultHerr);

		String resultFrau = EmailSendTool.getAnredeString("Frau", "Schmidt");
		assertEquals("Sehr geehrte Frau Schmidt", resultFrau);

		String resultUnknown = EmailSendTool.getAnredeString("Dr.", "Meier");
		assertEquals("Sehr geehrte Damen und Herren,", resultUnknown);
	}

//	@Test
//	public void testSendMailWithAttachment_FileExists() throws FileNotFoundException, EmailException {
//		File attachmentFile = mock(File.class);
//		when(attachmentFile.exists()).thenReturn(true);
//		when(attachmentFile.getAbsolutePath()).thenReturn("src/test/resources/attachment.txt");
//
//		// Mock MultiPartEmail to avoid sending real email
//		MultiPartEmail emailMock = mock(MultiPartEmail.class);
//		doReturn("mockMessageId").when(emailMock).send();
//		EmailSendTool emailSendToolSpy = Mockito.spy(emailSendTool);
//		doReturn(emailMock).when(emailSendToolSpy).createMultiPartEmailInstance();
//
//		String result = emailSendToolSpy.sendMailWithAttachment("recipient@example.com", "sender@example.com", "Test Subject", "Test Message", attachmentFile);
//		assertNotNull(result);
//	}

	@Test
	public void testSendMailWithAttachment_FileNotFound() {
		File attachmentFile = mock(File.class);
		when(attachmentFile.exists()).thenReturn(false);
		when(attachmentFile.getAbsolutePath()).thenReturn("src/test/resources/attachment.txt");

		assertThrows(FileNotFoundException.class, () -> {
			emailSendTool.sendMailWithAttachment("recipient@example.com", "sender@example.com", "Test Subject", "Test Message", attachmentFile);
		});
	}





	@Test
	public void testIsValidEmailAddress_Valid() throws AddressException
	{
		assertTrue(EmailSendTool.isValidEmailAddress("test@example.com"));
	}

	@Test
	public void testIsValidEmailAddress_Invalid() throws AddressException
	{
		assertFalse(EmailSendTool.isValidEmailAddress("invalid-email"));
	}}
