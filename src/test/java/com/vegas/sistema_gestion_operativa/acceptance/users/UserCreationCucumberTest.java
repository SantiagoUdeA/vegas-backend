package com.vegas.sistema_gestion_operativa.acceptance.users;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@SelectClasspathResource("features/users/UserCreation.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.vegas.sistema_gestion_operativa.acceptance.users")
public class UserCreationCucumberTest {
}

