package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.Tag;
import com.example.ADMS.model.dto.request.TagDTORequest;
import com.example.ADMS.model.dto.response.TagDTOResponse;
import com.example.ADMS.model.dto.response.TagSuggestDTOResponse;
import com.example.ADMS.utils.DateTimeHelper;

public class TagMapper {
    public static Tag toTag(TagDTORequest tagDTORequest) {
        return Tag.builder()
                .name(tagDTORequest.getName())
                .createdAt(DateTimeHelper.getTimeNow())
                .active(true)
                .build();
    }

    public static TagDTOResponse toTagDTOResponse(Tag tag) {
        return TagDTOResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .active(tag.getActive())
                .build();
    }

    public static TagSuggestDTOResponse toTagSuggestDTOResponse(Tag tag) {
        return TagSuggestDTOResponse.builder()
                .tagId(tag.getId())
                .tagName(tag.getName())
                .build();
    }
}
