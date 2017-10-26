@addition
Feature: Addition
  In order to avoid making mistakes
  As a dummy
  I want to add numbers

  Scenario: Add two variables
    Given a variable x with value 2
    And a variable y with value 3
    When I add x + y
    Then I get 5
