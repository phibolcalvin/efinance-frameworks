package com.nokor.frmk.tools;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nokor.frmk.tools.messaging.nexmo.Message;
import com.nokor.frmk.tools.messaging.nexmo.NexmoSmsClient;
import com.nokor.frmk.tools.messaging.nexmo.SmsSubmissionResult;
import com.nokor.frmk.tools.messaging.nexmo.TextMessage;
import com.nokor.frmk.tools.messaging.nexmo.UnicodeMessage;

/**
 * @author youhort.ly
 */
public class SmsSender {

	private static final Log log = LogFactory.getLog(SmsSender.class);
	
    /**
     * @param smsTo
     * @param smsText
     */
    public static void send(String smsTo, String smsText) {
        send(new TextMessage(MessagingConfig.getInstance().getConfiguration().getString("messaging.sms.smsfrom"), smsTo, smsText));
    }
    
    /**
     * @param smsTo
     * @param smsText
     */
    public static void sendUnicode(String smsTo, String smsText) {
    	send(new UnicodeMessage(MessagingConfig.getInstance().getConfiguration().getString("messaging.sms.smsfrom"), smsTo, smsText));
    }
    
    /**
     * @param smsTo
     * @param smsText
     */
    private static void send(Message message) {

        // Create a client for submitting to Nexmo
        NexmoSmsClient client = null;
        try {
            client = new NexmoSmsClient(MessagingConfig.getInstance().getConfiguration().getString("messaging.sms.apikey"), 
            		MessagingConfig.getInstance().getConfiguration().getString("messaging.sms.apisecret"));
        } catch (Exception e) {
        	log.error("Failed to instanciate a Nexmo Client", e);
            throw new RuntimeException("Failed to instanciate a Nexmo Client");
        }

        // Use the Nexmo client to submit the Text Message ...
        SmsSubmissionResult[] results = null;
        try {
            results = client.submitMessage(message);
        } catch (Exception e) {
        	log.error("Failed to communicate with the Nexmo Client", e);
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }

        // Evaluate the results of the submission attempt ...
        log.debug("... Message submitted in [ " + results.length + " ] parts");
        for (int i = 0; i < results.length; i++) {
        	log.debug("--------- part [ " + (i + 1) + " ] ------------");
        	log.debug("Status [ " + results[i].getStatus() + " ] ...");
            if (results[i].getStatus() == SmsSubmissionResult.STATUS_OK) {
            	log.debug("SUCCESS");
            } else if (results[i].getTemporaryError()) {
            	log.debug("TEMPORARY FAILURE - PLEASE RETRY");
            } else {
            	log.debug("SUBMISSION FAILED!");
            }
            log.debug("Message-Id [ " + results[i].getMessageId() + " ] ...");
            log.debug("Error-Text [ " + results[i].getErrorText() + " ] ...");

            if (results[i].getMessagePrice() != null) {
            	log.debug("Message-Price [ " + results[i].getMessagePrice() + " ] ...");
            }
            if (results[i].getRemainingBalance() != null) {
            	log.debug("Remaining-Balance [ " + results[i].getRemainingBalance() + " ] ...");
            }
        }
    }
}