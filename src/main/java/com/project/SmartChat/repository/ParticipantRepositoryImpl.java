package com.project.SmartChat.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.project.SmartChat.model.Group;
import com.project.SmartChat.model.Participant;

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
    public List<Participant> findByGroupId(Long groupId) {
        return entityManager.createQuery("SELECT p FROM Participants p WHERE p.group.id = :groupId", Participant.class)
                            .setParameter("groupId", groupId)
                            .getResultList();
    }

    @Override
    public List<Group> findGroupsWithExactParticipants(List<Long> participantUserIds) {
        TypedQuery<Group> query = entityManager.createQuery(
            "SELECT p.group " +
            "FROM Participants p " +
            "GROUP BY p.group " +
            "HAVING COUNT(p.user.id) = :size " +
            "AND SUM(CASE WHEN p.user.id IN :participantUserIds THEN 1 ELSE 0 END) = :size", 
            Group.class);
        query.setParameter("size", participantUserIds.size());
        query.setParameter("participantUserIds", participantUserIds);

        return query.getResultList();
    }

    @Override
    public Participant findById(Long id) {
        return entityManager.find(Participant.class, id);
    }

    @Override
    public void delete(Participant participant) {
        if (entityManager.contains(participant)) {
            entityManager.remove(participant);
        } else {
            entityManager.remove(entityManager.merge(participant));
        }
    }
}
