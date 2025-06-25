package com.example.ADMS.controller;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.BookVolumeDTOFilter;
import com.example.ADMS.model.dto.request.BookVolumeDTORequest;
import com.example.ADMS.model.dto.response.BookVolumeDTOResponse;
import com.example.ADMS.service.BookVolumeService;
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
@RequestMapping(API_VERSION + "/book-volume")
@Tag(name = "BookVolume", description = "API for BookVolume")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class BookVolumeController {
    BookVolumeService bookVolumeService;

    @Operation(summary = "Create BookVolume")
    @PostMapping("")
    public ResponseEntity<?> create(
            @Valid @RequestBody BookVolumeDTORequest request,
            @RequestParam Long bookSeriesSubjectId
    ) {
        return ResponseEntity.ok(bookVolumeService.createBookVolume(request, bookSeriesSubjectId));
    }

    @Operation(summary = "Update BookVolume")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody BookVolumeDTORequest request,
                                    @PathVariable @NotEmpty Long id) {
        BookVolumeDTOResponse bookVolumeDTOResponse = bookVolumeService.updateBookVolume(id, request);
        return ResponseEntity.ok(bookVolumeDTOResponse);
    }

    @Operation(summary = "Change Status BookVolume")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable @NotEmpty Long id) {
        bookVolumeService.changeStatus(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Book volume By subjectId")
    @GetMapping("/display/{bookSeriesSubjectId}")
    public PagingDTOResponse searchBookVolume(
            @ModelAttribute BookVolumeDTOFilter bookVolumeDTOFilter,
            @PathVariable(name = "bookSeriesSubjectId") Long bookSeriesSubjectId
    ) {
        return bookVolumeService.searchBookVolume(bookVolumeDTOFilter, bookSeriesSubjectId);
    }


    @Operation(summary = "View BookVolume by Id")
    @GetMapping("/{id}")
    public ResponseEntity<?> viewBookVolume(@PathVariable @NotEmpty Long id) {
        BookVolumeDTOResponse bookVolumeDTOResponse = bookVolumeService.viewBookVolumeById(id);
        return ResponseEntity.ok(bookVolumeDTOResponse);
    }

    @GetMapping("/list-by-subject-book-series")
    public ResponseEntity<?> getListBookVolumeBySubjectId(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long bookSeriesId
    ) {
        return ResponseEntity.ok(bookVolumeService.getListBookVolumeBySubjectId(subjectId, bookSeriesId));
    }

    @GetMapping("/list-by-book-series-subject")
    public ResponseEntity<?> getListBookVolumeByBookSeriesSubjectId(
            @RequestParam(required = false) Long bookSeriesSubjectId
    ) {
        return ResponseEntity.ok(bookVolumeService.getListBookVolumeByBookSeriesSubjectId(bookSeriesSubjectId));
    }


}
