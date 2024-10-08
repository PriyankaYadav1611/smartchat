package com.project.SmartChat.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Participant;
import com.project.SmartChat.model.ParticipantId;
import com.project.SmartChat.model.User;

@Repository
@Transactional
public class ParticipantRepositoryImpl implements ParticipantRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Participant save(Participant participant) {
        if (participant.getId() == null) {
            entityManager.persist(participant);
            return participant;
        } else {
            return entityManager.merge(participant);
        }
    }

    @Override
    public void saveAll(List<Participant> participants) {
        for (int i = 0; i < participants.size(); i++) {
            Group group = entityManager.find(Group.class, participants.get(i).getGroup().getId());
            User user = entityManager.find(User.class, participants.get(i).getUser().getId());

            // Set the managed entities to the participant
            participants.get(i).setGroup(group);
            participants.get(i).setUser(user);
            
            entityManager.persist(participants.get(i));


            // save in batch of 20
            if (i % 20 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public List<User> getUsersByGroupId(Long groupId) {
        return entityManager.createQuery("SELECT p.user FROM Participant p WHERE p.group.id = :groupId", User.class)
                            .setParameter("groupId", groupId)
                            .getResultList();
    }

    @Override
    public List<Group> findGroupsWithExactParticipants(List<Long> participantUserIds) {
        TypedQuery<Group> query = entityManager.createQuery(
            "SELECT p.group " +
            "FROM Participant p " +
            "GROUP BY p.group " +
            "HAVING COUNT(p.user.id) = :size " +
            "AND " +
            "SUM(CASE WHEN p.user.id IN :participantUserIds THEN 1 ELSE 0 END) = :size", 
            Group.class);
        query.setParameter("size", Long.valueOf(participantUserIds.size()));
        query.setParameter("participantUserIds", participantUserIds);

        return query.getResultList();
    }

    @Override
    public Participant findById(ParticipantId id) {
        return entityManager.find(Participant.class, id);
    }

    @Override
    public boolean isUserIdParticipantOfGroupId(Long userId, Long groupId) {
        TypedQuery<Participant> query = entityManager.createQuery(
            "SELECT p FROM Participant p " +
            "WHERE p.group.id = :groupId " +
            "AND " +
            "p.user.id = :userId",
            Participant.class);
        query.setParameter("groupId", groupId);
        query.setParameter("userId", userId);
        List<Participant> participants = query.getResultList();
        return !participants.isEmpty();
    }


    @Override
    public void delete(Participant participant) {
        if (entityManager.contains(participant)) {
            entityManager.remove(participant);
        } else {
            entityManager.remove(entityManager.merge(participant));
        }
    }

    @Override
    public List<Group> getAllGroupsWithUserId(Long userId) {
        TypedQuery<Group> query = entityManager.createQuery("SELECT p.group FROM Participant p WHERE p.user.id = :userId", Group.class)
        .setParameter("userId", userId);

        List<Group> groups = query.getResultList();
        return groups;
    }


    @Override
    public List<User> getUsersByGroupIds(List<Long> groupIds) {
        TypedQuery<User> query = entityManager.createQuery(
            "SELECT DISTINCT p.user FROM Participant p WHERE p.group.id IN :groupIds", User.class)
            .setParameter("groupIds", groupIds);

        return query.getResultList();
    }
}
