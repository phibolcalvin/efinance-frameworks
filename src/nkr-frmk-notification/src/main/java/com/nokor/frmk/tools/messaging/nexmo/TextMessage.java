package com.nokor.frmk.tools.messaging.nexmo;

public class TextMessage extends Message {

    static final long serialVersionUID = 6258872793039443129L;

    /**
     * @param from
     * @param to
     * @param messageBody
     */
    public TextMessage(final String from,
                       final String to,
                       final String messageBody) {
        super(MESSAGE_TYPE_TEXT,
              from,
              to,
              messageBody,
              null,
              null,
              null,
              false,
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
    public TextMessage(final String from,
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
              false,
              statusReportRequired,
              null,
              null,
              0,
              messageClass,
              protocolId);
    }

}
