package com.nokor.frmk.tools.messaging.nexmo;

public enum MessageClass {

    /**
     * Message Class 0
     */
    MESSAGE_CLASS_0(0),

    /**
     * Message Class 1
     */
    MESSAGE_CLASS_1(1),

    /**
     * Message Class 2
     */
    MESSAGE_CLASS_2(2),

    /**
     * Message Class 3
     */
    MESSAGE_CLASS_3(3);

    private final int messageClass;

    private MessageClass(int messageClass) {
        this.messageClass = messageClass;
    }

    public int getMessageClass() {
        return this.messageClass;
    }

}
