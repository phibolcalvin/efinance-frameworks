package com.nokor.frmk.tools.messaging.nexmo;


public class UnicodeMessage extends Message {

    static final long serialVersionUID = 8327129506926552344L;

    /**
     * @param from
     * @param to
     * @param messageBody
     */
    public UnicodeMessage(final String from,
                          final String to,
                          final String messageBody) {
        super(MESSAGE_TYPE_UNICODE,
              from,
              to,
              messageBody,
              null,
              null,
              null,
              true,
              false,
              null,
              null,
              0,
              null,
              null);
    }

    /**
     * @param from
     * @param to
     * @param messageBody
     * @param clientReference
     * @param statusReportRequired
     * @param messageClass
     * @param protocolId
     */
    public UnicodeMessage(final String from,
                          final String to,
                          final String messageBody,
                          final String clientReference,
                          final boolean statusReportRequired,
                          final MessageClass messageClass,
                          final Integer protocolId) {
        super(MESSAGE_TYPE_TEXT,
              from,
              to,
              messageBody,
              null,
              null,
              clientReference,
              true,
              statusReportRequired,
              null,
              null,
              0,
              messageClass,
              protocolId);
    }

}
