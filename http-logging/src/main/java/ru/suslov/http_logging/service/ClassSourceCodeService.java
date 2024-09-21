package ru.suslov.http_logging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suslov.http_logging.model.ClassSourceCode;
import ru.suslov.http_logging.repository.ClassSourceCodeRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassSourceCodeService {

    private final ClassSourceCodeRepository classSourceCodeRepository;

    @Autowired
    public ClassSourceCodeService(ClassSourceCodeRepository classSourceCodeRepository) {
        this.classSourceCodeRepository = classSourceCodeRepository;
    }

    public Optional<ClassSourceCode> findById(UUID id) {
        return classSourceCodeRepository.findById(id);
    }

    public Optional<ClassSourceCode> findByName(String name) {
        return classSourceCodeRepository.findByName(name);
    }

    public Page<ClassSourceCode> findAll(Pageable pageable) {
        return classSourceCodeRepository.findAll(pageable);
    }

    public ClassSourceCode save(ClassSourceCode classSourceCode) {
        return classSourceCodeRepository.save(classSourceCode);
    }

    public ClassSourceCode add(String name) {
        var classSourceCode = new ClassSourceCode();
        classSourceCode.setName(name);
        classSourceCode.setCreatedTime(OffsetDateTime.now());
        classSourceCode.setLastModifiedTime(classSourceCode.getCreatedTime());
        classSourceCode.setActive(Boolean.TRUE);
        classSourceCode.setDeleted(Boolean.FALSE);

        return classSourceCodeRepository.save(classSourceCode);
    }

    public void delete(ClassSourceCode classSourceCode) {
        classSourceCodeRepository.delete(classSourceCode);
    }

}
