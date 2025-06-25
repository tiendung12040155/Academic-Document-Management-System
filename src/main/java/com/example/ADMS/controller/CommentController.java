package com.example.ADMS.controller;


import com.example.ADMS.model.dto.request.CommentDTOFilter;
import com.example.ADMS.model.dto.request.CommentDTORequest;
import com.example.ADMS.service.CommentService;
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
@RequestMapping(API_VERSION + "/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Comment", description = "API for Comment")
@CrossOrigin
public class CommentController {
    CommentService commentService;

    @PostMapping
    @Operation(summary = "Create comment or reply comment(has comment root id)")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDTORequest request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @GetMapping("/more/{id}")
    @Operation(summary = "See more reply of a comment")
    public ResponseEntity<?> seeMoreReplyComment(@Valid @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.seeMoreReplyComment(id));
    }

    @Operation(summary = "Change Status ")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable @NotEmpty Long id) {
        return ResponseEntity.ok(commentService.changeStatus(id));
    }

    @Operation(summary = "Search Comment ")
    @GetMapping("/display")
    public ResponseEntity<?> searchComment(@ModelAttribute CommentDTOFilter commentDTOFilter) {
        return ResponseEntity.ok(commentService.searchComment(commentDTOFilter));
    }

    @Operation(summary = "Get Detail comment")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailComment(@PathVariable @NotEmpty Long id) {
        return ResponseEntity.ok(commentService.getReportDetailComment(id));
    }
}
