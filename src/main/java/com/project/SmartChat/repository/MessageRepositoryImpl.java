package com.project.SmartChat.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.project.SmartChat.model.Message;


@Repository
@Transactional
public class MessageRepositoryImpl implements MessageRepository {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Message save(Message message) {
        if (message.getId() == null) {
            entityManager.persist(message);
            return message;
        } else {
            return entityManager.merge(message);
        }
    }

    @Override
    public Message findById(Long id) {
        return entityManager.find(Message.class, id);
    }

    @Override
    public List<Message> getByGroupId(Long groupId) {
        return entityManager.createQuery("SELECT m FROM Message m WHERE m.group.id = :groupId", Message.class)
                            .setParameter("groupId", groupId)
                            .getResultList();
    
    }

    @Override
    public void delete(Message message) {
        if (entityManager.contains(message)) {
            entityManager.remove(message);
        } else {
            entityManager.remove(entityManager.merge(message));
        }
    }
    
}
