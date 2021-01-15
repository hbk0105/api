package com.rest.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardPaginationDto {

    private Long id;
    private String name;
    private String content;

}
