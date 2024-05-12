package com.example.data01.entity;


import com.example.data01.base.Period;
import com.example.data01.embedded.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_user")
@Getter @Setter @ToString
@SQLDelete(sql = "update tbl_user set deleted = 1 where user_id = ?")
@Where(clause = "deleted = 0")
@SequenceGenerator(name = "user_generator",
        sequenceName = "SEQ_USER",
        allocationSize = 1)
public class User extends Period {
    @Id @GeneratedValue(generator = "user_generator")
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String phone;
    private LocalDate birth;
    @Embedded
    private Address address;
    private boolean deleted = false;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<CheckOut> checkOutList = new ArrayList<CheckOut>();
}
