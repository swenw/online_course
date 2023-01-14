package com.example.school.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school.service.edu.entity.Chapter;
import com.example.school.service.edu.entity.Video;
import com.example.school.service.edu.mapper.ChapterMapper;
import com.example.school.service.edu.mapper.VideoMapper;
import com.example.school.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private VideoMapper videoMapper;

    @Override
    public boolean removeChapterById(String id) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", id);
        videoMapper.delete(videoQueryWrapper);

        return this.removeById(id);
    }
}
