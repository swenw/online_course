package com.example.school.service.edu.service;

import com.example.school.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.school.service.edu.entity.vo.SubjectVo;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
public interface SubjectService extends IService<Subject> {
    void batchImport(InputStream inputStream);

    List<SubjectVo> nestedList();
}
