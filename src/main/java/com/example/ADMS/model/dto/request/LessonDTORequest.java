package com.example.ADMS.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonDTORequest {

    @NotBlank(message = "Lesson name is mandatory")
    @Size(min = 4, message = "Lesson name is greater than 3")
    String name;
}
