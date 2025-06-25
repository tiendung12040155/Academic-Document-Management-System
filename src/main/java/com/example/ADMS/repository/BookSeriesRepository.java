package com.example.ADMS.repository;

import com.example.ADMS.entity.BookSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookSeriesRepository extends JpaRepository<BookSeries, Long> {
    Optional<BookSeries> findByIdAndActiveTrue(Long id);

    @Query("select bs from BookSeries bs join bs.bookSeriesSubjects bss join bss.subject s where " +
            "s.id = :subjectId and s.active = true and bss.active = true and bs.active = true and bs.classObject.id = :classId")
    List<BookSeries> findAllBySubjectIdClassId(Long subjectId, Long classId);

    List<BookSeries> findBookSeriesByActiveTrue();

    @Query("select bs from BookSeries bs where bs.classObject.id = :classId and bs.active = true")
    List<BookSeries> findAllByClassId(Long classId);

    @Query("select bs from BookSeries bs where bs.name = :bookSeriesName and bs.id != :bookSeriesIdPresent and bs.classObject.id = :classId")
    Optional<BookSeries> findByName(String bookSeriesName, Long bookSeriesIdPresent, Long classId);
}
