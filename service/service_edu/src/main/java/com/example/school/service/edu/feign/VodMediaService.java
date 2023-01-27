package com.example.school.service.edu.feign;

import com.example.school.common.base.result.R;
import com.example.school.service.edu.feign.fallback.VodMediaServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(value = "service-vod", fallback = VodMediaServiceFallBack.class)
public interface VodMediaService {
    @DeleteMapping("/admin/vod/media/remove/{vodId}")
    R removeVideo(@PathVariable("vodId") String vodId);

    @DeleteMapping("/admin/vod/media/remove/")
    R removeVideoByIdList(@RequestBody List<String> videoSourceIdList);
}
