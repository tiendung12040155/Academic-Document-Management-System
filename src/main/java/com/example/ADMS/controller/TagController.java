package com.example.ADMS.controller;

import com.example.ADMS.model.dto.request.TagDTOFilter;
import com.example.ADMS.model.dto.request.TagDTORequest;
import com.example.ADMS.model.dto.response.PagingTagDTOResponse;
import com.example.ADMS.model.dto.response.TagDTOResponse;
import com.example.ADMS.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static com.example.ADMS.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(API_VERSION + "/tags")
@Tag(name = "Tag", description = "API for Tags")
@CrossOrigin
public class TagController {
    TagService tagService;

    //Moderator
    @Operation(summary = "Create tag")
    @PostMapping("")
    public TagDTOResponse createTag(@Valid @RequestBody TagDTORequest tagDTORequest) {
        return tagService.createTag(tagDTORequest);
    }

    @Operation(summary = "Disable a tag")
    @PutMapping("/{id}")
    public TagDTOResponse archiveTag(@PathVariable long id) {
        return tagService.disableTag(id);
    }

    //Moderator+Teacher
    @Operation(summary = "Search tags")
    @GetMapping("/search")
    public PagingTagDTOResponse searchTags(@ModelAttribute TagDTOFilter tagDTOFilter) {
        return tagService.searchTags(tagDTOFilter);
    }

    @Operation(summary = "Get tag by ID")
    @GetMapping("/{id}")
    public TagDTOResponse getTagByID(@PathVariable long id) {
        return tagService.getTagByID(id);
    }


}
