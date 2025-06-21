package com.example.ADMS.repository;

import com.example.ADMS.entity.BookSeriesSubject;
import com.example.ADMS.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookSeriesSubjectRepository extends JpaRepository<BookSeriesSubject, Long> {
    Optional<BookSeriesSubject> findByIdAndActiveTrue(Long id);

    @Query("select bss from BookSeriesSubject bss where bss.active = :active and bss.bookSeries.id = :bookSeriesId and bss.subject.id = :subjectId")
    BookSeriesSubject findBySubjectAndBookSeries(Long bookSeriesId, Long subjectId, Boolean active);

    @Query("select bss from BookSeriesSubject bss where bss.bookSeries.id = :bookSeriesId and bss.subject.id = :subjectId")
    BookSeriesSubject findBySubjectAndBookSeries(Long bookSeriesId, Long subjectId);

    @Query("select bss.subject from BookSeriesSubject bss where bss.active = true and bss.bookSeries.id = :bookSeriesId")
    List<Subject> findAllByBookSeries(Long bookSeriesId);
}
