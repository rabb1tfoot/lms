package com.example.demo.admin.controller.service.impl;

import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.entity.Course;
import com.example.demo.course.entity.TakeCourse;
import com.example.demo.course.mapper.CourseMapper;
import com.example.demo.course.model.CourseInput;
import com.example.demo.course.model.CourseParam;
import com.example.demo.course.model.ServiceResult;
import com.example.demo.course.model.TakeCourseInput;
import com.example.demo.course.repository.CourseRepository;
import com.example.demo.course.repository.TakeCourseRepository;
import com.example.demo.admin.controller.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseServiceInpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TakeCourseRepository takeCourseRepository;

    private LocalDate getLocalDate(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            return LocalDate.parse(value, formatter);
        }catch (Exception e){
        }
        return null;
    }

    @Override
    public boolean add(CourseInput parameter) {

        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDtText());

        Course course = Course.builder()
                .subject(parameter.getSubject())
                .categoryId(parameter.getCategoryId())
                .keyword(parameter.getKeyword())
                .summary(parameter.getSummary())
                .contents(parameter.getContents())
                .price(parameter.getPrice())
                .salePrice(parameter.getSalePrice())
                .saleEndDt(saleEndDt)
                //세일 종료일
                .regDt(LocalDateTime.now())
                .build();

        courseRepository.save(course);

        return true;
    }

    @Override
    public List<CourseDto> list(CourseParam parameter) {

        List<CourseDto> list = courseMapper.selectList(parameter);
        long totalCount = courseMapper.selectListCount(parameter);

        if(!CollectionUtils.isEmpty(list)){
            int i = 0;
            for(CourseDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - (totalCount - parameter.getPageStart() - i) + 1); //x.setSeq(totalCount - param.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public CourseDto getById(long id) {
        return courseRepository.findById(id).map(CourseDto::of).orElse(null);
    }

    @Override
    public boolean set(CourseInput parameter) {

        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDtText());

        Optional<Course> optionalCourse = courseRepository.findById(parameter.getId());
        if(!optionalCourse.isPresent()){
            return false;
        }
        Course course = optionalCourse.get();
        course.setSubject(parameter.getSubject());
        course.setUdtDt(LocalDateTime.now());
        course.setCategoryId(parameter.getCategoryId());
        course.setKeyword(parameter.getKeyword());
        course.setSummary(parameter.getSummary());
        course.setContents(parameter.getContents());
        course.setPrice(parameter.getPrice());
        course.setSaleEndDt(saleEndDt);
        course.setSalePrice(parameter.getSalePrice());
        //종료문자열

        courseRepository.save(course);

        return true;
    }

    @Override
    public boolean del(String idList) {

        if(idList != null && idList.length() > 0){
            String[] ids = idList.split(",");
            for(String x : ids){
                long id = 0L;
                try{
                    id = Long.parseLong(x);
                }catch (Exception e){

                }

                if(id > 0){
                    courseRepository.deleteById(id);
                }

            }
        }

        return true;
    }

    @Override
    public List<CourseDto> frontList(CourseParam parameter) {

        if(parameter.getCategoryId() < 1){
            List<Course> courseList = courseRepository.findAll();
            return CourseDto.of(courseList);
        }

        Optional<List<Course>> optionalCourseList = courseRepository.findByCategoryId(parameter.getCategoryId());
        if(optionalCourseList.isPresent()){
            return CourseDto.of(optionalCourseList.get());
        }

        return null;
    }

    @Override
    public CourseDto frontDetail(long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            return CourseDto.of(optionalCourse.get());
        }
        return null;
    }

    @Override
    public ServiceResult req(TakeCourseInput parameter) {

        Optional<Course> optionalCourse = courseRepository.findById(parameter.getCourseId());
        ServiceResult serviceResult = new ServiceResult();
        if(!optionalCourse.isPresent()){

            serviceResult.setResult(false);
            serviceResult.setMessage("강좌 정보가 존재하지않습니다.");

            return serviceResult;
        }

        Course course = optionalCourse.get();

        //이미 신청했는지 확인
        String[] statusList = {TakeCourse.STATUS_REQ, TakeCourse.STATUS_COMPLETE};
        long count = takeCourseRepository.countByCourseIdAndUserIdAndStatusIn(course.getId()
        , parameter.getUserId(), List.of(statusList));

        if(count > 0){
            serviceResult.setResult(false);
            serviceResult.setMessage("이미 강좌를 신청하셨습니다.");

            return serviceResult;
        }

        TakeCourse takeCourse = TakeCourse.builder()
                .courseId(course.getId())
                .userId(parameter.getUserId())
                .payPrice(course.getSalePrice())
                .status(TakeCourse.STATUS_REQ)
                .regDt(LocalDateTime.now()).build();

        takeCourseRepository.save(takeCourse);
        serviceResult.setResult(true);
        serviceResult.setMessage("강좌신청성공.");
        return serviceResult;
    }
}
