package com.example.data01.dataRepository;

import com.example.data01.dto.BookDTO;
import com.example.data01.type.BookCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional @Commit
class BookQueryRepositoryTest {
    @Autowired
    BookQueryRepository bookQueryRepository;

//    @BeforeEach
//    void setUp(){
//        Book book1 = new Book();
//        book1.setCategory(BookCategory.IT);
//        book1.setName("JPA");
//        book1.setPrice(10_000);
//        book1.setPublishDate(LocalDate.of(2023, 11, 11));
//
//        Book book2 = new Book();
//        book2.setCategory(BookCategory.NOVEL);
//        book2.setName("해리포터");
//        book2.setPrice(20_000);
//        book2.setPublishDate(LocalDate.of(2000, 1, 12));
//
//        Book book3 = new Book();
//        book3.setCategory(BookCategory.HISTORY);
//        book3.setName("세계로");
//        book3.setPrice(15_000);
//        book3.setPublishDate(LocalDate.of(2010, 7, 23));
//
//        bookQueryRepository.saveAll(List.of(book1, book2, book3));
//    }

    @Test
    void findNameList(){
        bookQueryRepository.findNameList()
                .forEach(System.out::println);
    }

    @Test
    void findBookQuery(){
        bookQueryRepository.findBookQuery(BookCategory.IT, LocalDateTime.now());
    }

    @Test
    void findByIn(){
        bookQueryRepository.findByIn(List.of(BookCategory.IT, BookCategory.HISTORY));
    }

    @Test
    void findTotalPriceNovel(){
        int totalPriceNovel = bookQueryRepository.findTotalPriceNovel();
        System.out.println("totalPriceNovel = " + totalPriceNovel);
    }

    @Test
    void countIdOfGroup(){
        int count = bookQueryRepository.countIdOfGroup();
        System.out.println("count = " + count);
    }

    @Test
    void findDtoById(){
        BookDTO dto = bookQueryRepository.findDtoById(1L);
        System.out.println("dto = " + dto);
    }

    @Test
    void findAvgPriceOfCategory(){
        Map<String, Object> map = bookQueryRepository.findAvgPriceOfCategory(BookCategory.IT);
        System.out.println("map = " + map);
        System.out.println(map.get("0"));
    }

    @Test
    void findAvgPricePerCategory(){
        bookQueryRepository.findAvgPricePerCategory()
                .forEach(System.out::println);
    }

    @Test
    void findAllNameWithPrice(){
        bookQueryRepository.findAllNameWithPrice()
                .forEach(System.out::println);
    }

    @Test
    void findBooksByPublishDateYY(){
        bookQueryRepository.findBooksByPublishDateYY("2010");
    }

    @Test
    void nativeFind(){
        bookQueryRepository.nativeFind();
    }

}













