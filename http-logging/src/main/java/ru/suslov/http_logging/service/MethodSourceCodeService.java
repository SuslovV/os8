package ru.suslov.http_logging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suslov.http_logging.model.ClassSourceCode;
import ru.suslov.http_logging.model.MethodSourceCode;
import ru.suslov.http_logging.repository.MethodSourceCodeRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MethodSourceCodeService {

    private final MethodSourceCodeRepository methodSourceCodeRepository;

    @Autowired
    public MethodSourceCodeService(MethodSourceCodeRepository methodSourceCodeRepository) {
        this.methodSourceCodeRepository = methodSourceCodeRepository;
    }

    public Optional<MethodSourceCode> findById(UUID id) {
        return methodSourceCodeRepository.findById(id);
    }
    public Optional<MethodSourceCode> findByNameAndClassSourceCode(String name, ClassSourceCode classSourceCode) {
        return methodSourceCodeRepository.findByNameAndClassSourceCode(name, classSourceCode);
    }

    public Page<MethodSourceCode> findAll(Pageable pageable) {
        return methodSourceCodeRepository.findAll(pageable);
    }

    public MethodSourceCode save(MethodSourceCode method) {
        return methodSourceCodeRepository.save(method);
    }

    public MethodSourceCode add(String name, ClassSourceCode classSourceCode) {
        var method = new MethodSourceCode();
        method.setClassSourceCode(classSourceCode);
        method.setName(name);
        method.setCreatedTime(OffsetDateTime.now());
        method.setLastModifiedTime(method.getCreatedTime());
        method.setActive(Boolean.TRUE);
        method.setDeleted(Boolean.FALSE);

        return methodSourceCodeRepository.save(method);
    }

    public void delete(MethodSourceCode method) {
        methodSourceCodeRepository.delete(method);
    }

}
