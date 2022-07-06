package com.example.demo.admin.service;

import com.example.demo.admin.dto.CategoryDto;
import com.example.demo.admin.entity.Category;
import com.example.demo.admin.model.CategoryInput;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> list();

    boolean add(String categoryName);

    boolean update(CategoryInput parameter);

    boolean del(long id);
}
