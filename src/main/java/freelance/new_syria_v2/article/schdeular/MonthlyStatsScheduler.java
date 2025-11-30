package freelance.new_syria_v2.article.schdeular;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlyStatsScheduler {

    private final StatsService statsService;

    public MonthlyStatsScheduler(StatsService statsService) {
        this.statsService = statsService;
    }


    @Scheduled(cron = "0 59 23 L * ?", zone = "Africa/Cairo")
    public void runMonthlyStatsJob() {
        statsService.generateCurrentMonthStats();
        System.out.println("Monthly stats generated successfully.");
    }
}

