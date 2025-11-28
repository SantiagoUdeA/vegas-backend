package com.vegas.sistema_gestion_operativa.common.configuration;

import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        // Establecer la zona horaria de Bogotá como predeterminada para toda la aplicación
        TimeZone.setDefault(TimeZone.getTimeZone(DateTimeUtils.BOGOTA_ZONE));
    }
}

