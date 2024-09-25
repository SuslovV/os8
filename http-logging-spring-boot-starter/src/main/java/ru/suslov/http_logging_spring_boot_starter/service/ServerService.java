package ru.suslov.http_logging_spring_boot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suslov.http_logging_spring_boot_starter.model.Server;
import ru.suslov.http_logging_spring_boot_starter.repository.ServerRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;

    public Optional<Server> findById(UUID id) {
        return serverRepository.findById(id);
    }

    public Optional<Server> findByName(String name) {
        return serverRepository.findByName(name);
    }

    public Page<Server> findAll(Pageable pageable) {
        return serverRepository.findAll(pageable);
    }

    public Server save(Server server) {
        return serverRepository.save(server);
    }

    public Server add(String name) {
        var server = new Server();
        server.setName(name);
        server.setPath(name);
        server.setCreatedTime(OffsetDateTime.now());
        server.setLastModifiedTime(server.getCreatedTime());
        server.setActive(Boolean.TRUE);
        server.setDeleted(Boolean.FALSE);

        return serverRepository.save(server);
    }

    public void delete(Server server) {
        serverRepository.delete(server);
    }

}
