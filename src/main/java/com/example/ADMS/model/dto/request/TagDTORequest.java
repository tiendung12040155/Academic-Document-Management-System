package com.example.ADMS.model.dto.request;


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

public class TagDTORequest {
    @Length(max = 50, message = "Tag length cannot be greater than 50")
    @NotEmpty(message = "Tag cannot be empty")
    String name;
}
