package com.example.school.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.common.base.result.R;
import com.example.school.service.edu.entity.Teacher;
import com.example.school.service.edu.entity.vo.TeacherQueryVo;
import com.example.school.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
@CrossOrigin // 允许跨域
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherController {
    @Resource
    private TeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @GetMapping("/list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "根据id删除讲师", notes = "根据id删除讲师")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable String id) {
        boolean res = teacherService.removeById(id);
        if (res) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("讲师分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                      @ApiParam("讲师列表查询对象") TeacherQueryVo teacherQueryVo) {
        Page<Teacher> teacherPage = new Page<>(page, limit);
        IPage<Teacher> pageModel = teacherService.selectPage(teacherPage, teacherQueryVo);
        List<Teacher> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("新增讲师")
    @PostMapping("/save")
    public R save(@ApiParam("讲师对象") @RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return R.ok().message("保存成功");
    }

    @ApiOperation("更新讲师")
    @PutMapping("/update")
    // 前端以json对象进行提交，且在数据中必须要包含id字段
    public R update(@ApiParam("讲师对象") @RequestBody Teacher teacher) {
        boolean result = teacherService.updateById(teacher);
        if (result) {
            return R.ok().message("更新成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id获取讲师")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam("讲师对象的id") @PathVariable String id) {
        // 可能存在拿到id，但是还没来得及回显就被删掉的情况，因此要判断非空
        Teacher teacher = teacherService.getById(id);
        if (teacher != null) {
            return R.ok().data("item", teacher);
        } else {
            return R.error().message("数据不存在");
        }
    }
}

