package com.example.ADMS.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectDTOResponse {
    Long id;
    Long subjectId;
    String name;
    boolean active;
    LocalDateTime createdAt;
    @JsonIgnore
    List<BookSeriesDTOResponse> bookSeriesDTOResponse;
    @JsonIgnore
    BookSeriesSubjectDTOResponse bookSeriesSubject;
    String creator;
}
