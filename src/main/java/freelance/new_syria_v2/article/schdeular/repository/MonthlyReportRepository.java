package freelance.new_syria_v2.article.schdeular.repository;


import freelance.new_syria_v2.article.schdeular.entity.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {

    List<MonthlyReport> findAllByOrderByYearAscMonthAsc();
}

