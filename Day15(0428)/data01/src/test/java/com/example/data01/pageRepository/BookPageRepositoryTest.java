package com.example.data01.pageRepository;

import com.example.data01.entity.Book;
import com.example.data01.type.BookCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional @Commit
class BookPageRepositoryTest {
    @Autowired
    BookPageRepository bookPageRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void jpaPaging(){
        List<Book> resultList = em.createQuery("select b from Book b where b.category = :cate", Book.class)
                .setParameter("cate", BookCategory.IT)
                .setFirstResult(0) // 데이터 시작 번호 (page-1) * amount
                .setMaxResults(10) // 페이지 당 보여줄 데이터 수
                .getResultList();
    }

    @Test
    void findByCategory(){
//        Pageable을 구현한 구현체는 PageRequest가 있다.
        PageRequest pageRequest = PageRequest.of(0, 10); // page번호는 0부터 사용한다.

//        반환타입이 Page인 경우 자동으로 totalCount 쿼리가 실행된다.
        Page<Book> bookPage = bookPageRepository.findByCategory(BookCategory.IT, pageRequest);

        List<Book> content = bookPage.getContent();
        System.out.println("조회된 데이터 = " + content);

        long totalElements = bookPage.getTotalElements();
        System.out.println("토탈 데이터 수 = " + totalElements);

        int number = bookPage.getNumber();
        System.out.println("현재 페이지 번호 = " + number);

        int totalPages = bookPage.getTotalPages();
        System.out.println("총 페이지 수 = " + totalPages);

        boolean first = bookPage.isFirst();
        System.out.println("첫 번째 페이지인가? = " + first);
        boolean last = bookPage.isLast();
        System.out.println("마지막 페이지인가? = " + last);

        boolean hasNext = bookPage.hasNext();
        System.out.println("다음 페이지가 존재하는가? = " + hasNext);
        boolean hasPrevious = bookPage.hasPrevious();
        System.out.println("이전 페이지가 존재하는가? = " + hasPrevious);
    }

    @Test
    void findByCategory2(){
//        sort는 정렬 기준을 설정한다.
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");

        PageRequest pageRequest = PageRequest.of(0, 2, sortById);

        Page<Book> bookPage = bookPageRepository.findByCategory(BookCategory.IT, pageRequest);


    }
}









