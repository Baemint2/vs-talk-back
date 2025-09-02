package com.moz1mozi.vstalkbackend.domain.post.dto.enums;

import lombok.Getter;

@Getter
public enum PostSort {
    CREATED_ASC("asc"),
    CREATED_DESC("desc"),
    ENDING_SOON("endingSoon"),
    VOTES_DESC("vote");
    // COMMENTS_DESC("comment"),
    // VIEWS_DESC("views"),
    // HOT("hot");

    private final String code;
    PostSort(String code) { this.code = code; }

    public static PostSort from(String code) {
        if (code == null) return CREATED_ASC;
        for (var v : values()) {
            if (v.code.equalsIgnoreCase(code)) return v;
        }
        throw new IllegalArgumentException("Unsupported sort: " + code);
    }
}
