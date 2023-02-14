package com.example.school.service.statistics.feign.fallback;

import com.example.school.common.base.result.R;
import com.example.school.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public R countRegisterNum(String day) {
        log.error("熔断器被执行");
        return R.ok().data("registerNum", 0);
    }
}
