package com.example.ADMS.service;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ChapterDTOFilter;
import com.example.ADMS.model.dto.request.ChapterDTORequest;
import com.example.ADMS.model.dto.response.ChapterDTOResponse;

import java.util.List;

public interface ChapterService {
    ChapterDTOResponse createChapter(ChapterDTORequest request, Long bookVolumeId);

    ChapterDTOResponse updateChapter(Long id, ChapterDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter, Long bookVolumeId);

    ChapterDTOResponse viewChapterById(Long id);

    List<ChapterDTOResponse> getListChapterByBookVolumeId(Long bookVolumeId);
}
