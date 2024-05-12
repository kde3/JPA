package com.example.data01.repository;

import com.example.data01.entity.CheckOut;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CheckOutRepository {
    @PersistenceContext
    private EntityManager em;

    public CheckOut save(CheckOut checkOut) {
        em.persist(checkOut);
        return checkOut;
    }

    public Optional<CheckOut> findById(Long id) {
        return Optional.ofNullable(em.find(CheckOut.class, id));
    }

    public List<CheckOut> findAll(){
        String query = "select c from CheckOut c";
        return em.createQuery(query, CheckOut.class).getResultList();
    }

    public void delete(CheckOut checkOut) {
        em.remove(checkOut);
    }
}
