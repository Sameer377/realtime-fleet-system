package com.pappermint.app;

import com.pappermint.app.entity.RobotTelemetry;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI fleetManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fleet Management API")
                        .version("1.0")
                        .description("API for managing robot fleet state and telemetry"));
    }

}