package com.vegas.sistema_gestion_operativa.acceptance.users;

import com.vegas.sistema_gestion_operativa.utils.BaseCucumberTest;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasspathResource("features/users/UserCreation.feature")
public class UserCreationCucumberTest extends BaseCucumberTest {
}

