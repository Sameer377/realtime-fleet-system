package com.pappermint.app.repository;

import com.pappermint.app.entity.RobotTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Sameer Shaikh
 * @date 03-09-2026
 * @description
 */
public interface RobotTelemeteryRepository extends JpaRepository <RobotTelemetry,Long>{

    List<RobotTelemetry> findByRobotId(String robotId);

}
