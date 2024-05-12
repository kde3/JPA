package com.example.data01.customRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryCustomImplTest {
    @Autowired
    BookRepo bookRepo;

    @Test
    void test() {
        // Impl만 붙이면 JPA가 알아서 구현부를 찾아 실행시켜준다.
        bookRepo.findCustomByName("test");
    }

}