package com.example.ADMS.repository;

import com.example.ADMS.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findByIdAndActiveTrue(Long id);

    List<Class> findClassByActiveIsTrue();

    @Query("select c from Class c where c.name = :className and c.id != :classIdPresent")
    Optional<Class> findByName(String className, Long classIdPresent);

    @Query("select distinct bs.classObject from BookSeries bs join bs.bookSeriesSubjects bss join bss.subject s where s.id = :subjectId and " +
            "s.active = true and bss.active = true and bs.active = true")
    List<Class> findAllBySubjectId(Long subjectId);
}
