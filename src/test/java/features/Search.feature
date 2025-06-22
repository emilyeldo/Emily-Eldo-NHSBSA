Feature: NHS  Job Search Functionality
  As a jobseeker on NHS Jobs website
  I want to search for a job with my preferences
  So that I can get recently posted job results
Background:
    Given I am a jobseeker on NHS Jobs website

@Functional @smoke  
    Scenario:    Search for jobs using all search options
    When I put my "all" preferences into the Search functionality
    Then I should get a list of jobs which matches my preferences
    And sort my search results with the "newest Date Posted"
@Functional  @smoke  
    Scenario:   Search for jobs using fewer search options
    When I put my "few" preferences into the Search functionality
    Then I should get a list of jobs which matches my preferences
    And sort my search results with the "newest Date Posted"
@Functional     
    Scenario:   Search for jobs using location
    When I put my "location" preferences into the Search functionality
    Then I should get a list of jobs which matches my preferences
    And sort my search results with the "newest Date Posted"
@smoke  @Functional    
    Scenario:   Search for jobs using an employer
    When I put my "employer" preferences into the Search functionality
    Then I should get a list of jobs which matches my preferences
    And sort my search results with the "newest Date Posted"
@Functional 
    Scenario:    Search for jobs using a job reference id
    When I put my "JobReference" preferences into the Search functionality
    Then I should get a list of jobs which matches my preferences
    And sort my search results with the "newest Date Posted"
@Functional 
    Scenario:     Search for jobs using salary range
    When I put my "salary" preferences into the Search functionality
    Then I should get a list of jobs which matches my preferences
    And sort my search results with the "newest Date Posted"
    
 @Functional       
    Scenario:     Search for jobs using job title or skills
    When I put my "Job_Title_Or_Skill" preferences into the Search functionality
    Then I should get a list of jobs which matches my preferences
    And sort my search results with the "newest Date Posted"
    
   
    Scenario:     User should see ' No results found' message when no match found
    When I put my "Job_Title_Or_Skill" preferences into the Search functionality
    Then I should see "No results found" message
    
 @Functional   @smoke    
    Scenario:     Search for jobs with no search criteria
    When I put my "no" preferences into the Search functionality
    Then the results should be sorted by "Best Match" by default