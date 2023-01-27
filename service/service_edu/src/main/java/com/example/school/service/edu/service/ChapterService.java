package com.example.school.service.edu.service;

import com.example.school.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.school.service.edu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
public interface ChapterService extends IService<Chapter> {

    boolean removeChapterById(String id);

    List<ChapterVo> nestedList(String courseId);
}
