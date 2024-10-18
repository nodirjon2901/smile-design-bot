package uz.result.smiledesignbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.result.smiledesignbot.entity.Application;

import java.time.LocalDateTime;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query(value = "select count(*) from application where auto_created_date>=:startDate and auto_created_date<=:endDate", nativeQuery = true)
    Long countInWeek(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
