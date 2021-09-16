@Root

Feature: Test Feature

  @Regression
  Scenario Outline: Example Scenario
    Given init resources
    And upload input files "<filename>"
    And execute application with "<filename>"

    Examples:
      | filename   |
      | sample.csv |
