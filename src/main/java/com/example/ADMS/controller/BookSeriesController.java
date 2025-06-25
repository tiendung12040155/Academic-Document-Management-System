package com.example.ADMS.controller;

import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.BookSeriesDTOFilter;
import com.example.ADMS.model.dto.request.BookSeriesDTORequest;
import com.example.ADMS.model.dto.response.BookSeriesDTOResponse;
import com.example.ADMS.service.BookSeriesService;
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
@RequestMapping(API_VERSION + "/book-series")
@Tag(name = "BookSeries", description = "API for Book Series")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class BookSeriesController {
    BookSeriesService bookSeriesService;

    @Operation(summary = "Create book series")
    @PostMapping("")
    public ResponseEntity<BookSeriesDTOResponse> create(@Valid @RequestBody BookSeriesDTORequest request, @RequestParam Long classId) {
        BookSeriesDTOResponse bookSeriesDTOResponse = bookSeriesService.createBookSeries(request, classId);
        return ResponseEntity.ok(bookSeriesDTOResponse);
    }

    @Operation(summary = "Update book series")
    @PutMapping("/{id}")
    public ResponseEntity<BookSeriesDTOResponse> update(@Valid @RequestBody BookSeriesDTORequest request,
                                                        @PathVariable @NotEmpty Long id) {
        BookSeriesDTOResponse bookSeriesDTOResponse = bookSeriesService.updateBookSeries(id, request);
        return ResponseEntity.ok(bookSeriesDTOResponse);
    }

    @Operation(summary = "Change Status book series")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable @NotEmpty Long id) {
        bookSeriesService.changeStatus(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Book series By ClassId")
    @GetMapping("/display/{classId}")
    public PagingDTOResponse searchBookSeries(@ModelAttribute BookSeriesDTOFilter bookSeriesDTOFilter,
                                              @PathVariable(name = "classId") Long classId) {
        return bookSeriesService.searchBookSeries(bookSeriesDTOFilter, classId);
    }

    @Operation(summary = "View book series by Id")
    @GetMapping("/{id}")
    public ResponseEntity<BookSeriesDTOResponse> viewBookSeries(@PathVariable @NotEmpty Long id) {
        BookSeriesDTOResponse bookSeriesDTOResponse = bookSeriesService.viewBookSeriesById(id);
        return ResponseEntity.ok(bookSeriesDTOResponse);
    }


    @GetMapping("/list-by-subject-class")
    public ResponseEntity<?> getListBookSeriesByClassesSubjectId(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long classId
    ) {
        return ResponseEntity.ok(bookSeriesService.getListBookSeriesByClassesSubjectId(subjectId, classId));
    }

    @GetMapping("/list-by-class")
    public ResponseEntity<?> getListBookSeriesByClassId(
            @RequestParam(required = false) Long classId
    ) {
        return ResponseEntity.ok(bookSeriesService.getListBookSeriesByClassId(classId));
    }
}

