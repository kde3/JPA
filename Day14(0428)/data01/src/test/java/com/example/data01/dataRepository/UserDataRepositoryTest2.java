package com.example.data01.dataRepository;

import com.example.data01.embedded.Address;
import com.example.data01.entity.User;
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
class UserDataRepositoryTest2 {
    @Autowired
    UserDataRepository userDataRepository;

    @BeforeEach
    void setUp() {
        Address address1 = new Address("강남구", "101호", "11111");
        Address address2 = new Address("송파구", "202호", "22222");
        Address address3 = new Address("노원구", "303호", "33333");


        User user1 = new User();
        user1.setName("뽀로로");
        user1.setBirth(LocalDate.of(2000, 1, 1));
        user1.setPhone("01011111111");
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

        userDataRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void findByPhone() {
        Optional<User> opt = userDataRepository.findByPhone("01011111111");
        opt.ifPresent(System.out::println);
    }

    @Test
    void findByBirthBefore() {
        List<User> list = userDataRepository.findByBirthBefore(LocalDate.of(2010, 1, 1));
        System.out.println("list = " + list);
    }

    @Test
    void findByNameContaining() {
        userDataRepository.findByNameContaining("뽀")
                .forEach(System.out::println);
    }

    @Test
    void countByNameContaining() {
        Long count = userDataRepository.countByNameContaining("뽀");
        System.out.println("count = " + count);
    }

    @Test
    void existsByPhone() {
        boolean isTrue = userDataRepository.existsByPhone("01022222222");
        System.out.println("isTrue = " + isTrue);
    }

    @Test
    void findByNameContainingAndPhoneContainingOrderByIdDesc() {
        userDataRepository.findByNameContainingAndPhoneContainingOrderByIdDesc("루", "22")
                .forEach(System.out::println);
    }

    @Test
    void findByNameIn() {
        userDataRepository.findByNameIn(List.of("뽀로로", "크롱"))
                .forEach(System.out::println);
    }

    @Test
    void findByBirthIsNotNullAndIdBetween() {
        userDataRepository.findByBirthIsNotNullAndIdBetween(2L, 3L)
                .forEach(System.out::println);
    }
}