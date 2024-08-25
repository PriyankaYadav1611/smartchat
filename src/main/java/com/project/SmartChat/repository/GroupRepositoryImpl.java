package com.project.SmartChat.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.project.SmartChat.model.Group;

@Repository
@Transactional
public class GroupRepositoryImpl implements GroupRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Group save(Group group) {
        if (group.getId() == null) {
            entityManager.persist(group);
            return group;
        } else {
            return entityManager.merge(group);
        }
    }

    @Override
    public Group findById(Long id) {
        return entityManager.find(Group.class, id);
    }

    @Override
    public List<Group> findAll() {
        return entityManager.createQuery("SELECT g FROM Group g", Group.class).getResultList();
    }

    @Override
    public void delete(Group group) {
        if (entityManager.contains(group)) {
            entityManager.remove(group);
        } else {
            entityManager.remove(entityManager.merge(group));
        }
    }
}
