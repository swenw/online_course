package com.example.school.service.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school.common.base.result.R;
import com.example.school.service.statistics.entity.Daily;
import com.example.school.service.statistics.feign.UcenterMemberService;
import com.example.school.service.statistics.mapper.DailyMapper;
import com.example.school.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author SWenW
 * @since 2023-02-14
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Resource
    private UcenterMemberService ucenterMemberService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {

        //如果当日的统计记录已存在，则删除重新统计 或 提示用户当日记录已存在
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        baseMapper.delete(queryWrapper);

        //生成统计记录
        R r = ucenterMemberService.countRegisterNum(day);
        Integer registerNum = (Integer)r.getData().get("registerNum");
        int loginNum = RandomUtils.nextInt(100, 200);
        int videoViewNum = RandomUtils.nextInt(100, 200);
        int courseNum = RandomUtils.nextInt(100, 200);

        //在本地数据库创建统计信息
        Daily daily = new Daily();
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);
    }

    /**
     * 获取所有类比的数据
     */
    @Override
    public Map<String, Map<String, Object>> getChartData(String begin, String end) {

        HashMap<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> registerNum = this.getChartDataByType(begin, end, "register_num");
        Map<String, Object> loginNum = this.getChartDataByType(begin, end, "login_num");
        Map<String, Object> videoViewNum = this.getChartDataByType(begin, end, "video_view_num");
        Map<String, Object> courseNum = this.getChartDataByType(begin, end, "course_num");

        map.put("registerNum", registerNum);
        map.put("loginNum", loginNum);
        map.put("videoViewNum", videoViewNum);
        map.put("courseNum", courseNum);
        return map;
    }

    /**
     * 辅助方法：根据类别获取数据
     */
    private Map<String, Object> getChartDataByType(String begin, String end, String type) {

        HashMap<String, Object> map = new HashMap<>();

        ArrayList<String> xList = new ArrayList<>();//日期列表
        ArrayList<Integer> yList = new ArrayList<>();//数据列表

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(type, "date_calculated");
        queryWrapper.between("date_calculated", begin, end);

        List<Map<String, Object>> mapsData = baseMapper.selectMaps(queryWrapper);
        for (Map<String, Object> data : mapsData) {

            String dateCalculated = (String) data.get("date_calculated");
            xList.add(dateCalculated);

            Integer count = (Integer) data.get(type);
            yList.add(count);
        }
        map.put("xData", xList);
        map.put("yData", yList);
        return map;
    }
}
