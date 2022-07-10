package com.example.demo.course.controller;

import com.example.demo.util.PageUtil;

public class BaseController {

    public String getPaperHtml(Long totalCount, Long pageSize, Long pageIndex
                               , String queryString){
        PageUtil pageUtilList = new PageUtil(totalCount, pageSize, pageIndex, queryString);
        return pageUtilList.pager();
    }
}
