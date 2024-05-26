package ru.ivanov.theatremongo.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
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
        BasicQuery query = new BasicQuery("{_id: ?0}", actorId);
        Performance performance = mongoTemplate.findOne(query, Performance.class);
        if (performance == null)
            throw new IllegalArgumentException("such performance doesn't exist")
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
        BasicQuery query = new BasicQuery("{_id: ?0}", performanceId);
        mongoTemplate.remove(query, Performance.class);
    }
}
