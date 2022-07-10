package com.example.demo.course.model;

import com.example.demo.admin.model.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseParam extends CommonParam {

    long id;
    long categoryId;

}
