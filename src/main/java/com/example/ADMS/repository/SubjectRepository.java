package com.example.ADMS.repository;

import com.example.ADMS.entity.BookSeriesSubject;
import com.example.ADMS.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByIdAndActiveTrue(Long id);

    List<Subject> findAllByActiveTrue();

    @Query("select bss.subject from BookSeriesSubject bss where bss.bookSeries.id = :bookSeriesId " +
            "and bss.subject.active = true and bss.active = true")
    List<Subject> findSubjectByBookSeries(Long bookSeriesId);


    @Query("select s from Subject s where s.name = :subjectName and s.id != :subjectIdPresent")
    Optional<Subject> findByName(String subjectName, Long subjectIdPresent);

    @Query("select bss from BookSeriesSubject bss where bss.subject.id = :subjectId and " +
            " bss.bookSeries.id = :bookSeriesId and bss.active = true and bss.subject.active = true")
    BookSeriesSubject findBookSeriesSubjectBySubjectIdBookSeriesId(Long subjectId, Long bookSeriesId);
}
