package com.project.SmartChat.repository;

import org.springframework.stereotype.Repository;

import com.project.SmartChat.model.User;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public List<User> getAllUsers() {

        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        TypedQuery<User> typedQuery = entityManager
                .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id);
        return typedQuery.getResultList().stream().findFirst();
    }

    @Override
    public int deleteByUserName(String username) {
        // String jpql = "DELETE FROM User u WHERE u.username = :username";
        // Query query = entityManager.createQuery(jpql);
        // query.setParameter("username", username);
        // return jpql.executeUpdate();

        String jpql = "DELETE FROM User u WHERE u.username = :username";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("username", username);
        return query.executeUpdate();

    }
}
