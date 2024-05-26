package ru.ivanov.theatremongo.service;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.ActorDto;
import ru.ivanov.theatremongo.dto.PerformanceDto;
import ru.ivanov.theatremongo.model.Actor;
import ru.ivanov.theatremongo.model.Performance;
import ru.ivanov.theatremongo.security.MongoTemplateProvider;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;
    private final ActorService actorService;

    private Performance dtoToPerformance(PerformanceDto performanceDto) {
        return Performance.builder()
                .id(performanceDto.getId())
                .name(performanceDto.getName())
                .date(performanceDto.getDate())
                .description(performanceDto.getDescription())
                .ticketsList(performanceDto.getTicketsList())
                .actors(new ArrayList<>(performanceDto.getActors().keySet()))
                .build();
    }

    private PerformanceDto performanceToDto(Performance performance) {
        PerformanceDto performanceDto = PerformanceDto.builder()
                .id(performance.getId())
                .name(performance.getName())
                .date(performance.getDate())
                .description(performance.getDescription())
                .ticketsList(performance.getTicketsList())
                .actors(new HashMap<>())
                .build();
        for (String performanceActorId: performance.getActors()) {
            ActorDto actorDto = actorService.getActorById(performanceActorId);
            String actorFullName = actorDto.getName() + " " + actorDto.getSurname();
            performanceDto.getActors().put(performanceActorId, actorFullName);
        }
        return performanceDto;
    }

    public PerformanceDto getPerformanceById(String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", actorId);
        Performance performance = mongoTemplate.findOne(query, Performance.class);
        if (performance.getTicketsList() == null)
            performance.setTicketsList(new ArrayList<>());
        if (performance.getActors() == null)
            performance.setActors(new ArrayList<>());
        return performanceToDto(performance);
    }

    public List<PerformanceDto> getAllPerformances() {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        return mongoTemplate.findAll(Performance.class).stream()
                .map(this::performanceToDto)
                .toList();
    }

    public PerformanceDto savePerformance(PerformanceDto performanceDto) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = dtoToPerformance(performanceDto);
        performance = mongoTemplate.save(performance);
        return modelMapper.map(performance, PerformanceDto.class);
    }

    public void deletePerformance(String performanceId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", performanceId);
        mongoTemplate.remove(query, Performance.class);
    }
}
