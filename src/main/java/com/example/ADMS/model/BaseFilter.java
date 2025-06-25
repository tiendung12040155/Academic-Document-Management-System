package com.example.ADMS.model;

import com.example.ADMS.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseFilter {
    Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
    Long pageSize = Constants.DEFAULT_PAGE_SIZE;
    String name = "";
}
