package com.example.data01.repository;

import com.example.data01.embedded.Address;
import com.example.data01.entity.Book;
import com.example.data01.entity.CheckOut;
import com.example.data01.entity.User;
import com.example.data01.type.BookCategory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional @Commit
class RepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckOutRepository checkOutRepository;

    @Test
    void save() {
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

        System.out.println("checkout3 = " + checkOut3);
        System.out.println("book3 = " + book3);
        System.out.println(book3.getCheckOut().getId());

        System.out.println("user3 = " + user3);

        checkOut3.setUser(user2);
        checkOutRepository.save(checkOut3);

        System.out.println("checkOut3.getUser() = " + checkOut3.getUser());
        System.out.println("user3 = " + user3);
    }

    @Test
    void findById() {
        bookRepository.findById(2L).ifPresent(book -> System.out.println(book));
//        bookRepository.findById(2L).ifPresent(System.out::println);
    }

    @Test
    void findAll() {
        List<CheckOut> checkOutList = checkOutRepository.findAll();
        System.out.println("checkOutList = " + checkOutList);
    }

    @Test
    void delete() {
        checkOutRepository.findById(2L)
                .ifPresent(checkOutRepository::delete);

        Optional<Book> opt = bookRepository.findById(2L);
        opt.ifPresent(book -> bookRepository.delete(book));

    }

    // Entity 빠르게 초기화 하기 - 빌더 패턴
    @Test
    void BuilderTest() {
        Book test = Book.builder()
                .name("test")
                .price(10_100)
                .category(BookCategory.IT)
                .build();
    }
}














