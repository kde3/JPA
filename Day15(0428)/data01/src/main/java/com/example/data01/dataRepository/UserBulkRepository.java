package com.example.data01.dataRepository;

import com.example.data01.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBulkRepository extends JpaRepository<User, Long> {
//    주소에 '강남구'가 포함된 회원의 이름을 '기존이름' + '/강남' 으로 수정하기 -> 뽀로로/강남
    @Modifying
    @Query("""
    update User u 
    set u.name = concat(u.name, '/강남'), u.modifiedDate = current_timestamp
    where u.address.address like '%강남구%'
    """)
    void update1();

//    주소가 '강남구'가 포함되지 않은 회원의 이름을 '기존이름' + '/주소' 로 변경하기
//    루피/서울특별시 강북구
    @Modifying
    @Query("""
        update User u
        set u.name = concat(u.name, '/', u.address.address),
            u.modifiedDate = current_timestamp
        where u.address.address not like '%강남구%' 
    """)
    void update2();

}













