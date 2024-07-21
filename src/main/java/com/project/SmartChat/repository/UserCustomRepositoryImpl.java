package com.project.SmartChat.repository;

import org.springframework.stereotype.Repository;

import com.project.SmartChat.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserCustomRepositoryImpl implements UserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByUsername(String username) {
        List<User> users = entityManager
                .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    public List<User> getAllUsers(){

        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        return users;
    }
}
