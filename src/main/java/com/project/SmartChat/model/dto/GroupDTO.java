package com.project.SmartChat.model.dto;
import java.sql.Date;
import java.util.List;

import com.project.SmartChat.enums.GroupType;


public class GroupDTO {
    private Long id;
    private String title;
    private String description;
    private Date createdDate;
    private Long createdById;  // Represents the ID of the User who created the group
    private GroupType type;
    private List<Long> groupMembers;

    // Default constructor
    public GroupDTO() {
    }

    // Parameterized constructor
    public GroupDTO(String title, String description, Date createdDate, Long createdById, GroupType type, List<Long> groupMembers) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.createdById = createdById;
        this.type = type;
        this.groupMembers = groupMembers;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public void setGroupMembers(List<Long> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public List<Long> getGroupMembers() {
        return groupMembers;
    }
}