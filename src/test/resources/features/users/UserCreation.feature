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
