Feature: User Creation

Background:
  Given that the administrator is authenticated

Scenario: User Creation
  When the administrator enters an employee's information
  And creates a user, the system should register the new user
  And display a confirmation message "User created successfully"

Scenario: The user is already registered
  Given that the user's information is already registered in the system
  When the administrator enters the information of an already registered employee
  And creates a user, an error message is displayed "User already exists"

