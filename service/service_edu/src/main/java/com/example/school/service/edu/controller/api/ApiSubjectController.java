package com.example.school.service.edu.controller.api;

import com.example.school.common.base.result.R;
import com.example.school.service.edu.entity.vo.SubjectVo;
import com.example.school.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@Api(tags="课程分类")
@RestController
@RequestMapping("/api/edu/subject")
public class ApiSubjectController {
    @Resource
    private SubjectService subjectService;

    @ApiOperation("嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList() {
        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return R.ok().data("items", subjectVoList);
    }
}
