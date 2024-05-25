package ru.ivanov.TheatreMongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "actors")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Actor {
    @Id
    private String id;
    private String name;
    private String surname;
}
