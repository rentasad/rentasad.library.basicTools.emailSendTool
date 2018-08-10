package org.gustini.library.basicTools.emailSendTool;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.gustini.library.basicTools.emailSendTool.objects.EmailConfigObject;
import org.gustini.library.basicTools.emailSendTool.objects.EmailConfigParameterEnum;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EmailConfigObjectTest
{

	@BeforeAll
	public void setUp() throws Exception
	{
	}

	@AfterAll
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testWriteConfiguration() throws Exception
	{
		
		EmailConfigObject configObject = new EmailConfigObject();
		configObject.setValue("192.168.111.253", EmailConfigParameterEnum.mailserver);
		configObject.setValue("it@gustini.de", EmailConfigParameterEnum.from);
		configObject.setValue("matthias.staud@gustini.de", EmailConfigParameterEnum.to);
		
		
		assertTrue(EmailConfigObject.writeConfiguration(configObject, "mailSettingsTest.ini", "TEST"));
	}

	@Test
	public void testReadConfiguration() throws Exception
	{
		File file = new File("mailSettingsTest.ini");
		EmailConfigObject configObject = EmailConfigObject.readConfiguration(file, "TEST");
		assertEquals("192.168.111.253", configObject.getMailserver());
	}
	
	

}
