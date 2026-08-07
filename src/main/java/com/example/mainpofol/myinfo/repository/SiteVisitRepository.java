package com.example.mainpofol.myinfo.repository;

import com.example.mainpofol.global.entity.SiteVisit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {

    @Query(value = """
            select date_format(created_at, '%Y-%m-%d') as bucket,
                   count(*) as visitCount
            from site_visit
            where created_at >= date_sub(curdate(), interval 13 day)
            group by date_format(created_at, '%Y-%m-%d')
            order by bucket
            """, nativeQuery = true)
    List<VisitCountProjection> countDailyVisits();

    @Query(value = """
            select date_format(created_at, '%H') as bucket,
                   count(*) as visitCount
            from site_visit
            where created_at >= curdate()
              and created_at < date_add(curdate(), interval 1 day)
            group by date_format(created_at, '%H')
            order by bucket
            """, nativeQuery = true)
    List<VisitCountProjection> countHourlyVisitsToday();

    interface VisitCountProjection {

        String getBucket();

        long getVisitCount();
    }
}
