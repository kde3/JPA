package com.example.data01.customRepository;

import com.example.data01.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository/*("BookRepo")*/     //다른 인터페이스와 이름이 겹칠 때 등록을 할 수 있다.
public interface BookRepo extends JpaRepository<Book, Long>, BookRepositoryCustom {     // 인터페이스 간 extends
    //사용자 정의 레파지토리를 공통 인터페이스를 상속받는 일반 레파지토리에 같이 상속처리 해준다.
    //BookRepositoryCustom의 Impl이 붙은 커스텀 클래스를 찾아가서 같이 상속시켜준다.
}
