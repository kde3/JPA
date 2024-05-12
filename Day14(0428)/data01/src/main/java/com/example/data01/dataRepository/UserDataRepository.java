package com.example.data01.dataRepository;

import com.example.data01.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<User, Long> {
    //    핸드폰 번호로 조회
    Optional<User> findByPhone(String phoneNumber);

    //    생일이 특정 날짜보다 이전인 회원 조회
    List<User> findByBirthBefore(LocalDate birthDate);

    //    이름에 특정 글자가 포함되는 회원 조회
    List<User> findByNameContaining(String keyword);

    //    이름에 특정 글자가 포함되는 회원 수를 조회
    Long countByNameContaining(String keyword);

    //    특정 핸드폰 번호가 존재하는지 검사
    boolean existsByPhone(String phoneNumber);

    //    이름에 특정 글자가 포함되고 핸드폰에 특정 글자가 포함되는 회원들 조회(ID를 내림차순으로 정렬)
    List<User> findByNameContainingAndPhoneContainingOrderByIdDesc(String nameKeyword, String phoneKeyword);

    //    여러 이름을 넘겨받아 해당 이름과 일치하는 회원 정보 조회
    List<User> findByNameIn(Collection<String> names);

    //    birth가 null이 아니고 id가 특정 범위안에 속하는 모든 회원 조회
    List<User> findByBirthIsNotNullAndIdBetween(Long startId, Long endId);

}
