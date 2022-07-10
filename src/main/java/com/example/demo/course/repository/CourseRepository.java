package com.example.demo.course.repository;

import com.example.demo.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<List<Course>> findByCategoryId(Long categoryId);
}
