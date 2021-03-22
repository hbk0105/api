package com.rest.api.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 *
 * Description : 페이징 클래스
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
@Setter
@Getter
public class PageRequest {
    private static int page;
    private static int size;
    private static Sort.Direction direction;
    public PageRequest(){
        this.page = 1;
        this.size = 10;
        this.direction = Sort.Direction.DESC;
    }
    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }
    public void setSize(int size) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }
    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
    // getter
    public static org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page -1, size, direction, "id");
    }

    public static org.springframework.data.domain.PageRequest of(String ordr , String ordrNm) {
        Sort.Direction direction = Sort.Direction.DESC;
        if("ASC".equals(ordr)){
            direction = Sort.Direction.ASC;
        }
        return org.springframework.data.domain.PageRequest.of(page -1, size, direction, ordrNm);
    }
}
