package ru.ivanov.theatremongo.service;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.ActorDto;
import ru.ivanov.theatremongo.model.Actor;
import ru.ivanov.theatremongo.security.MongoTemplateProvider;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActorService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;

    public ActorDto getActorById(String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", actorId);
        Actor actor = mongoTemplate.findOne(query, Actor.class);
        return modelMapper.map(actor, ActorDto.class);
    }

    public List<ActorDto> getAllActors() {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        return mongoTemplate.findAll(Actor.class).stream()
                .map(actor -> modelMapper.map(actor, ActorDto.class))
                .toList();
    }

    public List<ActorDto> getPerformanceActors(String performanceId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{performanceId: ?0}", performanceId);
        return mongoTemplate.find(query, Actor.class).stream()
                .map(actor -> modelMapper.map(actor, ActorDto.class))
                .toList();
    }

    public ActorDto saveActor(ActorDto actorDto) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Actor actor = modelMapper.map(actorDto, Actor.class);
        actor = mongoTemplate.save(actor);
        return modelMapper.map(actor, ActorDto.class);
    }

    public void deleteActor(String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", actorId);
        mongoTemplate.remove(query, Actor.class);
    }
}
