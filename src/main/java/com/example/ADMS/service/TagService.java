package com.example.ADMS.service;


import com.example.ADMS.entity.Tag;
import com.example.ADMS.model.dto.request.TagDTOFilter;
import com.example.ADMS.model.dto.request.TagDTORequest;
import com.example.ADMS.model.dto.response.PagingTagDTOResponse;
import com.example.ADMS.model.dto.response.TagDTOResponse;

public interface TagService {
    Tag saveTag(Tag tag);

    TagDTOResponse createTag(TagDTORequest tagDTORequest);

    TagDTOResponse disableTag(long id);

    TagDTOResponse getTagByID(long id);

    PagingTagDTOResponse searchTags(TagDTOFilter tagDTOFilter);
}
