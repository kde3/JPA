package com.example.data01.dataRepository;

import com.example.data01.dto.BookDTO;
import com.example.data01.entity.Book;
import com.example.data01.type.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface BookQueryRepository extends JpaRepository<Book, Long> {
//        @Query 활용

    @Query("select b.name from Book b")
    List<String> findNameList();

    @Query("select b from Book b where b.checkOut is null")
    List<Book> findBooksChIsNull();

//    매개변수와 jpql의 파라미터를 바인딩하려면 @Param을 활용한다.
    @Query("select b from Book b " +
            "where b.category = :category and b.modifiedDate > :modifiedDate")
    List<Book> findBookQuery(@Param("category") BookCategory category,
                             @Param("modifiedDate") LocalDateTime modifiedDate);

//    in
    @Query("select b from Book b where b.category in :categories")
    List<Book> findByIn(@Param("categories")Collection<BookCategory> categories);

//    JPQL은 집계함수를 모두 제공한다.
    @Query("select sum(b.price) from Book b where b.category = 'NOVEL'")
    int findTotalPriceNovel();

//    group by 와 having을 지원한다.
    @Query("select count(b.id) from Book b " +
            "group by b.category having count(b.id) < 2")
    int countIdOfGroup();

//    dto로 반환을 설정할 수 있다.
    @Query(
            """
            select 
                new com.example.data01.dto.BookDTO(b.id, b.category, b.name, b.price, b.publishDate) 
            from Book b
            where b.id = :id
            """
    )
    BookDTO findDtoById(@Param("id") Long bookId);

//    dto조회 결과가 여러 행이라면 List로 감싼다.
    @Query("""
            select 
                new com.example.data01.dto.BookDTO(b.id, b.category, b.name, b.price, b.publishDate) 
            from Book b
            where b.price > :price
            """)
    List<BookDTO> findDtoByPrice(@Param("price") int price);

//    반환을 Map으로 받을 수 있다.
//    map을 사용하는 경우 별칭이 key로 설정된다.
    @Query("""
        select new Map(b.category as cate, avg(b.price) as avg, sum(b.price) as sum)
        from Book b
        group by b.category having b.category = :category
    """)
    Map<String, Object> findAvgPriceOfCategory(@Param("category") BookCategory category);

//    Map으로 반환할 때 여러 행을 반환받으려면 List로 감싼다.
    @Query("""
        select new Map(b.category as cate, round(avg(b.price), 2) as avg, sum(b.price) as sum)
        from Book b
        group by b.category
    """)
    List<Map<String, Object>> findAvgPricePerCategory();

//    JPQL에서 기본 제공하는 함수
    @Query("select concat(b.name, '의 가격 : ', b.price, '원') from Book b")
    List<String> findAllNameWithPrice();

//    표준 SQL의 명령어가 아닌 특정 벤더사의 명령어를 방언이라고 한다.
//    오라클에서 지원하는 형변환 함수 TO_CHAR(SYSDATE, 'YYYY-MM-DD')는 방언이기 때문에
//    별도의 등록이 필요하다.
//    하이버네이트는 DB별 기본적인 함수들을 등록해놨기 때문에 바로 사용 가능하다.
//    사용할 때는 function('함수명', 함수에 전달할 인수1, 함수에 전달할 인수2, ....)으로 사용한다.

//    현재 DB가 오라클이기 때문에 방언을 직접 호출하여 사용하는것도 가능하지만
//    DB 벤더사 변경시 문제가 발생될 수 있다.
//    @Query("select b from Book b where function('to_char', b.publishDate, 'YYYY') = :year")

//    jpql에서 기본제공하는 year함수를 사용해도 연도만 뽑을 수 있다.
    @Query("select b from Book b where year(b.publishDate) = :year")
    List<Book> findBooksByPublishDateYY(@Param("year") String year);

//    nativeQuery를 사용할 수 있다.
    @Query(value = "SELECT * FROM TBL_BOOK", nativeQuery = true)
    List<Book> nativeFind();

}
















