package com.example.data01.dataRepository;

import com.example.data01.entity.Book;
import com.example.data01.type.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
Spring Data JPA
스프링에서 JPA를 편하게 사용하도록 지원하는 기술이다.
어플리케이션 개발 시 대부분의 table에 해당하는 CRUD 기본기능을 구현해야한다.
즉, 기본적인 등록, 조회, 수정, 삭제 기능을 반복적으로 생성하게 되는데 JPA를 사용하여도
이러한 반복적인 코드가 해결되지 않는다.
스프링 데이터 JPA는 인터페이스를 생성하고 추상메소드를 만들기만 하여도 자동으로 JPA구현 객체를 생성해
주입해준다.
메소드 이름을 기반으로 적절한 JPQL쿼리를 생성하고 실행시켜주므로 반복적인 CRUD작업을
더 편하게 처리할 수 있다.
 */
@Repository
public interface BookDataRepository extends JpaRepository<Book, Long> {
//    Spring Data JPA는 쿼리 메소드를 지원해준다.
//    1. 쿼리 메소드는 기본적으로 메소드 이름을 기반으로 쿼리를 생성해준다.
//    2. 메소드 이름에 특정 키워드를 사용해야한다.

//    조회
//    조회는 find[식별자]By 를 사용한다.
//    findBy는 기본적인 select from 쿼리를 생성해준다.
//    By앞에 붙는 [식별자]는 이 쿼리를 설명하는 내용이나 식별하기 위한 이름을 작성하면된다.(생략가능)
    List<Book> findBy();
    List<Book> findTestBy();
    List<Book> findBookBy();

//    조건을 추가하고 싶다면 By뒤에 조건으로 사용할 필드를 작성한다.
//    find[식별자]By[필드]
//    해당 필드로 equal(=) 조건을 추가해준다. [where 필드와_매핑된_칼럼 = 값]
    List<Book> findByName(String name);
    List<Book> findByPrice(int price);

//    * 반환타입
//    쿼리 메소드는 다양한 반환타입을 설정할 수 있다.
//    entity, List, Optional 타입을 자주 사용한다.
//    옵셔널, 엔티티를 반환타입으로 설정하는경우 조회결과가 2건 이상이면 오류 발생됨(주의!)
//    조회 결과가 0건이면 null이 반환된다.
    Book findEntityByName(String name);
    Optional<Book> findOptionalByName(String name);
    
//    =======================================================================
//    * 조건 설정
//    equal(=) 조건이 아닌 다른 조건도 사용이 가능하다.
//    findBy필드[조건설정]

//    1. 비교 연산자 <, > , <=, >= != 설정하기

//    GreaterThan : 초과 조건
//    LessThan : 미만 조건
    List<Book> findByPriceGreaterThan(int price);
    List<Book> findByPriceLessThan(int price);
    
//    GreaterThanEqual : 이상 조건
//    LessThanEqual : 이하 조건
//    날짜 비교 조건도 가능하다.(권장하지 않음)
    List<Book> findByPublishDateGreaterThanEqual(LocalDate publishDate);
    List<Book> findByPublishDateLessThanEqual(LocalDate publishDate);
    
//    After : 초과 조건
//    Before : 미만 조건
//    GreaterThan, LessThan 과 동일한 쿼리가 생성된다.
//    After, Before는 의미적으로 날짜 비교에 사용하는것을 권장한다.
    List<Book> findByPublishDateBefore(LocalDate publishDate);

//    2. like 문자열 조건 생성하기
//        Like : like 조건을 생성한다. -> like 'keyword'
    List<Book> findByNameLike(String keyword);
//    NotLike : not like 조건을 생성한다. -> not like 'keyword'
    List<Book> findByNameNotLike(String keyword);
//    Containing : like를 이용해 포함 조건을 생성한다. -> like '%keyword%'
    List<Book> findByNameContaining(String keyword);

//    StartWith : like를 이용해 시작 글자 조건을 생성한다. -> like 'keyword%'
//    EndWith : like를 이용해 마지막 글자 조건을 생성한다. -> like '%keyword'
    List<Book> findByNameStartingWith(String keyword);
    List<Book> findByNameStartsWith(String keyword);

//    3. Null 조건 설정하기
//    IsNull, IsNotNull
    List<Book> findByPriceIsNull();
    List<Book> findByPriceIsNotNull();

//    4. SQL 연산자 조건 설정
//    Between : between A and B
    List<Book> findByPriceBetween(int a, int b);

//    In : in(a,b,c,d......)
    List<Book> findByNameIn(List<String> names);
    List<Book> findByNameIn(Set<String> names);
    List<Book> findCollectionByNameIn(Collection<String> names);

//    NotIn : not in(a,b,c,. ...)
    List<Book> findByNameNotIn(Collection<String> names);

//    5. 논리연산자를 사용한 여러 조건 설정
//    And, Or
//    findBy필드명[조건]And필드명[조건]
//    조건을 생략하면 모든 조건은 equal로 설정된다.

    List<Book> findByNameAndPrice(String name, int price);
//    매개변수가 2개 이상인경우 메소드 이름의 순서와 매개변수 순서가 동일해야한다.
//    매개변수 이름은 상관없다.

    List<Book> findByNameContainingAndPriceGreaterThan(String name, int price);

//    And조건 추가 제한은 없으나 이름이 너무 길어진다.
    List<Book> findByNameAndPriceLessThanAndPublishDateAfter(String name, int price, LocalDate publishDate);

//    ===================================================
//    * 기타 키워드

//    1. 정렬하기
//    OrderBy필드명[Desc]
//    항상 메소드 이름의 마지막에 사용한다.
    List<Book> findByPriceGreaterThanOrderById(int price);
    List<Book> findByPriceGreaterThanOrderByIdDesc(int price);
    
//    2. 대소문자 무시하기
//    findBy필드명[조건]IgnoreCase -> where upper(컬럼) = upper('keyword')
    List<Book> findByNameIgnoreCase(String keyword);

//    3. 중복제거
//    findDistinct[식별자]By
    List<Book> findDistinctByPrice(int price);

//    4. 상위 결과 가져오기 (Rank)
//    findTop<number>[식별자]By -> 조회 결과의 상위 <number>개를 가져온다.
    List<Book> findTop3ByName(String name);

//    ==============================================================
//    * find 외의 키워드
//    1. count[식별자]By[필드][조건]
//    반환타입은 정수로 설정하면 된다.
    int countBy();
    Long countByPublishDateBefore(LocalDate publishDate);

//    2. exists[식별자]By[필드][조건] : 조회 결과 유/무
    boolean existsBy();
    Boolean existsByCategory(BookCategory bookCategory);
    
//    3. delete[식별자]By[필드][조건] : 조건으로 여러 행 삭제
    void deleteByPublishDateAfter(LocalDate publishDate);
}











