package com.example.ADMS.utils;

import org.springframework.stereotype.Service;

@Service
public class Constants {
    public static final String HOST = "http://localhost:8080";
    public static final String HOST_FRONT_END = "http://localhost:3000";
    public static final String API_VERSION = "/api/v1";
    public static final Long DEFAULT_PAGE_SIZE = 12L;
    public static final Long DEFAULT_PAGE_INDEX = 1L;
    public static final Long TOTAL_POINT_DEFAULT = 10L;
    public static final Long EMAIL_WAITING_EXPIRATION = 15L;
    public static final Long POINT_RESOURCE = 0L;
    public static final Long DEFAULT_TAG_PAGE_SIZE = 25L;
    public static final String DEFAULT_PASSWORD = "aA12345678";
    public static final int SUGGEST_NUMBER_DOCUMENT = 30;
    public static String[] LIST_PERMIT_ALL = new String[]{
            "/api/v1/auth/**", "/api/v1/register/**", "/v2/api-docs", "/v3/api-docs",
            "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security", "/swagger-ui/**",
            "/webjars/**", "/swagger-ui.html", "/api/v1/resource/materials/**",
            "/api/v1/resource/medias/**", "/api/v1/resource/tags",
            "/api/v1/class/list", "/api/v1/book-series/list-by-class",
            "/api/v1/subject/list-by-book-series", "/api/v1/book-volume/list-by-book-series-subject",
            "/api/v1/chapter/list-by-book-volume", "/api/v1/lesson/list-by-chapter",
            "/api/v1/helper/**"
    };
    public static String CREATOR_RESOURCE_PERMISSION = "CDRUV";
    public static String CREATOR_RESOURCE_PERMISSION_MESSAGE = "Owner";
    public static String SHARED_RESOURCE_PERMISSION = "DV";
    public static String SHARED_RESOURCE_PERMISSION_MESSAGE = "View permission";
}
