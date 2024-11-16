package rentasad.library.basicTools.emailSendTool.objects;

import lombok.Data;
import rentasad.library.configFileTool.ConfigFileTool;
import rentasad.library.configFileTool.ConfigFileToolException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The EmailConfigObject class is used to store and manage email configuration details such as mail server, username, password, and other related properties.
 */
@Data
public class EmailConfigObject

{
	private String mailserver;
	private String username;
	private String password;
	private String from;
	private String to;
	private String subject;
	private String text;
	private Integer port;

	/**
	 * Constructs an EmailConfigObject with the specified parameters.
	 *
	 * @param mailserver the mail server address
	 * @param username   the username for the mail server
	 * @param password   the password for the mail server
	 */
	public EmailConfigObject(String mailserver, String username, String password)
	{
		super();
		this.mailserver = mailserver;
		this.username = username;
		this.password = password;
	}

	/**
	 * Constructs an EmailConfigObject with the specified mail server address.
	 *
	 * @param mailserver the mail server address
	 */
	public EmailConfigObject(
			String mailserver)
	{
		super();
		this.mailserver = mailserver;
		this.username = null;
		this.password = null;
	}

	/**
	 *
	 */
	public EmailConfigObject()
	{
		super();
	}

	/**
	 * Reads the configuration from the specified file and section, and returns an EmailConfigObject.
	 *
	 * @param fileName    the configuration file to be read
	 * @param sectionName the section within the configuration file that contains the email settings
	 * @return an EmailConfigObject populated with the configuration values from the specified file and section
	 * @throws IOException             if an I/O error occurs when reading the configuration file
	 * @throws ConfigFileToolException if an error occurs while parsing the configuration file
	 */
	public static EmailConfigObject readConfiguration(File fileName, String sectionName) throws IOException, ConfigFileToolException
	{
		EmailConfigObject emailConfigObject = new EmailConfigObject();
		Map<String, String> configMap = ConfigFileTool.readConfiguration(fileName.getAbsolutePath(), sectionName);
		for (EmailConfigParameterEnum enumItem : EmailConfigParameterEnum.values())
		{

			if (configMap.containsKey(enumItem.name()))
			{
				emailConfigObject.setValue(configMap.get(enumItem.name()), enumItem);
			}
		}
		return emailConfigObject;
	}

	/**
	 * Reads the configuration from the specified resources file and section, and returns an EmailConfigObject.
	 *
	 * @param fileNameString the name of the resources file to be read
	 * @param sectionName    the section within the resources file that contains the email settings
	 * @return an EmailConfigObject populated with the configuration values from the specified file and section
	 * @throws IOException             if an I/O error occurs when reading the resources file
	 * @throws ConfigFileToolException if an error occurs while parsing the resources file
	 */
	public static EmailConfigObject readConfigurationFromResources(final String fileNameString, final String sectionName) throws IOException, ConfigFileToolException
	{
		EmailConfigObject emailConfigObject = new EmailConfigObject();
		Map<String, String> configMap = ConfigFileTool.readConfigurationFromResources(fileNameString, sectionName);
		for (EmailConfigParameterEnum enumItem : EmailConfigParameterEnum.values())
		{

			if (configMap.containsKey(enumItem.name()))
			{
				emailConfigObject.setValue(configMap.get(enumItem.name()), enumItem);
			}
		}
		return emailConfigObject;
	}

	/**
	 * Reads the email configuration from a provided configuration map.
	 *
	 * @param configMap a map containing configuration parameters with keys corresponding to
	 *                  the enumerations in EmailConfigParameterEnum and their respective values.
	 * @return an EmailConfigObject populated with the configuration values from the provided map.
	 */
	public static EmailConfigObject readConfiguration(final Map<String, String> configMap)
	{
		EmailConfigObject emailConfigObject = new EmailConfigObject();
		for (EmailConfigParameterEnum enumItem : EmailConfigParameterEnum.values())
		{

			if (configMap.containsKey(enumItem.name()))
			{
				emailConfigObject.setValue(configMap.get(enumItem.name()), enumItem);
			}
		}
		return emailConfigObject;
	}

	/**
	 * Writes the provided email configuration to a specified file and section.
	 *
	 * @param emailConfigObject the email configuration object containing settings to be written
	 * @param fileName the name of the file to which the configuration is to be written
	 * @param sectionName the section within the file under which the configuration should be written
	 * @return true if the configuration file exists after writing, false otherwise
	 * @throws IOException if an I/O error occurs during the writing process
	 */
	public static boolean writeConfiguration(EmailConfigObject emailConfigObject, String fileName, String sectionName) throws IOException
	{
		Map<String, String> configMap = new HashMap<>();
		for (EmailConfigParameterEnum enumItem : EmailConfigParameterEnum.values())
		{
			if (emailConfigObject.getValue(enumItem) != null)
			{
				String value = emailConfigObject.getValue(enumItem);
				String key = enumItem.name();
				configMap.put(key, value);
			}
		}
		ConfigFileTool.writeConfiguration(fileName, sectionName, configMap);
		return (new File(fileName).exists());
	}

	/*
	 * ************************************************************
	 * GETTER / SETTER
	 * ***********************************************************
	 */

	/**
	 * Sets the value of an email configuration property based on the provided enum item.
	 *
	 * @param propertyValue the value to be set for the specified property
	 * @param enumItem      the enum item representing the email configuration property to be set
	 * @throws IllegalArgumentException if the provided enum item is unknown
	 */
	public void setValue(String propertyValue, EmailConfigParameterEnum enumItem)
	{
		switch (enumItem)
		{
		case from:
			this.from = propertyValue;
			break;
		case mailserver:
			this.mailserver = propertyValue;
			break;
		case password:
			this.password = propertyValue;
			break;
		case to:
			this.to = propertyValue;
			break;
		case username:
			this.username = propertyValue;
			break;
		case port:
			this.port = Integer.valueOf(propertyValue);
			break;
		default:
			throw new IllegalArgumentException("Unbekanntes Enum: " + enumItem.name());
		}
	}

	/**
	 * Retrieves the value of the specified email configuration parameter.
	 *
	 * @param enumItem the enum item representing the email configuration parameter to be retrieved
	 * @return the value of the specified email configuration parameter, or null if the parameter is not set or the port is null
	 * @throws IllegalArgumentException if the provided enum item is unknown
	 */
	public String getValue(EmailConfigParameterEnum enumItem)
	{
		switch (enumItem)
		{
		case from:
			return this.from;
		case mailserver:
			return this.mailserver;
		case password:
			return this.password;
		case to:
			return this.to;
		case username:
			return this.username;
		case port:
			if (this.port != null)
			{
				return this.port.toString();
			}
			else
			{
				return null;
			}
		default:
			throw new IllegalArgumentException(enumItem.name());
		}
	}

}
