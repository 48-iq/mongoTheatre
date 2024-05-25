package ru.ivanov.TheatreMongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Ticket {
    @Id
    private String id;
    private Integer row;
    private Integer place;
    private Integer price;
}
