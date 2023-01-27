package com.example.school.service.edu.feign.fallback;

import com.example.school.common.base.result.R;
import com.example.school.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodMediaServiceFallBack implements VodMediaService {
    @Override
    public R removeVideo(String vodId) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoSourceIdList) {
        log.info("熔断保护");
        return R.error();
    }
}
