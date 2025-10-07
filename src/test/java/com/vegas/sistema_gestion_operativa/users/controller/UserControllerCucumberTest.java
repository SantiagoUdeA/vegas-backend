package com.vegas.sistema_gestion_operativa.users.controller;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@SelectClasspathResource("features/UserController.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.vegas.sistema_gestion_operativa.users.controller")
public class UserControllerCucumberTest {
}

