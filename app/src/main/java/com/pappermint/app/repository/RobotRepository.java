package com.pappermint.app.repository;

import com.pappermint.app.entity.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sameer Shaikh
 * @date 03-09-2026
 * @description
 */
public interface RobotRepository extends JpaRepository<Robot,String> {

}
