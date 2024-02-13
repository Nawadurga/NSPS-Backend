package com.project.carparking.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WithPaginationResponse<T> {
    private List<T> content = new ArrayList<>();
    private PaginationResponse paginationResponse;
}
