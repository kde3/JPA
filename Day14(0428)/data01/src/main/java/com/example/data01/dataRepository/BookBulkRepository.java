package com.example.data01.dataRepository;

import com.example.data01.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBulkRepository extends JpaRepository<Book, Long> {

    @Modifying // @Modifying을 사용해야 executeUpdate()로 실행된다.
//  @Query 을 보조하는 역할이므로 같이 사용해야한다.
    @Query("update Book b set b.name = '수정완료!!' where b.id < 3")
    void updateBook();

//    자동 flush, 자동 clear 설정
//    매핑된 jpql이 실행되기 전에 flush와 clear가 실행된다.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Book b set b.name = '수정완료!!', b.modifiedDate = current_timestamp where b.id < :id")
    void updateBook2(@Param("id") Long id);
}












