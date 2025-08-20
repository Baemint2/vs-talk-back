package com.moz1mozi.vstalkbackend.dto;

import java.util.List;

public record SliceResponse<T>(List<T> content, boolean hasNext, int page, int size) { }
