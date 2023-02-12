package com.example.school.service.sms.controller;

import com.aliyuncs.exceptions.ClientException;
import com.example.school.common.base.result.R;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.common.base.util.FormUtils;
import com.example.school.common.base.util.RandomUtils;
import com.example.school.service.base.exception.SchoolException;
import com.example.school.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {
    @Resource
    private SmsService smsService;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {
        //校验手机号是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            log.error("请输入正确的手机号码 ");
            throw new SchoolException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        // 生成验证码
        String checkCode = RandomUtils.getFourBitRandom();
        // 发送验证码
        smsService.send(mobile, checkCode);
        // 将验证码存入redis缓存
        // 这里不使用注解的原因是，不需要将整个返回结果存入redis中，只需要验证码信息
        redisTemplate.opsForValue().set(mobile, checkCode, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}
