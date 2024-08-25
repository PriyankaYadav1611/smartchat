package com.project.SmartChat.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="participant")
public class Participant {
    
    @EmbeddedId
    private ParticipantId id;

   @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors, getters, setters

    public Participant() {}

    public Participant(Group group, User user) {
        this.id = new ParticipantId(group.getId(), user.getId());
        this.group = group;
        this.user = user;
    }


    public ParticipantId getId() {
        return id;
    }

    public void setId(ParticipantId id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
