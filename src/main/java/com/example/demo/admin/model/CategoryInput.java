package com.example.demo.admin.model;

import lombok.Data;

@Data
public class CategoryInput {

    long id;
    String categoryName;
    int sortValue;
    boolean usingYN;
}
