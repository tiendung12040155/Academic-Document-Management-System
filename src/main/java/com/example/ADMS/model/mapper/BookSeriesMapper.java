package com.example.ADMS.model.mapper;

import com.example.ADMS.entity.BookSeries;
import com.example.ADMS.model.dto.response.BookSeriesDTOResponse;

public class BookSeriesMapper {
    public static BookSeriesDTOResponse toBookseriesDTOResponse(BookSeries bookSeries) {
        return BookSeriesDTOResponse.builder()
                .name(bookSeries.getName())
                .id(bookSeries.getId())
                .active(bookSeries.getActive())
                .createdAt(bookSeries.getCreatedAt())
                .classDTOResponse(ClassMapper.toClassDTOResponse(bookSeries.getClassObject()))
                .build();
    }

    public static BookSeriesDTOResponse toBookseriesDTOResponse(BookSeries bookSeries, String userName) {
        return BookSeriesDTOResponse.builder()
                .name(bookSeries.getName())
                .id(bookSeries.getId())
                .active(bookSeries.getActive())
                .createdAt(bookSeries.getCreatedAt())
                .classDTOResponse(ClassMapper.toClassDTOResponse(bookSeries.getClassObject()))
                .creator(userName)
                .build();
    }
}
