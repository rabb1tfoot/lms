package com.example.demo.admin.mapper;

import com.example.demo.admin.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> selectList(CategoryDto parameter);
}
