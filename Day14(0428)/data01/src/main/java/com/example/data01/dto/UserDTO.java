package com.example.data01.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private LocalDate birth;

    public UserDTO(Long id, String name, LocalDate birth) {
        this.id = id;
        this.name = name;
        this.birth = birth;
    }
}
