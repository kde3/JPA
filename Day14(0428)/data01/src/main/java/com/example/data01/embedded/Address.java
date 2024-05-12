package com.example.data01.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @ToString @Setter
@NoArgsConstructor @AllArgsConstructor
public class Address {
    private String address;
    private String addressDetail;
    private String zipcode;
}
