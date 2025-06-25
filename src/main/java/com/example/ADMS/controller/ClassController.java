package com.example.ADMS.controller;

import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ClassDTOFilter;
import com.example.ADMS.model.dto.request.ClassDTORequest;
import com.example.ADMS.model.dto.response.ClassDTOResponse;
import com.example.ADMS.service.ClassService;
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
@RequestMapping(API_VERSION + "/class")
@Tag(name = "Class", description = "API for Class")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class ClassController {
    ClassService classService;

    @Operation(summary = "create Class")
    @PostMapping("")
    public ResponseEntity<ClassDTOResponse> create(@Valid @RequestBody ClassDTORequest request) {
        ClassDTOResponse response = classService.createClass(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update Class")
    @PutMapping("/{id}")
    public ResponseEntity<ClassDTOResponse> update(@Valid @RequestBody ClassDTORequest request,
                                                   @PathVariable @NotEmpty Long id) {
        ClassDTOResponse classDTOResponse = classService.updateClass(id, request);
        return ResponseEntity.ok(classDTOResponse);
    }

    @Operation(summary = "Change Status Class")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable @NotEmpty Long id) {
        return ResponseEntity.ok(classService.changeStatus(id));
    }

    @Operation(summary = "display Class")
    @GetMapping("/display")
    public PagingDTOResponse searchClass(@ModelAttribute ClassDTOFilter classDTOFilter) {
        return classService.searchClass(classDTOFilter);
    }

    @Operation(summary = "View Class by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ClassDTOResponse> viewClass(@PathVariable @NotEmpty Long id) {
        ClassDTOResponse classDTOResponse = classService.viewClassById(id);
        return ResponseEntity.ok(classDTOResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListClasses() {
        return ResponseEntity.ok(classService.getListClasses());
    }

    @GetMapping("/list-by-subject")
    public ResponseEntity<?> getListClassesBySubject(@RequestParam(required = false) Long subjectId) {
        return ResponseEntity.ok(classService.getListClassesBySubjectId(subjectId));
    }
}


