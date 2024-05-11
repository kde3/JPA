package com.example.data01.repository;

import com.example.data01.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public User save (User user) {
        em.persist(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public List<User> findAll(){
        String query = "select u from User u";
        return em.createQuery(query, User.class).getResultList();
    }

    public void delete(User user){
        em.remove(user);
    }
}
