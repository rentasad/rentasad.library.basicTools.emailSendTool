package rentasad.library.basicTools.emailSendTool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rentasad.library.basicTools.emailSendTool.objects.EmailConfigObject;
import rentasad.library.basicTools.emailSendTool.objects.EmailConfigParameterEnum;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rentasad.library.configFileTool.ConfigFileTool;
import rentasad.library.configFileTool.ConfigFileToolException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmailConfigObjectTest
{
	final String MAIL_SETTINGS_FILE_PATH_WRITE = "resources/test/mailSettingsTest.ini";
	final String MAIL_SETTINGS_FILE_PATH_READ = "resources/test/mailSettingsReadTest.ini";
	final String MAIL_SETTINGS_HOSTNAME = "mail.server.de";
	final String MAIL_SETTINGS_FROM = "From@Adress.de";
	final String MAIL_SETTINGS_TO = "to@mail.de";

	final String CONFIG_MAIL_SECTION = "MAILCONFIG";

	EmailConfigObject emailConfigObject = new EmailConfigObject();

	@BeforeEach
	public void setUp() throws Exception
	{
		//		emailConfigObject.setValue(MAIL_SETTINGS_HOSTNAME, EmailConfigParameterEnum.mailserver);
		//		emailConfigObject.setValue(MAIL_SETTINGS_FROM, EmailConfigParameterEnum.from);
		//		emailConfigObject.setValue(MAIL_SETTINGS_TO, EmailConfigParameterEnum.to);
	}

	@AfterEach
	public void tearDown() throws Exception
	{
		File mailsettingsFile = new File(MAIL_SETTINGS_FILE_PATH_WRITE);
		if (mailsettingsFile.exists())
		{
			mailsettingsFile.delete();
		}

	}

	@Test
	public void testWriteConfiguration() throws Exception
	{
		File file = new File(MAIL_SETTINGS_FILE_PATH_WRITE);
		assertTrue(EmailConfigObject.writeConfiguration(emailConfigObject, MAIL_SETTINGS_FILE_PATH_WRITE, CONFIG_MAIL_SECTION));
		assertTrue(file.exists());

	}

	@Test
	public void testReadConfiguration() throws Exception
	{
		File mailsettingsFile = new File(MAIL_SETTINGS_FILE_PATH_READ);

		EmailConfigObject configObject = EmailConfigObject.readConfiguration(mailsettingsFile, CONFIG_MAIL_SECTION);
		assertEquals(MAIL_SETTINGS_HOSTNAME, configObject.getMailserver());
		assertEquals(MAIL_SETTINGS_FROM, configObject.getFrom());
		assertEquals(MAIL_SETTINGS_TO, configObject.getTo());

	}

	@Test
	public void testConstructorWithMailServerUsernamePassword()
	{
		emailConfigObject = new EmailConfigObject("smtp.example.com", "user", "pass");
		assertEquals("smtp.example.com", emailConfigObject.getMailserver());
		assertEquals("user", emailConfigObject.getUsername());
		assertEquals("pass", emailConfigObject.getPassword());
	}

	@Test
	public void testConstructorWithMailServerOnly()
	{
		emailConfigObject = new EmailConfigObject("smtp.example.com");
		assertEquals("smtp.example.com", emailConfigObject.getMailserver());
		assertNull(emailConfigObject.getUsername());
		assertNull(emailConfigObject.getPassword());
	}

	@Test
	public void testDefaultConstructor()
	{
		assertNull(emailConfigObject.getMailserver());
		assertNull(emailConfigObject.getUsername());
		assertNull(emailConfigObject.getPassword());
	}

	@Test
	public void testSetValue()
	{
		emailConfigObject.setValue("smtp.example.com", EmailConfigParameterEnum.mailserver);
		assertEquals("smtp.example.com", emailConfigObject.getMailserver());

		emailConfigObject.setValue("user", EmailConfigParameterEnum.username);
		assertEquals("user", emailConfigObject.getUsername());

		emailConfigObject.setValue("pass", EmailConfigParameterEnum.password);
		assertEquals("pass", emailConfigObject.getPassword());

		emailConfigObject.setValue("25", EmailConfigParameterEnum.port);
		assertEquals(Integer.valueOf(25), emailConfigObject.getPort());
	}

	@Test
	public void testGetValue()
	{
		emailConfigObject.setMailserver("smtp.example.com");
		assertEquals("smtp.example.com", emailConfigObject.getValue(EmailConfigParameterEnum.mailserver));

		emailConfigObject.setUsername("user");
		assertEquals("user", emailConfigObject.getValue(EmailConfigParameterEnum.username));

		emailConfigObject.setPassword("pass");
		assertEquals("pass", emailConfigObject.getValue(EmailConfigParameterEnum.password));
	}

	@Test
	public void testReadConfigurationFromFile() throws IOException, ConfigFileToolException
	{
		emailConfigObject.setValue(MAIL_SETTINGS_HOSTNAME, EmailConfigParameterEnum.mailserver);
		emailConfigObject.setValue(MAIL_SETTINGS_FROM, EmailConfigParameterEnum.from);
		emailConfigObject.setValue(MAIL_SETTINGS_TO, EmailConfigParameterEnum.to);
		File configFile = new File("src/test/resources/email_config.ini");
		String sectionName = "MAILCONFIG";
		EmailConfigObject config = EmailConfigObject.readConfiguration		(configFile, sectionName);
		assertNotNull(config);
		assertEquals("mail.server.de", config.getMailserver());
		assertEquals("user", config.getUsername());
		assertEquals("pass", config.getPassword());
	}

	@Test
	public void testReadConfigurationFromMap()
	{
		Map<String, String> configMap = new HashMap<>();
		configMap.put("mailserver", "smtp.example.com");
		configMap.put("username", "user");
		configMap.put("password", "pass");
		EmailConfigObject config = EmailConfigObject.readConfiguration(configMap);
		assertEquals("smtp.example.com", config.getMailserver());
		assertEquals("user", config.getUsername());
		assertEquals("pass", config.getPassword());
	}

	@Test
	public void testWriteConfiguration2() throws IOException
	{
				emailConfigObject.setValue(MAIL_SETTINGS_HOSTNAME, EmailConfigParameterEnum.mailserver);
		emailConfigObject.setValue(MAIL_SETTINGS_FROM, EmailConfigParameterEnum.from);
		emailConfigObject.setValue(MAIL_SETTINGS_TO, EmailConfigParameterEnum.to);
		EmailConfigObject config = new EmailConfigObject("smtp.example.com", "user", "pass");
		String fileName = "src/test/resources/email_config_output.ini";
		String sectionName = "EmailSettings";
		boolean success = EmailConfigObject.writeConfiguration(config, fileName, sectionName);
		assertTrue(success);
		File outputFile = new File(fileName);
		assertTrue(outputFile.exists());
		outputFile.delete(); // Clean up after test
	}

	@Test
	public void testSetValueWithInvalidEnum()
	{
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			emailConfigObject.setValue("value", EmailConfigParameterEnum.valueOf("invalid"));
		});
		assertNotNull(thrown.getMessage());
	}

	@Test
	public void testGetValueWithInvalidEnum()
	{
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			emailConfigObject.getValue(EmailConfigParameterEnum.valueOf("invalid"));
		});
		assertNotNull(thrown.getMessage());
	}

}
