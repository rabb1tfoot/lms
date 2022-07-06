package com.example.demo.admin.repository;

import com.example.demo.admin.entity.Category;
import com.example.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    //Optional<List<Category>> findAllOrderBySortValueDesc();
}
