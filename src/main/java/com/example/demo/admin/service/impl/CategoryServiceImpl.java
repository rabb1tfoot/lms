package com.example.demo.admin.service.impl;

import com.example.demo.admin.dto.CategoryDto;
import com.example.demo.admin.entity.Category;
import com.example.demo.admin.mapper.CategoryMapper;
import com.example.demo.admin.model.CategoryInput;
import com.example.demo.admin.repository.CategoryRepository;
import com.example.demo.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private Sort getSortBySortValueDesc(){
        return Sort.by(Sort.Direction.DESC, "sortValue");
    }

    @Override
    public List<CategoryDto> list() {
       List<Category> categories = categoryRepository.findAll(getSortBySortValueDesc());

        return CategoryDto.of(categories);
    }

    @Override
    public boolean add(String categoryName){

        //카테고리 중복 체크

        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYN(true)
                .sortValue(0)
                .build();

        categoryRepository.save(category);

        return true;

    }
    @Override
    public boolean update(CategoryInput parameter){

        Optional<Category> optioinalcategory = categoryRepository.findById(parameter.getId());

        if(!optioinalcategory.isPresent()){
            return false;
        }

        Category category = optioinalcategory.get();

        category.setCategoryName(parameter.getCategoryName());
        category.setSortValue(parameter.getSortValue());
        category.setUsingYN(parameter.isUsingYN());
        categoryRepository.save(category);

        return true;

    }
    @Override
    public boolean del(long id){

        categoryRepository.deleteById(id);
        return true;

    }

    @Override
    public List<CategoryDto> frontList(CategoryDto categoryDto) {

        return categoryMapper.selectList(categoryDto);
    }
}
