package com.allclear.socialhub.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostPaging {

    private int postCnt;
    private List<?> postList = new ArrayList<>();
    private int pageSize;
    private int page;
    private int totalPage;

    public PostPaging(Page<?> pageList) {

        this.postCnt = (int) pageList.getTotalElements();
        this.postList = pageList.getContent();
        this.pageSize = pageList.getSize();
        this.page = pageList.getPageable().getPageNumber();
        this.totalPage = pageList.getTotalPages();
    }

}
