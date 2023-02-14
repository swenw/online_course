package com.example.school.service.statistics.task;

import com.example.school.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ScheduledTask {

    @Resource
    private DailyService dailyService;

    @Scheduled(cron = "0 0 12 * * ?")
    public void taskGenerateStatisticsData() {
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createStatisticsByDay(day);
        log.info("taskGenarateStatisticsData 统计完毕");
    }
}
