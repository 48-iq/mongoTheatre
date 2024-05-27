package ru.ivanov.theatremongo.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.ActorDto;
import ru.ivanov.theatremongo.model.Actor;
import ru.ivanov.theatremongo.model.Performance;
import ru.ivanov.theatremongo.security.MongoTemplateProvider;



@RequiredArgsConstructor
@Service
public class ActorService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;

    public ActorDto getActorById(String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Actor actor = mongoTemplate.findById(actorId, Actor.class);
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
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance == null)
            throw new IllegalArgumentException("such performance doesn't exist");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(performance.getActors()));
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
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(actorId));
        mongoTemplate.remove(query, Actor.class);
    }

    public List<ActorDto> getActorsWithoutPerformance(String performanceId) {
        List<ActorDto> performanceActors = getPerformanceActors(performanceId);
        List<ActorDto> allActors = new ArrayList<>(getAllActors());
        allActors.removeAll(performanceActors);
        return allActors;
    }
}
