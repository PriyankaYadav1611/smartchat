package com.project.SmartChat.model.dto;


public class ParticipantDTO {
    
    private Long groupId;

    private Long userId;

    public ParticipantDTO() {}

    public ParticipantDTO(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public void setGroupId(Long groupId){
        this.groupId = groupId;
    }

    public Long getGroupId(){
        return groupId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }

}
