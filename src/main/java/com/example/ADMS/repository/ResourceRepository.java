package com.example.ADMS.repository;

import com.example.ADMS.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query("select r from Resource r where r.resourceSrc like %?1% and r.active = true")
    Optional<Resource> findByResourceSrc(String fileName);

    Optional<Resource> findByIdAndActiveTrue(Long id);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv join bv.bookSeriesSubject bss " +
            "join bss.bookSeries bs join bss.subject s join bs.classObject c where c.id in (:listClassIds) " +
            "and c.active = true and s.active = true and bs.active = true and bss.active = true and bv.active = true " +
            "and le.active = true and cha.active = true")
    Set<Long> findResourceIdClass(Set<Long> listClassIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv join bv.bookSeriesSubject bss " +
            "join bss.bookSeries bs join bss.subject s where bs.id in (:listBookSeriesIds) " +
            "and s.active = true and bs.active = true and bss.active = true and bv.active = true " +
            "and le.active = true and cha.active = true")
    Set<Long> findResourceIdBookSeries(Set<Long> listBookSeriesIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv join bv.bookSeriesSubject bss " +
            "join bss.subject s where s.id in (:listSubjectIds) " +
            "and s.active = true and bss.active = true and bv.active = true " +
            "and le.active = true and cha.active = true")
    Set<Long> findResourceIdSubject(Set<Long> listSubjectIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv where bv.id in (:listBookVolumeIds) " +
            "and bv.active = true and le.active = true and cha.active = true")
    Set<Long> findResourceIdBookVolume(Set<Long> listBookVolumeIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha where cha.id in (:listChapterIds) " +
            "and le.active = true and cha.active = true")
    Set<Long> findResourceIdChapter(Set<Long> listChapterIds);

    @Query("select r.id from Resource r join r.lesson le where le.id in (:listLessonIds) " +
            "and le.active = true")
    Set<Long> findResourceIdLesson(Set<Long> listLessonIds);
}
