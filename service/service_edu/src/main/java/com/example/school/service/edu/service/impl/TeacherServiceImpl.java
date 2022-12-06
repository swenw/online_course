package com.example.school.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.service.edu.entity.Teacher;
import com.example.school.service.edu.entity.vo.TeacherQueryVo;
import com.example.school.service.edu.mapper.TeacherMapper;
import com.example.school.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public IPage<Teacher> selectPage(Page<Teacher> teacherPage, TeacherQueryVo teacherQueryVo) {
        // 显示分页查询列表
        // 1. 根据sort字段进行排序
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByAsc("sort");
        // 2. 没有条件，直接进行分页查询
        if (teacherQueryVo == null) {
            return baseMapper.selectPage(teacherPage, teacherQueryWrapper);
        }
        // 3.条件查询
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        if (!StringUtils.isNullOrEmpty(name)) {
            teacherQueryWrapper.likeRight("name", name);
        } if (level != null) {
            teacherQueryWrapper.eq("level", level);
        } if (!StringUtils.isNullOrEmpty(joinDateBegin)) {
            teacherQueryWrapper.ge("join_date", joinDateBegin);
        } if (!StringUtils.isNullOrEmpty(joinDateEnd)) {
            teacherQueryWrapper.le("join_date", joinDateEnd);
        }
        return baseMapper.selectPage(teacherPage, teacherQueryWrapper);
    }
}
