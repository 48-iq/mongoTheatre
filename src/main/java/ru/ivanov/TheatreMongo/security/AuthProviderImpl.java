package ru.ivanov.TheatreMongo.security;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (username.contains("@") || password.contains("@"))
            throw new IllegalArgumentException("using @");
        MongoClient mongoClient = MongoClients.create();
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "/theatreDB");
        return new UsernamePasswordAuthenticationToken(new UserDetailsImpl(username, password, mongoTemplate), password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
