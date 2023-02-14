package com.example.school.service.cms.controller.api;

import com.example.school.common.base.result.R;
import com.example.school.service.cms.entity.Ad;
import com.example.school.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

// @CrossOrigin //解决跨域问题
@Api(tags = "广告推荐管理")
@RestController
@RequestMapping("/api/cms/ad")
@Slf4j
public class ApiAdController {
    @Resource
    private AdService adService;

    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(@ApiParam(value = "推荐位id", required = true) @PathVariable String adTypeId) {

        List<Ad> ads = adService.selectByAdTypeId(adTypeId);
        return R.ok().data("items", ads);
    }
}
