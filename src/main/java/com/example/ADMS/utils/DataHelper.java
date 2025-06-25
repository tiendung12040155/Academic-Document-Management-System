package com.example.ADMS.utils;

import com.example.ADMS.entity.Resource;
import com.example.ADMS.entity.User;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class DataHelper {
    public static Set<Long> parseStringToLongSet(String str) {
        str = str.replaceAll("[\\[\\] ]", "");
        String[] tagArray = str.split(",");
        Set<Long> tags = new HashSet<>();
        Arrays.stream(tagArray).forEach(tag -> tags.add(Long.parseLong(tag)));
        return tags;
    }

    public static String replaceSpecialChars(String input) {
        String regex = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`”“-]";
        return input.replaceAll(regex, " ")
                .replaceAll("\\s+", " ")
                .toLowerCase().trim();
    }

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("")
                .replace("Đ", "D").replace("đ", "d");
        return replaceSpecialChars(result);
    }

    public static String extractFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf("."))
                    .replace(".", "");
        }
        return "";
    }

    public static String extractFilename(String filename) {
        return filename.split("\\.")[0];
    }

    public static String generateFilename(String filename, String extension) {
        String name = filename.concat("-").concat(String.valueOf(System.currentTimeMillis()))
                .concat(".").concat(extension);
        name = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "-");
        return name;
    }

    public static String getLinkResource(Resource resource, S3Util s3Util) {
        return s3Util.getPresignedUrl(resource.getResourceSrc()).toString();
    }

    public static String getLinkThumbnail(Resource resource, S3Util s3Util) {
        return s3Util.getPresignedUrl(resource.getThumbnailSrc()).toString();
    }

    public static String getLinkAvatar(User user, S3Util s3Util) {
        return s3Util.getPresignedUrl(user.getAvatar()).toString();
    }

    public static Boolean isRole(User userLoggedIn, Long roleId) {
        return userLoggedIn.getUserRoleList().stream().anyMatch(r -> Objects.equals(r.getRole().getId(), roleId));
    }
}
