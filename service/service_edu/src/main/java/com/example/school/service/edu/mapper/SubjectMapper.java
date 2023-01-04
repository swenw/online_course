package com.example.school.service.edu.mapper;

import com.example.school.service.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.school.service.edu.entity.vo.SubjectVo;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    List<SubjectVo> selectNestedListByParentId(String parentId);
}
