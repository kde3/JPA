package com.example.data01.dataRepository;

import com.example.data01.entity.Book;
import com.example.data01.entity.CheckOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CheckOutDataRepository extends JpaRepository<CheckOut, Long> {
    // 연관관계를 맺은 엔티티로 조회가능(해당 엔티티의 ID를 이용하여 조회함)
    // 즉, ID를 가지고 있는 객체여야 한다. 즉, 한번 영속화가 되어야 ID가 부여되어있을 것이고,
    // 비영속화인 애들은 조회할 수 없다.
    Optional<CheckOut> findByBook(Book book);

    // 연관관계를 맺은 엔티티의 ID로 조회가능
    Optional<CheckOut> findByBookId(Long bookId);

    // 연관관계를 맺은 엔티티의 필드로 조회가능
    Optional<CheckOut> findByBookName(String bookName);

//    ==============================================================

    // @Query를 사용할 때도 일반 join을 사용하면 지연로딩으로 인한 n+1문제가 동일하게 발생됨
    //(book에 대한 정보를 select해서 가져오지 않으니 book의 정보를 가져오기 위해
    // 쿼리를 또 날린다는 것.)
    @Query("select ch from CheckOut ch join ch.book")
    List<CheckOut> findByJoinBook();

    //위 n+1문제를 해결하려면 똑같이 fetch join을 사용해야 한다.
    //fetch join은 성능 향상을 위한 기본적인 방법이므로 반드시 사용할 줄 알아야.
    @Query("select ch from CheckOut  ch join fetch ch.book")
    List<CheckOut> findByFetchJoinBook();

    //연관관계가 잘 맺어져 있다면 자동으로 on이 걸린다.
    //일반적으로 연관관계를 맺은 엔티티끼리 join하여 조회한다(여러개 가능).
    //연관관계를 맺은 엔티티는 join을 할 때 알아서 on절에 PK=KF(등가조인) 조건이 생긴다.
    //outer join 가능하며 left, right를 활용하면 된다.
    //(다만 하이버네이트 버전에 따라 right가 불가능한 경우가 있음)(좀 옛날 버전 하이버네이트).
    @Query("select ch from CheckOut ch right join fetch ch.book join fetch ch.user")
    List<CheckOut> findByJoinBookAndUser();

    //만약 연관관계와 무관한 값으로 조인을 한다면?
    //-> on절이 1=1로 날라간다. 따라서 직접 on 절을 넣어줘야 한다.
//    @Query("select ch from CheckOut ch join Book")        //on 1=1
//    List<CheckOut> joinTest1();

//    @Query("select ch from CheckOut ch join Book b on ch.deleted = b.deleted")    //on절 명시
//    List<CheckOut> joinTest1();

    //연관관계와 무관한 데이터를 join하면 fetch 적용되지 않는다(꼭 기억할 것)
//    @Query("select ch from CheckOut ch join fetch Book b on ch.deleted = b.deleted")
    //이 경우 fetch 대신 프로젝션을 정확히 명시하여 조회하면 된다.
    @Query("select ch, b from CheckOut ch join Book b on ch.deleted = b.deleted")
    List<CheckOut> joinTest1();


    //이름 기반으로 쿼리를 만들거나, 기본 제공되는 메소드를 재정의하여 사용할 때
    //jpql 없이 @EntityGraph(attributePaths = "")을 사용하여 fetch join을 사용할 수 있다.
    //(spring data에서 지원하는 어노테이션.)
    //left join을 걸어주며, 다른 join을 걸고 싶다면 jpql 사용해야 함.
    //간간이 사용됨.
    @Override @EntityGraph(attributePaths = "book")
    List<CheckOut> findAll();

    /*
    이름 기반 : 거의 사용 안하지만 간단한 쿼리 만드는 데는 편하므로 간간이 사용
     JPQL : 일반 쿼리 작성할 때 사용
     QueryDSL : 동적쿼리 날릴 때 사용

     위 3가지를 다 사용하게 됨.
    * */


    @EntityGraph(attributePaths = {"book", "user"}) //항상 여러 속성값을 줄때는 중괄호 사용
    List<CheckOut> findByCreatedDateGreaterThan(LocalDateTime dateTime);

    //조인과 페이징처리
    //페이징 처리에서 반드시 필요한 쿼리는 total count 쿼리이다.
    //left join의 결과 total을 구할 때는 대부분의 상황에서 join이 필요없다.
    //이런 경우 count 쿼리에 join을 적용하면 성능 저하의 원인이 되므로 count는 따로 작성하는 게 좋다.
    //Spring Data JPA는 성능 최적화를 어느정도 도와주지만 쿼리를 확인했을 때 수정해야 한다면
    //다음과 같이 count쿼리를 별도로 설정할 수 있다.
    @Query(value="select ch from CheckOut ch left join fetch ch.book",
    countQuery = "select count(ch.id) from CheckOut ch")
    Page<CheckOut> joinTest3(Pageable pageable);

//    ========================================================================
    //jpql의 서브쿼리는 where,, having에서만 사용가능하고 하이버네이트를 사용하면
    //select 절에서도 사용가능하다.
    //from절 서브쿼리는 하이버네이트 6.1 이상부터 지원한다.
    @Query("select ch from CheckOut ch where ch.user.birth = (" +
            "select min(u.birth) from User u" +
            ")")
    List<CheckOut> sub1();

    // (IDE 컴파일러가 인식을 못해서 빨간 줄 그어짐. 정상적으로 작동되긴 함)
    @Query("select new Map(ch as check, (select 1) as number) from CheckOut ch where ch.id = 1")
    Map<String, Object> sub2();


    //(*버전이 낮아서 그런지 sub 쿼리가 잘 안됨. 쿼리 자체는 맞는 쿼리)
    @Query("""
        select s.id from (
            select ch.id as id from CheckOut ch where ch.id = 1
        ) s
    """)
    Optional<Long> sub3();

    //task1 : book 평균 가격보다 높은 책의 대여 기록을 조회한다.
    @Query("select ch from CheckOut ch " +
            "where ch.book.price > (select avg(b.price) from Book b)")
    List<CheckOut> sub4();


    //book 카테고리 별 가격이 가장 높은 책의 대여 기록을 조회한다.
    @Query("""
        select ch from CheckOut ch
        join fetch ch.book b
        where (b.price, b.category) in(select max(b.price), b.category from Book b group by b.category)
""")
    List<CheckOut> sub5();
}
