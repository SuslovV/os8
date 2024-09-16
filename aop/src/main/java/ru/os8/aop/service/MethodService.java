package ru.os8.aop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.os8.aop.model.ClassSourceCode;
import ru.os8.aop.model.Method;
import ru.os8.aop.repository.MethodRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MethodService {

    private final MethodRepository methodRepository;

    @Autowired
    public MethodService(MethodRepository methodRepository) {
        this.methodRepository = methodRepository;
    }

    public Optional<Method> findById(UUID id) {
        return methodRepository.findById(id);
    }
    public Optional<Method> findByNameAndClassSourceCode(String name, ClassSourceCode classSourceCode) {
        return methodRepository.findByNameAndClassSourceCode(name, classSourceCode);
    }

    public Page<Method> findAll(Pageable pageable) {
        return methodRepository.findAll(pageable);
    }

    public Method save(Method method) {
        return methodRepository.save(method);
    }

    public Method add(String name, ClassSourceCode classSourceCode) {
        var method = new Method();
        method.setClassSourceCode(classSourceCode);
        method.setName(name);
        method.setCreatedTime(OffsetDateTime.now());
        method.setLastModifiedTime(method.getCreatedTime());
        method.setActive(Boolean.TRUE);
        method.setDeleted(Boolean.FALSE);

        return methodRepository.save(method);
    }

    public void delete(Method method) {
        methodRepository.delete(method);
    }

}
