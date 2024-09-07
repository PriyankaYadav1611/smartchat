package com.project.SmartChat.model.dto;

import java.util.Date;


public class MessageDTO {

    private Long id;
    private Long senderId;
    private Long groupId;
    private String content;
    private Date sentAt;
    private boolean isSeen = false;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    } 
 
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public boolean getIsSeen() {
        return isSeen;
    }
}
