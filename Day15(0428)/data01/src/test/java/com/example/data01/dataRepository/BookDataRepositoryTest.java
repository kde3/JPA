package com.example.data01.dataRepository;

import com.example.data01.entity.Book;
import com.example.data01.type.BookCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional @Commit
class BookDataRepositoryTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    BookDataRepository bookDataRepository;

    @Test
    void save(){
        Book book = new Book();
        book.setName("test");
        book.setPrice(10_000);
        book.setCategory(BookCategory.IT);
        book.setPublishDate(LocalDate.of(2000, 10, 10));

        bookDataRepository.save(book);
    }

    @Test
    void saveAll(){
        Book book1 = new Book();
        book1.setCategory(BookCategory.IT);
        book1.setName("JPA");
        book1.setPrice(10_000);
        book1.setPublishDate(LocalDate.of(2023, 11, 11));

        Book book2 = new Book();
        book2.setCategory(BookCategory.NOVEL);
        book2.setName("해리포터");
        book2.setPrice(20_000);
        book2.setPublishDate(LocalDate.of(2000, 1, 12));

        Book book3 = new Book();
        book3.setCategory(BookCategory.HISTORY);
        book3.setName("세계로");
        book3.setPrice(15_000);
        book3.setPublishDate(LocalDate.of(2010, 7, 23));

//        bookDataRepository.save(book1);
//        bookDataRepository.save(book2);
//        bookDataRepository.save(book3);

//        List.of(book1, book2, book3).forEach(bookDataRepository::save);
        bookDataRepository.saveAll(List.of(book1, book2, book3));
    }

    @Test
    void find(){
        bookDataRepository.findById(3L).ifPresent(book -> System.out.println(book));
        bookDataRepository.findAll().forEach(book -> System.out.println(book));
    }

    @Test
    void delete(){
        bookDataRepository.deleteById(3L);
        bookDataRepository.deleteAll();
    }

//    ========================================================================

    @Test
    void findBy(){
        List<Book> bookList = bookDataRepository.findBy();
        bookDataRepository.findTestBy();
        bookDataRepository.findBookBy();
    }

    @Test
    void findByName(){
        List<Book> bookList = bookDataRepository.findByName("해리포터");
        bookDataRepository.findByPrice(10_000);

        System.out.println("bookList = " + bookList);
    }

    @Test
    void findEntityByName(){
        Book book = bookDataRepository.findEntityByName("해리포터");
        System.out.println("book = " + book);

        Book book1 = bookDataRepository.findOptionalByName("해리포터")
                .orElse(new Book());
        System.out.println("book1 = " + book1);
    }

    @Test
    void operTest(){
//        bookDataRepository.findByPriceGreaterThan(10_000);
//        bookDataRepository.findByPriceLessThan(10_000);

//        bookDataRepository.findByPublishDateGreaterThanEqual(LocalDate.of(2000, 10, 10));
//        bookDataRepository.findByPublishDateBefore(LocalDate.now());
    }
    @Test
    void likeTest(){
//        bookDataRepository.findByNameLike("해");

//        bookDataRepository.findByNameContaining("해");

        bookDataRepository.findByNameStartingWith("해");
        bookDataRepository.findByNameStartsWith("해");
    }

    @Test
    void isNullTest(){
        bookDataRepository.findByPriceIsNull();
        bookDataRepository.findByPriceIsNotNull();
    }

    @Test
    void SqlOper(){
//        bookDataRepository.findByPriceBetween(1_000, 12_000);
//        bookDataRepository.findByNameIn(List.of("해리포터", "B", "C", "D"));

//        bookDataRepository.findByNameIn(Set.of("해리포터", "B", "C", "D"));

        bookDataRepository.findCollectionByNameIn(List.of("해리포터", "B", "C", "D"));
    }

    @Test
    void andOrTest(){
        String query = "select b from Book b where b.name = ?1 and b.price = ?2";
        List<Book> resultList = em.createQuery(query, Book.class)
                .setParameter(1, "JPA")
                .setParameter(2, 10_000)
                .getResultList();

//        bookDataRepository.findByNameAndPrice("JPA", 10_000);
    }

    @Test
    void otherTest(){
//        bookDataRepository.findByPriceGreaterThanOrderById(10_000);

//        bookDataRepository.findByNameIgnoreCase("aBcD");

//        bookDataRepository.findDistinctByPrice(10_000);

        bookDataRepository.findTop3ByName("test");
    }

    @Test
    void countTest(){
//        bookDataRepository.countBy();
//        bookDataRepository.countByPublishDateBefore(LocalDate.now());

//        bookDataRepository.existsBy();
//        bookDataRepository.existsByCategory(BookCategory.IT);

        bookDataRepository.deleteByPublishDateAfter(LocalDate.of(2000,10,10));
    }
}







