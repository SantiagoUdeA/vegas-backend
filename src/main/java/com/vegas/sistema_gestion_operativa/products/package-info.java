@ApplicationModule(
        displayName = "Products Catalog",
        allowedDependencies = {
                "raw_material",
                "raw_material::domain",
                "raw_material::entity",
                "common::common-domain",
                "common::common-dto",
                "common::common-exceptions",
                "common::common-utils",
                "security",
                "branches"
        }
)
package com.vegas.sistema_gestion_operativa.products;

import org.springframework.modulith.ApplicationModule;