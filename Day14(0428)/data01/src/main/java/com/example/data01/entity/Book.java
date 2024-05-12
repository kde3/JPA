package com.example.data01.entity;

import com.example.data01.base.Period;
import com.example.data01.type.BookCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_book")
@Getter
@Setter
@ToString
@SQLDelete(sql = "update tbl_book set deleted = 1 where book_id = ?")
@Where(clause = "deleted = 0")
@SequenceGenerator(name = "book_generator",
        sequenceName = "SEQ_BOOK",
        allocationSize = 1)
@Builder                //빌더 생성해줌(추가하는 동시에 AllArgsConstructor를 만듦)
@NoArgsConstructor      // 그렇다고 NoArgs를 생성하면 AllArgs가 사라짐.
@AllArgsConstructor     // 따라서 둘다 달아줘야 함.
public class Book extends Period {
    @Id
    @GeneratedValue(generator = "book_generator")
    @Column(name = "book_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private BookCategory category;
    private String name;
    private Integer price;
    private LocalDate publishDate;
    private boolean deleted = false;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "book")
    private CheckOut checkOut;
}
