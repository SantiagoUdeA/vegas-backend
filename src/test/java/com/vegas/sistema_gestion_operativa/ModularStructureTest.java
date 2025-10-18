package com.vegas.sistema_gestion_operativa;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularStructureTests {
    @Test
    void modularStructureIsValid() {
        ApplicationModules modules = ApplicationModules.of(SistemaGestionOperativaApplication.class);
        // modules.forEach(System.out::println);
        modules.verify();
    }
}
