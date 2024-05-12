package com.example.data01.dataRepository;

import com.example.data01.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class UserBulkRepositoryTest {
    @Autowired
    UserBulkRepository userBulkRepository;

    @Test
    void update1() {
        userBulkRepository.update1();
        List<User> list = userBulkRepository.findAll();

        list.stream()
                .filter(user -> user.getAddress().getAddress().equals("강남구"))
                .forEach(System.out::println);
    }

    @Test
    void update2(){
        userBulkRepository.update2();
        List<User> list = userBulkRepository.findAll();
        list.stream()
                .filter(user -> !user.getAddress().getAddress().equals("강남구"))
                .forEach(user -> System.out.println(user.getName()));
    }
}







