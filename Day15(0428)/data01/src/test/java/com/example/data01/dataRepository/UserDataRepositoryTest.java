package com.example.data01.dataRepository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class UserDataRepositoryTest {
    @Autowired
    UserDataRepository userDataRepository;

    @BeforeEach
    void setUp() {

    }
}