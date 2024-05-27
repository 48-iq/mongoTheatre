package ru.ivanov.theatremongo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NameDto {
    private String name;
    public String toString() {
        return name;
    }
}
