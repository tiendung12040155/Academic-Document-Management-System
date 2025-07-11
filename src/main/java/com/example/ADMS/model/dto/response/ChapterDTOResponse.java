package com.example.ADMS.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterDTOResponse {
    Long id;
    String name;
    Boolean active;
    LocalDateTime createdAt;
    @JsonIgnore
    BookVolumeDTOResponse bookVolumeDTOResponse;
    String creator;
}
