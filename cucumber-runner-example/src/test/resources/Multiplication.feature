@my-test
Feature: Multiplication
  In order to avoid making mistakes
  As a dummy
  I want to multiply numbers

  Scenario: Multiply two variables
    Given a variable x with value 2
    And a variable y with value 3
    When I multiply x * y
    Then I get 6
