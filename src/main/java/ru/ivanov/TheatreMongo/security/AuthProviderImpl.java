package ru.ivanov.theatremongo.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import ru.ivanov.theatremongo.model.Performance;

@Component
public class AuthProviderImpl implements AuthenticationProvider {
    @Value("${mongodb.connection.host:}")
    private String connectionHost;
    @Value("${mongodb.connection.port:}")
    private Integer connectionPort;
    @Value("${mongodb.connection.database:}")
    private String database;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        MongoClientSettings mcs = MongoClientSettings.builder()
                .applyToClusterSettings(settings -> 
                    settings.hosts(Collections.singletonList(new ServerAddress(connectionHost, connectionPort)))  
        ).credential(MongoCredential.createCredential(username, database, password.toCharArray()))
        .build();
        MongoClient mongoClient = MongoClients.create(mcs);
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, database);
        try {
            mongoTemplate.findAll(Performance.class);
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Wrong username or password");
        }
        return new UsernamePasswordAuthenticationToken(
            new UserDetailsImpl(username, password, mongoTemplate), password, Collections.emptyList()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
