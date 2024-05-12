package com.example.data01.customRepository;

import com.example.data01.entity.Book;

import java.util.List;

public interface BookRepositoryCustom {
    //사용자 정의 Repository를 만들기 위한 인터페이스(사용자 정의 인터페이스)
    //일반적인 인터페이스처럼 껍데기만 만든다.
    List<Book> findCustomByName(String name);
}
