package com.example.school.service.edu.service;

import com.example.school.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
public interface VideoService extends IService<Video> {
    void removeMediaVideoById(String id);

    void removeMediaVideoByCourseId(String courseId);

    void removeMediaVideoByChapterId(String chapterId);
}
