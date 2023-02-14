package com.example.school.service.vod.controller.admin;

import com.example.school.common.base.result.R;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.common.base.util.ExceptionUtils;
import com.example.school.service.base.exception.SchoolException;
import com.example.school.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api(tags ="阿里云视频点播")
// @CrossOrigin //跨域
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class MediaController {
    @Resource
    private VideoService videoService;

    @PostMapping("/upload")
    public R uploadVideo(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file")MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId = videoService.uploadVideo(inputStream, originalFilename);
            return R.ok().message("视频上传成功").data("videoId", videoId);
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new SchoolException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
    }

    @DeleteMapping("remove")
    public R removeVideoByIdList(
            @ApiParam(value = "阿里云视频id列表", required = true)
            @RequestBody List<String> videoIdList){

        try {
            videoService.removeVideoByIdList(videoIdList);
            return  R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new SchoolException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
}
