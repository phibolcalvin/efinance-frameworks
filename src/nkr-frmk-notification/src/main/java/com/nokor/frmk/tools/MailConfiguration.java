package com.nokor.frmk.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author youhort.ly
 */
public class MailConfiguration {
	
	/** Logger */
	private static final Log log = LogFactory.getLog(MailConfiguration.class);
	
	private static MailConfiguration instance = null;
	
	private MessagingConfig config = null;
	
	MailConfiguration() {
		log.debug("new MailConfiguration instance");
		this.config = MessagingConfig.getInstance();
	}
	
	/**
     * @return
     */
    public static MailConfiguration getInstance() {
    	if (instance == null) {
    		instance = new MailConfiguration();
    	}
    	return instance;
    }
	
	/**
	 * @return
	 */
	public boolean isActive(){ 
		return this.config.getConfiguration().getBoolean("active");
	}
	
	/**
	 * @param from
	 * @param fromName
	 * @param subject
	 * @return
	 */
	public MultiPartEmail getMultiPartEmailInstance(String from, String fromName, String subject) {
		MultiPartEmail res = new MultiPartEmail();
		this.configure(res, from, fromName, subject);
		return res;
	}

	/**
	 * @param from
	 * @param fromName
	 * @param subject
	 * @return
	 */
	public HtmlEmail getHtmlEmailInstance(String from, String fromName, String subject) {
		HtmlEmail res = new HtmlEmail();
		this.configure(res, from, fromName, subject);
		return res;
	}
	
	/**
	 * @param from
	 * @param fromName
	 * @param subject
	 * @return
	 */
	public SimpleEmail getSimpleEmailInstance(String from, String fromName, String subject) {
		SimpleEmail res = new SimpleEmail();
		this.configure(res, from, fromName, subject);
		return res;
	}
	
	/**
	 * @param email
	 * @param from
	 * @param fromName
	 * @param subject
	 */
	private void configure(Email email, String from, String fromName, String subject) {
		if(!this.isActive()) {
			log.debug("EMail function is disabled.");
		} else {
			email.setHostName(this.config.getConfiguration().getString("smtpserver"));
			email.setSmtpPort(this.config.getConfiguration().getInt("smtpport"));
			email.setAuthenticator(new DefaultAuthenticator(this.config.getConfiguration().getString("username"), this.config.getConfiguration().getString("password")));
			try {
				email.setFrom(from, fromName, "UTF-8");
			} catch (EmailException e) {
				new IllegalArgumentException("Error while setting mail configuration");
			}
			email.setSubject(subject);
		}
	}
}
