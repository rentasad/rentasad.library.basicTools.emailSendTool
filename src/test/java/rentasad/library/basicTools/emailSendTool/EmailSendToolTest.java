package rentasad.library.basicTools.emailSendTool;

import org.junit.jupiter.api.Test;

import rentasad.library.basicTools.emailSendTool.objects.EmailConfigObject;
import rentasad.library.basicTools.emailSendTool.objects.EmailConfigParameterEnum;

public class EmailSendToolTest
{

    @Test
    public void testSendMailViaSophos() throws Exception
    {
        EmailConfigObject emailConfigObject = new EmailConfigObject("192.168.111.253", null, null);
        emailConfigObject.setValue("25", EmailConfigParameterEnum.port);
        emailConfigObject.setValue("it@gustini.de", EmailConfigParameterEnum.from);
        emailConfigObject.setValue("matthias.staud@gustini.de", EmailConfigParameterEnum.to);

        EmailSendTool emailSendTool = new EmailSendTool(emailConfigObject);
        emailSendTool.sendMail("matthias.staud@gustini.de", "it@gustini.de", "TEST gstvmex", "TEST1 INHALT");
//        EmailSendTool.sendeEmail("192.168.111.253",null, null, "it@gustini.de", "matthias.staud@gustini.de,matthias@familie-staud.de", "TEST1-SUBJECT", "TEST1 INHALT");
    }
    
//    @Test
    public void testSendMailStringViaGstbs() throws Exception
    {
        EmailConfigObject emailConfigObject = new EmailConfigObject("gstbs", null, null);
        emailConfigObject.setValue("25", EmailConfigParameterEnum.port);
        emailConfigObject.setValue("it@gustini.de", EmailConfigParameterEnum.from);
        emailConfigObject.setValue("matthias.staud@gustini.de", EmailConfigParameterEnum.to);

        EmailSendTool emailSendTool = new EmailSendTool(emailConfigObject);
        emailSendTool.sendMail("matthias.staud@gustini.de", "it@gustini.de", "TEST gstbs", "TEST1 INHALT");
//        EmailSendTool.sendeEmail("192.168.111.253",null, null, "it@gustini.de", "matthias.staud@gustini.de,matthias@familie-staud.de", "TEST1-SUBJECT", "TEST1 INHALT");
    }
    @Test
    public void testSendMailStringViaGSTVMTASK() throws Exception
    {
        EmailConfigObject emailConfigObject = new EmailConfigObject("gstvmtask", null, null);
        emailConfigObject.setValue("25", EmailConfigParameterEnum.port);
        emailConfigObject.setValue("it@gustini.de", EmailConfigParameterEnum.from);
        emailConfigObject.setValue("matthias.staud@gustini.de", EmailConfigParameterEnum.to);

        EmailSendTool emailSendTool = new EmailSendTool(emailConfigObject);
        emailSendTool.sendMail("matthias.staud@gustini.de", "it@gustini.de", "TEST gstvmtask", "TEST1 INHALT");
//        EmailSendTool.sendeEmail("192.168.111.253",null, null, "it@gustini.de", "matthias.staud@gustini.de,matthias@familie-staud.de", "TEST1-SUBJECT", "TEST1 INHALT");
    }
}
