package com.example.data01.entity;

import com.example.data01.base.Period;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity @Table(name = "tbl_check_out")
@Getter @Setter @ToString(exclude = {"book", "user"})
// 데이터를 실제 DB상에서 삭제하는것은 hard delete
// 상태칼럼을 활용하는 것을 soft delete라고 한다.
// 엔티티 매니저의 remove()를 실행했을 때 delete대신 날릴 쿼리를 재정의하는 어노테이션
// ?의 위치에는 해당 엔티티의 ID(식별자) 들어간다.
@SQLDelete(sql = "update tbl_check_out set deleted = 1 where check_out_id = ?")
// delete쿼리를 대신하여 update를 실행하므로 나중에 전체 정보를 조회할 때 삭제처리된
// 데이터까지 같이 조회될 수 있다. 그러므로 where에 deleted = 0 인 조건이 항상 추가되어야한다.
// 우리가 직접 추가한다면 불편하므로 @Where를 활용하여 해결한다.
@Where(clause = "deleted = 0")
@SequenceGenerator(name = "check_out_generator",
        sequenceName = "SEQ_CHECK_OUT",
        allocationSize = 1)
public class CheckOut extends Period {
    @Id @GeneratedValue(generator = "check_out_generator")
    @Column(name = "check_out_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;
    private boolean deleted = false;

//    편의 메서드
    public void setBook(Book book) {
        this.book = book;
        book.setCheckOut(this);
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getCheckOutList().remove(this);
        }
        this.user = user;
        user.getCheckOutList().add(this);
    }
}













