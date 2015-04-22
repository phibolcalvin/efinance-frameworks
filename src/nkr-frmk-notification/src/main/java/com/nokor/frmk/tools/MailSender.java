package com.nokor.frmk.tools;


import org.apache.commons.mail.SimpleEmail;

/**
 * @author youhort.ly
 */
public class MailSender {
	
    /**
     * @param smsTo
     * @param smsText
     */
    public static void send(String from, String fromName, String to, String subject, String message) throws Exception {
    	SimpleEmail notification = MailConfiguration.getInstance().getSimpleEmailInstance(from, fromName, subject);
		notification.addTo(to);
		notification.setMsg(message);
		notification.send();
    }
}