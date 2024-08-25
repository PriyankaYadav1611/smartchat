package com.project.SmartChat.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParticipantId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "user_id")
    private Long userId;

    public ParticipantId() {}

    public ParticipantId(Long groupId, Long userId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantId that = (ParticipantId) o;
        return Objects.equals(groupId, that.groupId) &&
               Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}
