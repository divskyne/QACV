Feature: User Log In Testing
  As a User...
  I want to be able to log in...
  So that I can access the correct features
 
  Scenario Outline: User Log In 
    Given I'm at the Log In page
    When I Log In with the "<Username>" and the "<Password>" 
    Then I'm successfully logged in 
    And return the correct "<Role>"


    Examples: 
      | Username | Password | Role  |
      | Melvin |   lemontea | Trainer |
      | Melv |   lemon | Trainee |
      | Me |   tea | Trainer Manager |
      
      