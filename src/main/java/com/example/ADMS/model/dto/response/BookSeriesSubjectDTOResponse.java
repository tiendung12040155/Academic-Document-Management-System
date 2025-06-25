package com.example.ADMS.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSeriesSubjectDTOResponse {
    Long id;
    Boolean active;
    LocalDateTime createdAt;
    String name;
    String creator;
}
