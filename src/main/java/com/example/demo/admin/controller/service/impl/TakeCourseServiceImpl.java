package com.example.demo.admin.controller.service.impl;

import com.example.demo.course.dto.TakeCourseDto;
import com.example.demo.course.entity.TakeCourse;
import com.example.demo.course.mapper.TakeCourseMapper;
import com.example.demo.course.model.*;
import com.example.demo.admin.controller.service.TakeCourseService;
import com.example.demo.course.repository.TakeCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TakeCourseServiceImpl implements TakeCourseService {

    private TakeCourseRepository takeCourseRepository;
    private final TakeCourseMapper takeCourseMapper;


    @Override
    public List<TakeCourseDto> list(TakeCourseParam parameter) {

        List<TakeCourseDto> list = takeCourseMapper.selectList(parameter);
        long totalCount = takeCourseMapper.selectListCount(parameter);

        if(!CollectionUtils.isEmpty(list)){
            int i = 0;
            for(TakeCourseDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - (totalCount - parameter.getPageStart() - i) + 1); //x.setSeq(totalCount - param.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public ServiceResult updateStatus(long id, String status) {

        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);

        if(!optionalTakeCourse.isPresent()){
            return new ServiceResult(false, "수강정보가 없습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();

        takeCourse.setStatus(status);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true);
    }
}
