package com.example.finalapp.domain.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter @ToString
@MappedSuperclass       //단순 상속은 이 어노테이션은 달아준다.
@EntityListeners(AuditingEntityListener.class)  //CreatedDate과 LastModifiedDate을 사용하기 위함.
//@EnableJpaAuditing도 application에 달아줘야 작동하게 됨.
public class Period {
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
