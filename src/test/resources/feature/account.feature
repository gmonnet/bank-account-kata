# language: en
Feature: manage an account
  Scenario: create an account
    Given an empty database
    When the user create an account John Doe
    Then a http code 201 is received
    And the account John Doe exist
  Scenario: deposit and withdraw on an account
    Given an empty database
    And an account John Doe exist
    When the user deposit 50 on the account John Doe
    And the user deposit 20 on the account John Doe
    And the user withdraw 10 on the account John Doe
    Then the account John Doe has a balance of 60
# TODO test GET /{id}
# TODO test GET /{id}/operations
