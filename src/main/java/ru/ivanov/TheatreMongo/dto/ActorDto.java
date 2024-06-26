package ru.ivanov.theatremongo.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ActorDto {
    private String id;
    private String name;
    private String surname;
}
