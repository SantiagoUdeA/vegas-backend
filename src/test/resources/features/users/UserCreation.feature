Feature: User Creation
  As an administrator
  I want to create new users in the system
  So that employees can access the application

  Background:
    Given the administrator is authenticated

  Scenario: Create a new user
    When the administrator enters the employee's information
    And creates a user
    Then the system should register the new user
    And display a confirmation message "User created successfully"

  Scenario: The user is already registered
    Given the user’s information is already registered in the system
    When the administrator enters the information of an already registered employee
    And creates a user
    Then an error message is displayed: “The user already exists”

  Scenario: Incomplete information
    When incomplete user information is entered
    And creates a user
    Then the system displays an error message: “Required fields are missing”