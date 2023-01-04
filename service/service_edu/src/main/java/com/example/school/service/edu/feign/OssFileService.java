package com.example.school.service.edu.feign;

import com.example.school.common.base.result.R;
import com.example.school.service.edu.feign.fallback.OssFileServiceFallBack;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {
    @GetMapping("/admin/oss/file/test")
    R test();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);
}
