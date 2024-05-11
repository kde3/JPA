package com.example.data01.repository;

import com.example.data01.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    @PersistenceContext
    private EntityManager em;

    public Book save(Book book) {
        em.persist(book);
        return book;
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    public List<Book> findAll(){
        String query = "select b from Book b";
        return em.createQuery(query, Book.class).getResultList();
    }

    public void delete(Book book){
        em.remove(book);
    }
}
