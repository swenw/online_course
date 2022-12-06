package com.example.school.service.oss.controller.admin;

import com.example.school.common.base.result.R;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.common.base.util.ExceptionUtils;
import com.example.school.service.base.exception.SchoolException;
import com.example.school.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Api(tags = "阿里云文件管理")
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/admin/oss/file")
public class FileController {
    @Resource
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file")
            MultipartFile file,
            @ApiParam(value = "模块", required = true)
            @RequestParam("module")
            String module) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream, module, originalFilename);

            return R.ok().message("文件上传成功").data("url", uploadUrl);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new SchoolException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }
}
