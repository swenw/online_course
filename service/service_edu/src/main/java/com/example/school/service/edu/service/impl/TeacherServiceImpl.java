package com.example.school.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.common.base.result.R;
import com.example.school.service.edu.entity.Course;
import com.example.school.service.edu.entity.Teacher;
import com.example.school.service.edu.entity.vo.TeacherQueryVo;
import com.example.school.service.edu.feign.OssFileService;
import com.example.school.service.edu.mapper.*;
import com.example.school.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private OssFileService ossFileService;

    @Resource
    private CourseMapper courseMapper;

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

    @Override
    public List<Map<String, Object>> selectNameList(String key) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name", key);

        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);
        return list;
    }

    @Override
    public boolean removeAvatarById(String id) {
        // 根据id获取讲师的url地址
        Teacher teacher = baseMapper.selectById(id);
        if (teacher != null) {
            String avatar = teacher.getAvatar();
            if (!StringUtils.isNullOrEmpty(avatar)) {
                R r = ossFileService.removeFile(avatar);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {
        Teacher teacher = baseMapper.selectById(id);

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id", id);
        List<Course> courses = courseMapper.selectList(courseQueryWrapper);

        HashMap<String, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("courseList", courses);

        return map;
    }

    @Cacheable(value = "index", key = "'selectHotTeacher'")
    @Override
    public List<Teacher> selectHotTeacher() {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByAsc("sort");
        teacherQueryWrapper.last("limit 4");
        return baseMapper.selectList(teacherQueryWrapper);
    }
}
