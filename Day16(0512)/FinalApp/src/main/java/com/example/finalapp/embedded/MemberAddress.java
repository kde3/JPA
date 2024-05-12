package com.example.finalapp.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @ToString @EqualsAndHashCode
@NoArgsConstructor      // 필수
@AllArgsConstructor     //초기화 목적
public class MemberAddress {
    private String address;
    private String addressDetail;
    private String zipcode;
}
