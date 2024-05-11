package com.example.data01.dataRepository;

import com.example.data01.entity.Book;
import com.example.data01.entity.CheckOut;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional @Commit
class CheckOutDataRepositoryTest {
    @Autowired
    CheckOutDataRepository checkOutDataRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void findByBook() {
        Book book = em.find(Book.class, 1L);
        Optional<CheckOut> opt = checkOutDataRepository.findByBook(book);
        opt.ifPresent(System.out::println);
    }

    @Test
    void findByBookId() {
        Optional<CheckOut> opt = checkOutDataRepository.findByBookId(1L);
        opt.ifPresent(System.out::println);
    }

    @Test
    void findByBookName() {
        // 테이블에 없어도 연관관계가 설정되어있다면 알아서 join을 걸어서 가져와준다.
        // 다만 book에 대한 정보는 가져오지 않는다(Book 필드 정보는 가져오지X).
        // 따라서 영속화가 되지 않는다.
        Optional<CheckOut> opt = checkOutDataRepository.findByBookName("Greenlam");
        opt.ifPresent(System.out::println);

        // 연관관계를 맺은 엔티티의 id를 제외한 다른 필드로 조회를 하면 join을 사용한다.
        //join이 사용되었으나 Book 엔티티를 영속화한 것이 아니기 때문에
        // (조건을 걸기 위해 join을 한 것이기 때문)
        //book에 접근하면 추가 select 쿼리가 발생된다.
        System.out.println(opt.get().getBook());
        //select
        //        c1_0.check_out_id,
        //        c1_0.book_id,
        //        c1_0.created_date,
        //        c1_0.deleted,
        //        c1_0.modified_date,
        //        c1_0.user_id
        //    from
        //        tbl_check_out c1_0
        //    left join
        //        tbl_book b1_0
        //            on b1_0.book_id=c1_0.book_id
        //    where
        //        (
        //            c1_0.deleted = 0
        //        )
        //        and b1_0.name='Greenlam'
    }

    @Test
    void find() {
        //checkOut을 조회할 때 연관관계 엔티티들의 fetchType을 지연로딩으로 설정하였기 때문에
        // join이 걸리지 않는다.
        Optional<CheckOut> opt = checkOutDataRepository.findById(1L);

        //book 엔티티에 접근하면 추가 select 쿼리가 발생된다.
        opt.ifPresent(ch -> System.out.println(ch.getBook()));

        //checkOut만 조회해서 사용한다면 문제가 없지만, Book에 대한 정보도 같이 사용한다면
        //select가 2번 보다는 join 1번이 성능 측면에서 좋다.
        //그렇다고 즉시 로딩으로 설정한다면 checkOut만 사용하는 상황에서는 불필요한 join이
        // 발생하므로 지연로딩을 사용하면서 상황에 따라 직접 Join을 사용해주는 게 좋다.
    }

    @Test
    void join1() {
        CheckOut checkout = em.createQuery("select ch from CheckOut ch " +
                        "join ch.book where ch.id=1", CheckOut.class)
                .getSingleResult();

        //join을 직접 사용하여도 지연로딩을 설정했으므로 book엔티티는 프록시 객체로 설정된다.
        //즉, 사용시점에 다시 select 쿼리를 날리는 n+1문제 발생.
        //추가 쿼리 날라가는 거 막고 싶으면 fetch join을 써야 함.
        System.out.println(checkout.getBook());
    }

    @Test
    void fetchJoin1() {
        //위 예제의 n+1 문제를 해결하기 위해 fetch join 사용.
        //book에 대한 정보는 안 가져옴(book_id만 가져옴).
        //fetch join쓰면 book 정보까지 다 가져옴. 따라서 쿼리가 한번만 날라감
        CheckOut checkOut = em.createQuery("select ch from CheckOut ch " +
                        "join fetch ch.book where ch.id=1", CheckOut.class)
                .getSingleResult();

        System.out.println(checkOut);
    }

    @Test
    void findByJoinBook(){
        checkOutDataRepository.findByJoinBook();
    }

    @Test
    void findByFetchJoinBook() {
        checkOutDataRepository.findByFetchJoinBook();
    }

    @Test
    void findByJoinBookAndUser() {
        checkOutDataRepository.findByJoinBookAndUser();
    }

    @Test
    void joinTest1() {
        checkOutDataRepository.joinTest1();
    }

    @Test
    void findAll() {
        checkOutDataRepository.findAll();
    }

    @Test
    void findByCreatedDateGreaterThen() {
        checkOutDataRepository.findByCreatedDateGreaterThan(LocalDateTime.of(2000, 1,1,12,30));
    }

    @Test
    void joinTest3() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        checkOutDataRepository.joinTest3(pageRequest);
    }

    @Test
    void sub1() {
        checkOutDataRepository.sub1();
    }

    @Test
    void sub2() {
        checkOutDataRepository.sub2();
    }

    @Test
    void sub3() {
        checkOutDataRepository.sub3();
    }

    @Test
    void sub4() {
        List<CheckOut> list = checkOutDataRepository.sub4();
        System.out.println(list);

        list.stream().map(CheckOut::getBook).forEach(System.out::println);
    }

    @Test
    void sub5() {
        List<CheckOut> list = checkOutDataRepository.sub5();
        System.out.println(list);
    }
}