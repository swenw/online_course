package com.example.school.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.service.base.exception.SchoolException;
import com.example.school.service.vod.service.VideoService;
import com.example.school.service.vod.util.AliyunVodSDKUtils;
import com.example.school.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;


@Slf4j
@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VodProperties vodProperties;

    @Resource
    private VideoService videoService;

    @Override
    public String uploadVideo(InputStream inputStream, String originalFilename) {

        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret(),
                title, originalFilename, inputStream);

        /* 模板组ID(可选) */
//        request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        /* 工作流ID(可选) */
//        request.setWorkflowId(vodProperties.getWorkflowId());

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        String videoId = response.getVideoId();
        //没有正确的返回videoid则说明上传失败
        if(StringUtils.isEmpty(videoId)){
            log.error("阿里云上传失败：" + response.getCode() + " - " + response.getMessage());
            throw new SchoolException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String videoId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        client.getAcsResponse(request);
    }

    @Override
    public void removeVideoByIdList(List<String> videoIdList) throws ClientException {

        //初始化client对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        int size = videoIdList.size();
        StringBuffer idListStr = new StringBuffer();
        for (int i = 0; i < size; i++) {

            idListStr.append(videoIdList.get(i));
            if(i == size - 1 || i % 20 == 19){
                System.out.println("idListStr = " + idListStr.toString());
                //支持传入多个视频ID，多个用逗号分隔，最多20个
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                idListStr = new StringBuffer();
            }else if(i % 20 < 19){
                idListStr.append(",");
            }
        }
    }
}
