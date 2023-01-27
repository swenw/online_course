package com.example.school.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school.service.edu.entity.Video;
import com.example.school.service.edu.feign.VodMediaService;
import com.example.school.service.edu.mapper.VideoMapper;
import com.example.school.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Resource
    private VodMediaService vodMediaService;

    @Override
    public void removeMediaVideoById(String id) {
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        log.warn("VideoServiceImpl：videoSourceId= " + videoSourceId);
        vodMediaService.removeVideo(videoSourceId);
    }

    /**
     * 获取阿里云视频id列表
     */
    private List<String> getVideoSourceIdList(List<Map<String, Object>> maps){
        List<String> videoSourceIdList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String videoSourceId = (String)map.get("video_source_id");
            videoSourceIdList.add(videoSourceId);
        }
        return videoSourceIdList;
    }

    @Override
    public void removeMediaVideoByCourseId(String courseId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_id", courseId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> videoSourceIdList = this.getVideoSourceIdList(maps);
        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    @Override
    public void removeMediaVideoByChapterId(String chapterId) {

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id", chapterId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> videoSourceIdList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String videoSourceId = (String)map.get("video_source_id");
            videoSourceIdList.add(videoSourceId);
        }
        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }
}
