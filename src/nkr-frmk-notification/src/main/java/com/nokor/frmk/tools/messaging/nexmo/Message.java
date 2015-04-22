package com.nokor.frmk.tools.messaging.nexmo;


public abstract class Message implements java.io.Serializable {

    private static final long serialVersionUID = 3847700435531116012L;

    /**
     * Message ia a regular TEXT SMS message
     */
    public static final int MESSAGE_TYPE_TEXT = 1;
    /**
     * Message ia a binary SMS message with a custom UDH and binary payload
     */
    public static final int MESSAGE_TYPE_BINARY = 2;
    /**
     * Message is a wap-push message to send a browsable / downloadable url to the handset
     */
    public static final int MESSAGE_TYPE_WAPPUSH = 3;
    /**
     * Message is a unicode message, for sending messages in non-latin script to a supported handset
     */
    public static final int MESSAGE_TYPE_UNICODE = 4;

    /**
     * Message is a USSD 'display' message that displays on handset but does not require a response
     */
    public static final int MESSAGE_TYPE_USSD_DISPLAY = 5;

    /**
     * Message is a USSD 'prompt' message that requires a response from the end user
     */
    public static final int MESSAGE_TYPE_USSD_PROMPT = 6;

    private final int type;

    private final String from;
    private final String to;
    private final String messageBody;
    private final byte[] binaryMessageBody;
    private final byte[] binaryMessageUdh;
    private final String clientReference;
    private final boolean unicode;
    private final boolean statusReportRequired;
    private final String wapPushUrl;
    private final String wapPushTitle;
    private final int wapPushValidity;
    private final MessageClass messageClass;
    private final Integer protocolId;

    /**
     * @param type
     * @param from
     * @param to
     * @param messageBody
     * @param binaryMessageBody
     * @param binaryMessageUdh
     * @param clientReference
     * @param unicode
     * @param statusReportRequired
     * @param wapPushUrl
     * @param wapPushTitle
     * @param wapPushValidity
     * @param messageClass
     * @param protocolId
     */
    protected Message(final int type,
                      final String from,
                      final String to,
                      final String messageBody,
                      final byte[] binaryMessageBody,
                      final byte[] binaryMessageUdh,
                      final String clientReference,
                      final boolean unicode,
                      final boolean statusReportRequired,
                      final String wapPushUrl,
                      final String wapPushTitle,
                      final int wapPushValidity,
                      final MessageClass messageClass,
                      final Integer protocolId) {
        this.type = type;

        this.from = from;
        this.to = to;
        this.messageBody = messageBody;
        this.binaryMessageBody = binaryMessageBody;
        this.binaryMessageUdh = binaryMessageUdh;
        this.clientReference = clientReference;
        this.unicode = unicode;
        this.statusReportRequired = statusReportRequired;
        this.wapPushUrl = wapPushUrl;
        this.wapPushTitle = wapPushTitle;
        this.wapPushValidity = wapPushValidity;
        this.messageClass = messageClass;
        this.protocolId = protocolId;
    }

    /**
     * @return
     */
    public int getType() {
        return this.type;
    }

    /**
     * @return
     */
    public String getFrom() {
        return this.from;
    }

    /**
     * @return
     */
    public String getTo() {
        return this.to;
    }

    /**
     * @return
     */
    public String getMessageBody() {
        return this.messageBody;
    }

    /**
     * @return
     */
    public byte[] getBinaryMessageBody() {
        return this.binaryMessageBody == null ? null : this.binaryMessageBody.clone();
    }

    /**
     * @return
     */
    public byte[] getBinaryMessageUdh() {
        return this.binaryMessageUdh == null ? null : this.binaryMessageUdh.clone();
    }

    /**
     * @return
     */
    public String getClientReference() {
        return this.clientReference;
    }

    /**
     * @return
     */
    public boolean isUnicode() {
        return this.unicode;
    }

    /**
     * @return
     */
    public boolean getStatusReportRequired() {
        return this.statusReportRequired;
    }

    /**
     * @return
     */
    public String getWapPushUrl() {
        return this.wapPushUrl;
    }

    /**
     * @return
     */
    public String getWapPushTitle() {
        return this.wapPushTitle;
    }

    /**
     * @return
     */
    public int getWapPushValidity() {
        return this.wapPushValidity;
    }

    public MessageClass getMessageClass() {
        return this.messageClass;
    }

    public Integer getProtocolId() {
        return this.protocolId;
    }

}
