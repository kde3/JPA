package com.example.data01.problem;

import com.example.data01.dataRepository.BookDataRepository;
import com.example.data01.dataRepository.UserDataRepository;
import com.example.data01.dataRepository.UserQueryRepository;
import com.example.data01.embedded.Address;
import com.example.data01.entity.Book;
import com.example.data01.entity.CheckOut;
import com.example.data01.entity.User;
import com.example.data01.repository.BookRepository;
import com.example.data01.repository.CheckOutRepository;
import com.example.data01.repository.UserRepository;
import com.example.data01.type.BookCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Commit
public class ProblemTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckOutRepository checkOutRepository;

    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    BookDataRepository bookDataRepository;

    @Autowired
    UserQueryRepository userQueryRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
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
    }

    @Test
    void oneToOne(){
        em.flush();
        em.clear();

//        bookDataRepository.findAll();
//        bookDataRepository.findByPriceGreaterThan(1_000);
//        userDataRepository.findAll();

//        book 엔티티를 조회할 때 여러건을 조회하면
//        조회된 엔티티의 id를 통해 단건 추가 쿼리가 발생하는것을 볼 수 있다.
//        우리는 분명 Book엔티티의 checkOut필드를 지연로딩으로 처리하였지만 자동으로
//        select 쿼리가 발생된다.

//        엔티티 매니저를 직접 사용해도 동일한 문제가 발생된다.
//        Book book = em.find(Book.class, 1L);

//        이 문제는 OneToOne 양방향 관계에서 발생하는 N+1문제이다.

//        checkOut은 정상적으로 지연로딩이 적용되고 있다.
//        CheckOut checkOut = em.find(CheckOut.class, 1L);

//        이유가 뭘까?
//        1. 테이블 관계                             2. 객체 관계
//        TBL_BOOK          TBL_CHECK_OUT           Book            CheckOut
//        ID                ID                      id              id
//        NAME              BOARD_ID(FK)            name            book(*)
//        PHONE             USER_ID(FK)             phone           user(*)
//        BIRTH                                     birth
//        ADDRESS                                   address
//        ADDRESS_DETAIL                            checkout(*)`
//        ZIPCODE

//        위의 관계에서 TBL_CHECK_OUT은 BOOK_ID를 FK칼럼으로 가지고 있다.
//        즉, TBL_CHECK_OUT를 조회할 때 연관관계가 있다면 checkOut엔티티의 book필드에
//        book 프록시 객체가 들어가고(지연로딩) 연관관계가 없다면 null이 저장된다.

//        그런데 반대로 TBL_BOARD 테이블은 FK를 가지고 있지 않다.
//        즉, TBL_BOARD 테이블만 조회해서는 Board엔티티의 checkOut에 null이 들어가야할 지
//        프록시 객체가 들어갈지 알 수 없다.
//        그러므로 연관관계 유무를 알기 위해서는 TBL_CHECK_OUT을 조회할 수 밖에 없다.

//        정리하자면 OneToOne 양방향 관계에서 관계의 주인이 아닌 엔티티로 조회를 하면
//        구조 불일치로 인해 N+1문제가 발생된다.

//        이런 N+1문제는 단건 조회에서는 크게 문제되지 않을 수 있으나 findAll()처럼
//        여러건 조회를 하게되면 심각한 성능저하 원인이 될 수 있다.

//        어떻게 해결할까?
//        명쾌한 해답은 없다.
//        1. 꼭 필요한게 아니라면 OneToOne은 단방향으로 사용한다.
//        2. OneToMany, ManyToOne 양방향 관계로 변경한다.(OneToOne이라는 논리적인 관계를 포기)
//        3. 직접적인 연관관계를 끊고 서로를 확인할 수 있는 key를 칼럼으로 추가하여
//            직접 join을 걸어 사용한다.(jpa의 연관관계 그래프 탐색 포기)
//        4. 어쩔 수 없으니 fetch 조인 사용해서 쿼리 수라도 줄여본다.

//        체크아웃은 book필드에 프록시 객체를 저장했기 때문에 book필드 사용시
//        추가 쿼리가 발생됨 (레이지 로딩)
//        List<CheckOut> checkOutList = checkOutRepository.findAll();
//        System.out.println(checkOutList.get(0).getBook());

//        북은 checkout필드에 실제 엔티티를 저장했기 때문에 checkout필드 사용시
//        추가 쿼리는 발생 안된다.
        List<Book> list = bookDataRepository.findAll();
        System.out.println(list.get(0).getCheckOut());
    }

    @Test
    void embedded(){
//       쿼리 메소드의 반환 타입을 Embedded로 설정할 수 있다.
        List<Address> addressList = userQueryRepository.findAddress();

//        임베디드 타입은 엔티티와는 다르게 변경감지가 일어나지 않는다.
//        엔티티 타입만 영속화가 가능하기 때문이다.
        addressList.get(0).setAddress("수정!");

//        변경감지를 통해 수정하고 싶다면 엔티티를 가져와야한다.
        Optional<User> opt = userDataRepository.findById(1L);
        opt.ifPresent(user -> user.setAddress(new Address("수정", "한다", "1234")));
    }

    @Test
    void jpqlAndFlush(){
        em.flush();
        em.clear();
//        아래 쿼리들의 실행 순서를 확인해보자
//        1. find
        Optional<User> opt = userDataRepository.findById(1L);
//        2. set
        opt.ifPresent(user -> user.setName("수정했다!!!"));
//        3. find
        Optional<User> opt2 = userDataRepository.findById(2L);

//        결과는 select -> select -> update
//        set을 통해 변경감지 update쿼리가 실행되는 시점은 flush가 발생하는 시점이다.
//        즉, 메소드가 종료되어 commit되기 직전에 실행된다.
    }

    @Test
    void jpqlAndFlush2(){
        em.flush();
        em.clear();
//        아래 쿼리들의 실행 순서를 확인해보자.
//        1. find
        Optional<User> opt = userDataRepository.findById(1L);
//        2. set
        opt.ifPresent(user -> user.setName("수정했다!!"));
//        3. JPQL로 select
        User foundUser = em.createQuery("select u from User u where u.id = 2", User.class)
                .getSingleResult();

//      결과는 select -> update -> select

//        JPQL의 특징
//        엔티티 매니저의 find()를 실행시키는 경우 우선 영속성 컨텍스트에 조회하려는 ID를 가진
//        엔티티가 있는지 먼저 검사한다. 없다면 DB에 select 쿼리를 날린다.
//        JPQL을 사용하는 경우 쿼리를 생성하여 DB로 바로 날린다.
//        그런데 이때 문제가 생길 수 있다.
//        만약 직전 코드에서 데이터를 수정하기 위해 set을 사용했다면 DB에는 아직 반영되지 않은
//        상태일 것이다.
//        즉, 수정되기 전의 엔티티가 조회된다. 이렇게 되면 로직에 큰 문제가 발생할 수 있으므로
//        JPQL로 쿼리를 날리기 전에 flush를 먼저 실행하고 jpql로 생성된 쿼리를 날린다.

//        그러면 변경감지를 통해 수정된 데이터가 DB에 반영된 후
//        JPQL로 만든 select쿼리가 실행될 것이다.

    }

    @Test
    void jpqlAndFlush3(){
        em.flush();
        em.clear();

//        엔티티 매니저에는 플러쉬모드를 설정하는 기능이 있다.
//        FlushModeType.AUTO가 디폴트이며, 커밋/jpql 사용 시 자동으로 플러쉬 된다.
//        FlushModeType.COMMIT 은 커밋 시점에만 자동으로 플러시가 된다.
        em.setFlushMode(FlushModeType.COMMIT);

//        아래 쿼리들의 실행 순서를 확인해보자.
//        1. find
        Optional<User> opt = userDataRepository.findById(1L);
//        2. set
        opt.ifPresent(user -> user.setName("수정했다!!"));
//        3. jpql을 사용한 select
        List<User> userList = em.createQuery("select u from User u where u.id < 3", User.class)
                .getResultList();

//        결과는 select -> select -> update
//        jpql을 사용했지만 플러시모드를 COMMIT으로 설정하였기 때문에 update가 마지막에 실행

//        그렇다면 조회가 모두 끝난뒤 update가 실행된 것인데
//        list에 담긴 1번 회원의 정보는 수정이 되어있을까?
        System.out.println("userList = " + userList);

//        분명 update 가 마지막에 실행되었는데 update이전에 list에 담긴 1번 회원 정보는
//        수정이 되어있다.
//        jpql은 쿼리를 DB로 바로 전송하지만 조회결과를 가져왔을 때는 영속성 컨텍스트에 동일한
//        ID를 가진 엔티티가 존재하는지 검사한다.
//        만약 동일한 ID를 가진 엔티티가 이미 존재한다면 어떻게 처리할까?

//        1. 두 엔티티를 모두 영속성 컨텍스트에 저장한다.
//            -> 불가능하다. 같은 ID를 가진 엔티티가 존재해서는 안된다.
//        2. 기존의 영속성 컨텍스트에 저장된 엔티티를 삭제하고 새로 가져온 엔티티를 등록한다.
//            -> 가능한 방법이지만 엔티티의 동일성 보장이 깨진다.
//        3. 기존 영속성 컨텍스트에 저장된 엔티티를 유지하고 새로 조회한 엔티티를 버린다.
//            -> 이 방식으로 작동한다.
//                엔티티가 영속 컨텍스트에 존재한다는 것은 이미 사용중인 엔티티라는 의미이다.
//                해당 엔티티와 다시 조회한 결과에서 동일성을 보장하기 위해서는 이 방법을 사용해야한다.

        System.out.println("동일성을 보장하는가? : " + ( opt.get() == userList.get(0) ));

    }

}











