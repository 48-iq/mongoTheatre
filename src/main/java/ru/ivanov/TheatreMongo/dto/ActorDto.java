package ru.ivanov.TheatreMongo.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ActorDto {
    private String id;
    private String name;
    private String surname;
}
