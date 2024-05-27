package ru.ivanov.theatremongo.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.PerformanceDto;
import ru.ivanov.theatremongo.model.Performance;
import ru.ivanov.theatremongo.security.MongoTemplateProvider;


@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;
    private final ActorService actorService;

    public PerformanceDto getPerformanceById(String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(actorId, Performance.class);
        if (performance == null)
            throw new IllegalArgumentException("such performance doesn't exist");
        if (performance.getTicketsList() == null)
            performance.setTicketsList(new ArrayList<>());
        if (performance.getActors() == null)
            performance.setActors(new ArrayList<>());
        return modelMapper.map(performance, PerformanceDto.class);
    }

    public List<PerformanceDto> getAllPerformances() {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        return mongoTemplate.findAll(Performance.class).stream()
                .map(performance -> modelMapper.map(performance, PerformanceDto.class))
                .toList();
    }

    public PerformanceDto savePerformance(PerformanceDto performanceDto) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = modelMapper.map(performanceDto, Performance.class);
        performance = mongoTemplate.save(performance);
        return modelMapper.map(performance, PerformanceDto.class);
    }

    public void deletePerformance(String performanceId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(performanceId));
        mongoTemplate.remove(query, Performance.class);
    }

    public void addActorToPerformance(String performanceId, String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance.getActors() == null)
            performance.setActors(new ArrayList<>());
        else 
            performance.setActors(new ArrayList<>(performance.getActors()));
        if (!performance.getActors().contains(actorId))
            performance.getActors().add(actorId);
        mongoTemplate.save(performance);
    }

    public void removeActorFromPerformance(String performanceId, String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance.getActors() == null)
            performance.setActors(new ArrayList<>());
        else 
            performance.setActors(new ArrayList<>(performance.getActors()));
        if (performance.getActors().contains(actorId))
            performance.getActors().remove(actorId);
        mongoTemplate.save(performance);
    }

    public List<PerformanceDto> getPerformancesByName(String name) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(name));
        return mongoTemplate.find(query, Performance.class).stream()
                .map(performance -> modelMapper.map(performance, PerformanceDto.class))
                .toList();
    }
}
