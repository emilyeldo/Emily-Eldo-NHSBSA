# Framework - Cucumber + TestNG + Selenium + Java + Extent Reports

**XMLRunner**  - This is the main java class to run tests. TestNG file is dynamically created using this class in which TestRunner class is added as the XMLClass.

**TestRunner** - Test Data as well as scenarios to be run is picked up from src/main/resources > TestData.xlsx. Also, based on the value of property "tag" from maven, the test execution will either pick up from excel sheet with scenarios marked as "Yes"(This gives user flexibility to determine which test cases to be executed just by changing "To Execute" to yes or no) or test execution will be based on tags in the feature file. Excel sheet present under src/main/resources > TestData.xlsx

**FailedTestRunner** - Failed test cases can be re run from this class

**Excel Utils** - Once the test execution is completed, test execution result "pass" or "fail" as well as execution time for each scenario will get updated in the excel sheet
Extent Reports - Reports can be find under Reports folder and sub folder created based on execution date and time

## To run the test scenarios based on the excel file
`mvn compile exec:java -"Dexec.mainClass"="runners.XMLRunner"`


## To run the test scenarios based on the tag
`mvn compile exec:java -"Dexec.mainClass"="runners.XMLRunner" -DrunMode=tag -DtagFilter="@smoke"`

## To re-run failed scenarios

`mvn test -"Dcucumber.options"="@target/failed_scenarios.txt"`

## To run test scripts in firefox browser
User needs to set property `browser=firefox` in `config.properties` file under resources directory

## To Do

- Custom Exception Handling need to be added
- Logging need to be added using log4j
- Invalid validation for search fields can be considered(But not worth for automation as this can be done as part of manual testing)
- Test Case "User should see 'No result found' message when no match found" not yet completed
- In the job result verification, search criteria is verified for the list of jobs returned. Need to verify in search result header also which is pending (which can be done using contains text of the search criteria in the search result header element)

