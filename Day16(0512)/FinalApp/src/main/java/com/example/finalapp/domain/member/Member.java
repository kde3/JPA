package com.example.finalapp.domain.member;

import com.example.finalapp.domain.base.Period;
import com.example.finalapp.embedded.MemberAddress;
import com.example.finalapp.type.MemberGender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity @Table(name = "tbl_member")
@Getter @ToString                   //setter는 웬만하면 entity에 만들지X
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert      // 동적으로 insert 쿼리를 만듦(제외된 컬럼 있으면 동적으로 쿼리 만들어라)
@AllArgsConstructor @Builder    //allargs를 따로 쓸 거면 어노테이션 말고 생성자 따로 생성하는게 나음
                                // (지금은 빌더만 쓸거라 걍 어노테이션 씀)
public class Member extends Period {
    @Id @Column(name = "member_id")
    @GeneratedValue
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private LocalDate birth;
    private String phoneNumber;
    @Enumerated(EnumType.STRING) @ColumnDefault("'NONE'")   // 문자열은 작은 따옴표 붙여야 함.
    private MemberGender gender;
    @Embedded
    private MemberAddress memberAddress;
}
