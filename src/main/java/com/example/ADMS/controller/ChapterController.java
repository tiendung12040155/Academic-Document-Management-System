package com.example.ADMS.controller;

import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ChapterDTOFilter;
import com.example.ADMS.model.dto.request.ChapterDTORequest;
import com.example.ADMS.model.dto.response.ChapterDTOResponse;
import com.example.ADMS.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.ADMS.utils.Constants.API_VERSION;


@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/chapter")
@Tag(name = "Chapter", description = "API for Chapter")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class ChapterController {
    ChapterService chapterService;

    @Operation(summary = "Create Chapter")
    @PostMapping("")
    public ResponseEntity<ChapterDTOResponse> create(@Valid @RequestBody ChapterDTORequest request, Long bookVolumeId) {
        ChapterDTOResponse chapterDTOResponse = chapterService.createChapter(request, bookVolumeId);
        return ResponseEntity.ok(chapterDTOResponse);
    }

    @Operation(summary = "Update Chapter")
    @PutMapping("/{id}")
    public ResponseEntity<ChapterDTOResponse> update(@Valid @RequestBody ChapterDTORequest request,
                                                     @PathVariable @NotEmpty Long id) {
        ChapterDTOResponse chapterDTOResponse = chapterService.updateChapter(id, request);
        return ResponseEntity.ok(chapterDTOResponse);
    }

    @Operation(summary = "Change Status Chapter")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable @NotEmpty Long id) {
        chapterService.changeStatus(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Chapter By bookVolumeId")
    @GetMapping("/display/{bookVolumeId}")
    public PagingDTOResponse searchChapter(@ModelAttribute ChapterDTOFilter chapterDTOFilter,
                                           @PathVariable(name = "bookVolumeId") Long bookVolumeId) {
        return chapterService.searchChapter(chapterDTOFilter, bookVolumeId);
    }

    @Operation(summary = "View Chapter by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTOResponse> viewChapter(@PathVariable @NotEmpty Long id) {
        ChapterDTOResponse chapterDTOResponse = chapterService.viewChapterById(id);
        return ResponseEntity.ok(chapterDTOResponse);
    }

    @GetMapping("/list-by-book-volume")
    public ResponseEntity<?> getListChapterByBookVolumeId(@RequestParam(required = false) Long bookVolumeId) {
        return ResponseEntity.ok(chapterService.getListChapterByBookVolumeId(bookVolumeId));
    }
}
