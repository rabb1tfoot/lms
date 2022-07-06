package com.example.demo.admin.dto;

import com.example.demo.admin.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    Long id;

    String categoryName;
    int sortValue;
    boolean usingYN;

    public static List<CategoryDto> of (List<Category> categories){
        if(categories != null){
            List<CategoryDto> categoryList = new ArrayList<>();
            for(Category x : categories){
                categoryList.add(of(x));
            }
            return categoryList;
        }
        return null;
    }

    public static CategoryDto of(Category category){
        return CategoryDto.builder()
                .categoryName(category.getCategoryName())
                .id(category.getId())
                .sortValue(category.getSortValue())
                .usingYN(category.isUsingYN())
                .build();
    }
}