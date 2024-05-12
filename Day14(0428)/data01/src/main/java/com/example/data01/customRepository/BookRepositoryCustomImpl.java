package com.example.data01.customRepository;

import com.example.data01.entity.Book;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

//인터페이스 명과 똑같이 짓고 뒤에 'Impl' 붙이기
//ctrl + o : 오버라이드 목록 뜸
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    //사용자 정의 Repository를 구현하는 클래스다.
    //구현 클래스는 반드시 인터페이스 이름 + impl이라는 키워드를 클래스 이름으로 사용한다.
    //(*yml에서 설정해서 Impl 이라는 키워드 대신 다른 걸 이용할 수도 있지만 거의 그러진 않는다.)
    //구현 클래스에서 엔티티 매니저를 이용한 jpql을 직접 짤 수 있다.
    //jpql 말고도 QueryDSL이나 JDBC 등을 사용하여 코드를 작성할 수 있다.
    private final EntityManager em;

    @Override
    public List<Book> findCustomByName(String name) {
        return em.createQuery("select b from Book b where b.name = :name", Book.class)
                .setParameter("name", name)
                .getResultList();
    }
}
