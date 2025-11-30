package freelance.new_syria_v2.article.schdeular;


import freelance.new_syria_v2.article.repository.ArticleRepository;
import freelance.new_syria_v2.article.schdeular.entity.MonthlyReport;
import freelance.new_syria_v2.article.schdeular.repository.MonthlyReportRepository;
import freelance.new_syria_v2.article.schdeular.repository.MonthlyStatsProjection;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class StatsService {

    private final ArticleRepository articleRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    public StatsService(ArticleRepository articleRepository,
                        MonthlyReportRepository monthlyReportRepository) {
        this.articleRepository = articleRepository;
        this.monthlyReportRepository = monthlyReportRepository;
    }

    public void generateCurrentMonthStats() {
        var now = ZonedDateTime.now(ZoneId.of("Africa/Cairo"));
        int year = now.getYear();
        int month = now.getMonthValue();

        String monthName = now.getMonth().name();

        MonthlyStatsProjection stats = articleRepository.getStatsForMonth(year, month);

        MonthlyReport report = new MonthlyReport();
        report.setMonth(monthName);
        report.setYear(year);
        report.setTotalPosts(stats.getTotalPosts());
        report.setTotalComments(stats.getTotalComments());

        monthlyReportRepository.save(report);
    }

    public List<MonthlyReport> getAllMonthlyReports() {
        return monthlyReportRepository.findAllByOrderByYearAscMonthAsc();
    }
}
