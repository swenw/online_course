package com.example.school.service.edu.controller;


import com.example.school.common.base.result.R;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.common.base.util.ExceptionUtils;
import com.example.school.service.base.exception.SchoolException;
import com.example.school.service.edu.entity.vo.SubjectVo;
import com.example.school.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
@CrossOrigin
@Api(tags = "课程分类管理")
@RestController
@RequestMapping("/admin/edu/subject")
@Slf4j
public class SubjectController {
    @Resource
    private SubjectService subjectService;

    @ApiOperation("Excel批量导入课程分类")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "xls文件", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new SchoolException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation("嵌套数据列表")
    @GetMapping("/nested-list")
    public R nestedList() {
        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return R.ok().data("items", subjectVoList);
    }

}

