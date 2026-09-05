package com.pappermint.app.service;

import com.pappermint.app.dto.RobotConfigDTO;
import com.pappermint.app.dto.RobotTelemetryDTO;
import com.pappermint.app.entity.Robot;
import com.pappermint.app.entity.RobotTelemetry;
import com.pappermint.app.repository.RobotRepository;
import com.pappermint.app.repository.RobotTelemeteryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sameer Shaikh
 * @date 03-09-2026
 * @description
 */

@RequiredArgsConstructor
@Service
public class RobotService {

   private final RobotTelemeteryRepository robotTelemeteryRepository;
   private final RobotRepository robotRepository;

   public List<Robot> registerRobots(
           List<RobotConfigDTO> robotList
   ){
      return  robotRepository.saveAll(robotList.stream().map(RobotConfigDTO::toEntity).toList());
   }

    public List<Robot> listRobots(
            List<String> robotList
    ){
        return  robotRepository.findAllById(robotList);
    }

}
