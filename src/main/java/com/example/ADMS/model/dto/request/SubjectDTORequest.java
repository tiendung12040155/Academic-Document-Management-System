package com.example.ADMS.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectDTORequest {
    @Schema(description = "Subject is not null", example = "Toan")
    @NotEmpty(message = "Subject name is not null")
    @Length(min = 4, message = "Subject name is greater than 3")
    String name;
}
