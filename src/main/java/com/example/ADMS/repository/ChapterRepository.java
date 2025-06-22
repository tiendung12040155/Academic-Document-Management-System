package com.example.ADMS.repository;

import com.example.ADMS.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Optional<Chapter> findByIdAndActiveTrue(Long id);

    @Query("select cha from Chapter cha where cha.name = :chapterName and cha.id != :chapterIdPresent and cha.bookVolume.id = :bookVolumeId")
    Optional<Chapter> findByName(String chapterName, Long chapterIdPresent, Long bookVolumeId);

    @Query("select c from Chapter c join c.bookVolume bv where c.active = true " +
            "and bv.active = true and bv.id = :bookVolumeId")
    List<Chapter> findAllByBookVolumeId(Long bookVolumeId);

    List<Chapter> findChapterByActiveTrue();
}
