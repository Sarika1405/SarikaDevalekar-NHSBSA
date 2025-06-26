@acceptance
Feature: NHS Jobs Search Functionality

  As a jobseeker
  I want to search for jobs based on my preferences
  So that I can find roles relevant to me

  Background:
  Given I am on the NHS Jobs website

  Scenario: Search with job title and location, sort by newest
    When I enter "Manager" as the job title and "London" as the location
    And I click the search button
    Then I should see a list of jobs that match my preferences
    And I should be able to sort results by newest Date Posted


  Scenario: Job search and sort with default
    When I enter "Nurse" as the job title and "Manchester" as the location
    And I click the search button
    Then I should see a list of jobs that match my preferences


  Scenario: Invalid combination of preference
    When I enter "Astronaut" as the job title and "London" as the location
    And I click the search button
    Then a message indicating no result found should be displayed


  Scenario: Sorting resets to default after modifying search
    When I enter "Nurse" as the job title and "Manchester" as the location
    And I click the search button
    Then I should see a list of jobs that match my preferences
    And I should be able to sort results by newest Date Posted
    When I update my search preferences and perform a new search
    Then the sort order should reset to default


