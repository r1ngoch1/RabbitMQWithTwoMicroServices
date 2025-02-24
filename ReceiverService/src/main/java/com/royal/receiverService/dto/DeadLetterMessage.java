package com.royal.receiverService.dto;

import java.sql.Timestamp;

public class DeadLetterMessage {
    private Long id;
    private String messageContent;
    private String errorReason;
    private Timestamp timestamp;


    public DeadLetterMessage(String messageBody, String errorReason, Timestamp timestamp) {
        this.messageContent = messageBody;
        this.errorReason = errorReason;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageBody() {
        return messageContent;
    }

    public void setMessageBody(String messageBody) {
        this.messageContent = messageBody;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
