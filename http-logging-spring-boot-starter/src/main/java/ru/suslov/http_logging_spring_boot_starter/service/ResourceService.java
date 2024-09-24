package ru.suslov.http_logging_spring_boot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suslov.http_logging_spring_boot_starter.model.Server;
import ru.suslov.http_logging_spring_boot_starter.model.Resource;
import ru.suslov.http_logging_spring_boot_starter.repository.ResourceRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

//    @Autowired
//    public ResourceService(ResourceRepository resourceRepository) {
//        this.resourceRepository = resourceRepository;
//    }

    public Optional<Resource> findById(UUID id) {
        return resourceRepository.findById(id);
    }
    public Optional<Resource> findByNameAndServer(String name, Server server) {
        return resourceRepository.findByNameAndServer(name, server);
    }

    public Page<Resource> findAll(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }

    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource add(String name, Server server) {
        var resource = new Resource();
        resource.setServer(server);
        resource.setName(name);
        resource.setPath(name);
        resource.setCreatedTime(OffsetDateTime.now());
        resource.setLastModifiedTime(resource.getCreatedTime());
        resource.setActive(Boolean.TRUE);
        resource.setDeleted(Boolean.FALSE);

        return resourceRepository.save(resource);
    }

    public void delete(Resource resource) {
        resourceRepository.delete(resource);
    }

}
