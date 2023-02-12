package com.example.school.service.edu.controller.api;

import com.example.school.common.base.result.R;
import com.example.school.service.edu.entity.Course;
import com.example.school.service.edu.entity.Teacher;
import com.example.school.service.edu.service.CourseService;
import com.example.school.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin //解决跨域问题
@Api(tags = "广告推荐管理")
@RestController
@RequestMapping("/api/edu/index")
@Slf4j
public class ApiIndexController {
    @Resource
    private CourseService courseService;

    @Resource
    private TeacherService teacherService;

    @ApiOperation("首页课程列表")
    @GetMapping
    public R index() {
        //获取首页最热门前8条课程数据
        List<Course> courseList = courseService.selectHotCourse();

        //获取首页推荐前4条讲师数据
        List<Teacher> teacherList = teacherService.selectHotTeacher();

        return R.ok().data("courseList", courseList).data("teacherList", teacherList);
    }
}
