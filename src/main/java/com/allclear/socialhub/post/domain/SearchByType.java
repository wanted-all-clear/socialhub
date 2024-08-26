package com.allclear.socialhub.post.domain;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum SearchByType {
    TITLE("title"),
    CONTENT("content");

    private final String searchBy;

    public static EnumSet<SearchByType> fromString(String searchBy) {

        EnumSet<SearchByType> result = EnumSet.noneOf(SearchByType.class);
        if (searchBy != null && !searchBy.isEmpty()) {
            for (String value : searchBy.split(",")) {
                try {
                    result.add(SearchByType.valueOf(value.trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new CustomException(ErrorCode.INVALID_SEARCH_CONDITION);
                }
            }
        }
        return result;
    }
}
