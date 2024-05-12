package com.example.data01.dto;

import com.example.data01.type.BookCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private BookCategory category;
    private String name;
    private Integer price;
    private LocalDate publishDate;

    public BookDTO(Long id, BookCategory category, String name, Integer price, LocalDate publishDate) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.publishDate = publishDate;
    }
}









