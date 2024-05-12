package com.example.data01.pageRepository;

import com.example.data01.entity.Book;
import com.example.data01.type.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPageRepository extends JpaRepository<Book, Long> {

//    페이징처리를 하기 위해서 반환타입을 Page로,
//    페이징 조건을 넣기 위해서 매개변수에 Pageable을 선언한다.
    Page<Book> findByCategory(BookCategory category, Pageable pageable);

//    jpql을 사용하여도 동일하다.
    @Query("select b from Book b where b.id > 10")
    Page<Book> findBooks(Pageable pageable);
}
