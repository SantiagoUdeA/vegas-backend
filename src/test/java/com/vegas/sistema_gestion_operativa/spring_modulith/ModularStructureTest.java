package com.vegas.sistema_gestion_operativa.spring_modulith;

import com.vegas.sistema_gestion_operativa.SistemaGestionOperativaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularStructureTests {
    @Test
    void modularStructureIsValid() {
        ApplicationModules modules = ApplicationModules.of(SistemaGestionOperativaApplication.class);
        modules.verify();
    }
}
