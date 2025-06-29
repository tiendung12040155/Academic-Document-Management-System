package com.example.ADMS.service.impl;

import com.example.ADMS.entity.Tag;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.dto.request.TagDTOFilter;
import com.example.ADMS.model.dto.request.TagDTORequest;
import com.example.ADMS.model.dto.response.PagingTagDTOResponse;
import com.example.ADMS.model.dto.response.TagDTOResponse;
import com.example.ADMS.model.mapper.TagMapper;
import com.example.ADMS.repository.TagRepository;
import com.example.ADMS.repository.criteria.TagCriteria;
import com.example.ADMS.service.TagService;
import com.example.ADMS.utils.FileHelper;
import com.example.ADMS.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;
    TagCriteria tagCriteria;
    MessageException messageException;

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public TagDTOResponse createTag(TagDTORequest tagDTORequest) {
        String tagName = tagDTORequest.getName().toLowerCase().trim();
        tagDTORequest.setName(tagName);
        if (tagRepository.findByNameEqualsIgnoreCaseAndActiveTrue(tagDTORequest.getName()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_TAG_EXISTED);
        }
        if (FileHelper.checkContentInputValid(tagDTORequest.getName()))
            throw ApiException.badRequestException(messageException.MSG_TEXT_NO_STANDARD_WORD);
        Tag tag = TagMapper.toTag(tagDTORequest);
        tagRepository.save(tag);
        return TagMapper.toTagDTOResponse(tag);
    }

    @Override
    public TagDTOResponse disableTag(long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND));
        tag.setActive(!tag.getActive());
        tagRepository.save(tag);
        return TagMapper.toTagDTOResponse(tag);
    }

    @Override
    public PagingTagDTOResponse searchTags(TagDTOFilter tagDTOFilter) {
        return tagCriteria.searchTag(tagDTOFilter);
    }

    @Override
    public TagDTOResponse getTagByID(long id) {
        Tag tag = tagRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND));
        return TagMapper.toTagDTOResponse(tag);
    }
}
