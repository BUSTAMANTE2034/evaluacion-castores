package com.castores.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfo {

    private long total;
    private int pages;
    private int current_page;
    private int per_page;
    private boolean has_next;
    private boolean has_prev;
    private Integer next_page;
    private Integer prev_page;
}