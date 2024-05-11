package com.example.data01.dataRepository;

import com.example.data01.embedded.Address;
import com.example.data01.entity.Book;
import com.example.data01.entity.CheckOut;
import com.example.data01.entity.User;
import com.example.data01.repository.BookRepository;
import com.example.data01.repository.CheckOutRepository;
import com.example.data01.repository.UserRepository;
import com.example.data01.type.BookCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional @Commit
class BookBulkRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckOutRepository checkOutRepository;

    @Autowired
    BookBulkRepository bookBulkRepository;

//    @BeforeEach
    void setUp(){
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

        Address address1 = new Address("서울특별시 강남구", "101호", "11111");
        Address address2 = new Address("서울특별시 송파구", "202호", "22222");
        Address address3 = new Address("서울특별시 노원구", "303호", "33333");


        User user1 = new User();
        user1.setName("뽀로로");
        user1.setBirth(LocalDate.of(2000, 1, 1));
        user1.setPhone("010111111111");
        user1.setAddress(address1);

        User user2 = new User();
        user2.setName("루피");
        user2.setBirth(LocalDate.of(2010, 10, 19));
        user2.setPhone("01022222222");
        user2.setAddress(address2);

        User user3 = new User();
        user3.setName("크롱");
        user3.setBirth(LocalDate.of(2013, 5, 30));
        user3.setPhone("01033333333");
        user3.setAddress(address3);

        List.of(book1, book2, book3).forEach(bookRepository::save);
        List.of(user1, user2, user3).forEach(userRepository::save);

        CheckOut checkOut1 = new CheckOut();
        checkOut1.setBook(book1);
        checkOut1.setUser(user1);

        CheckOut checkOut2 = new CheckOut();
        checkOut2.setBook(book2);
        checkOut2.setUser(user2);

        CheckOut checkOut3 = new CheckOut();
        checkOut3.setBook(book3);
        checkOut3.setUser(user3);

        List.of(checkOut1, checkOut2, checkOut3).forEach(checkOutRepository::save);

        em.flush();
        em.clear();
    }

    @Test
    void test(){
//        Book foundBook = em.find(Book.class, 1L);
//        foundBook.setName("update!!");

        List<Book> bookList = bookBulkRepository.findAll();
        bookList.forEach(book -> book.setName("update!!"));

//        기존의 변경감지를 이용한 업데이트는
//        여러건의 데이터를 수정할 때 불리하다.
//        1. N개의 데이터를 모두 영속성 컨텍스트로 가져온다.
//        2. N개의 데이터를 setter로 모두 수정한다.
//        3. flush 시점에 N개의 변경감지가 일어나며, N개의 SQL문이 실행된다.
    }

    @Test
    void bulkUpdate(){
//        벌크 연산(벌크성 쿼리)이란 여러 행의 데이터를 하나의 쿼리로 수정, 삭제하는 것을 의미
        em.createQuery("update Book b set b.name = 'update!!' ")
                .executeUpdate(); //수정, 삭제 쿼리는 executeUpdate()를 사용해야한다.
    }

    @Test
    void bulkDelete(){
//        삭제도 동일한 방식으로 진행한다.
//        벌크 연산은 JPQL을 사용하여 DB에 쿼리를 즉시 보내기때문에
//        엔티티 관리와 무관한 상태이다. 그러므로 soft delete가 수행되지 않는다.
//        cascade 옵션도 적용되지 않으므로 연관관계를 맺은 데이터를 직접 삭제해야한다.

//        em.createQuery("delete from Book b where b.id < 3")
//                .executeUpdate();


        em.createQuery("update CheckOut ch set ch.book.id = null where ch.book.id < 3")
                .executeUpdate();
        em.createQuery("delete from Book b where b.id < 3")
                .executeUpdate();
    }

    @Test
    void bulkTest(){
//        벌크성 쿼리에서 가장 주의해야하는 것은 데이터 불일치 문제이다.
        Book foundBook = em.find(Book.class, 1L);
        System.out.println("foundBook = " + foundBook);

//        조회 직후 벌크 연산으로 데이터를 수정하였다.
        em.createQuery("update Book b set b.name = 'update!!' where b.id < 3")
                .executeUpdate();

//      처음 조회한 1번 책도 수정 대상에 속하므로 DB에서 name칼럼이 수정되었을 것이다.
//       그러나 영속화된 1번 엔티티는 name이 수정되지 않았다.(DB와 Entity간의 데이터 불일치)
        System.out.println("foundBook = " + foundBook);

//        다시 조회를 해왔지만 여전히 1번 엔티티는 변경사항이 적용되지 않는다.
//        jpql로 가져온 엔티티 중 이미 영속화된 ID가 있다면 기존 엔티티를 유지하는 특징때문이다.
        List list = em.createQuery("select b from Book b where b.id < 3")
                .getResultList();

        System.out.println("list = " + list);
    }

    @Test
    void bulkTest2(){
        Book foundBook = em.find(Book.class, 1L);
        System.out.println("foundBook = " + foundBook);

        em.createQuery("update Book b set b.name = '수정완료!' where b.id < 3")
                .executeUpdate();
        
//        불일치 문제를 해결하는 방법
//        1. refresh() : 영속성 컨텍스트에 존재하는 특정 엔티티를 DB에서 다시 조회한다.
//        em.refresh(foundBook);
//        2. 항상 벌크연산을 먼저 실행 : 가능한 상황이라면 제일 무난함
//        3. 영속성 컨텍스트를 비우기 : 전부 비우고 새로 읽어 온다.
        em.flush();
        em.clear();

        Book foundBook2 = em.find(Book.class, 1L);
        System.out.println("foundBook2 = " + foundBook2);
    }


    @Test
    void updateBook(){
        Optional<Book> opt = bookBulkRepository.findById(1L);
        opt.ifPresent(book -> System.out.println("book = " + book));

        bookBulkRepository.updateBook();

//        em.flush();
        em.clear();

        Optional<Book> opt2 = bookBulkRepository.findById(1L);
        opt2.ifPresent(book -> System.out.println("book = " + book));
    }

    @Test
    void updateBook2(){
        Optional<Book> opt = bookBulkRepository.findById(1L);
        opt.ifPresent(book -> System.out.println("book = " + book));

        bookBulkRepository.updateBook2(3L);

        Optional<Book> opt2 = bookBulkRepository.findById(1L);
        opt2.ifPresent(book -> System.out.println("book = " + book));
    }

}











