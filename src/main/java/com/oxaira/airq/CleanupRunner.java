package com.oxaira.airq;

import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CleanupRunner implements CommandLineRunner {

    private final SensorRepository sensorRepository;
    private final JdbcTemplate jdbcTemplate;

    public CleanupRunner(SensorRepository sensorRepository, JdbcTemplate jdbcTemplate) {
        this.sensorRepository = sensorRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Cleaning up duplicated sensors...");
        Iterable<Sensor> sensors = sensorRepository.findAll();
        
        boolean keepOne = false;
        
        for (Sensor s : sensors) {
            if ("AA:BB:CC:DD:EE:FF".equals(s.getSerialNumber())) {
                if (!keepOne) {
                    keepOne = true;
                    continue; // Keep the first one
                }
                System.out.println("Deleting measurements for sensor id: " + s.getId());
                jdbcTemplate.update("DELETE FROM measurements WHERE sensor_id = ?", s.getId());
                System.out.println("Deleting sensor id: " + s.getId());
                sensorRepository.delete(s);
            }
        }
    }
}
