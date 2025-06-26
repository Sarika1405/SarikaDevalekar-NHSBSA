# NHS Jobs Functional Acceptance Test Suite

## Exercise Coverage
**Acceptance Criteria Covered**
- Input preferences into the search form.
- Receive job results that match those preferences.
- Ensure results are sorted by the newest Date Posted.

## Test Coverage
| Test Area               | Description                                     |
|------------------------|-------------------------------------------------|
| Input & Submission      | Validates form inputs and successful submission |
| Result Matching         | Ensures results align with user preferences     |
| Sorting Logic           | Verifies sorting by newest Date Posted          |
| Negative Scenarios      | invalid data, and no-result cases               |


## Prerequisites
- Java 21
- Maven
- Selenium WebDriver
- Cucumber BDD
- WebDriverManager (for dynamic driver management)
- JUnit

## Project Structure

nhs-acceptance-tests/ 
├── src/ │   
└── test/ │       
      ├── java/ │       
         └── uk.nhsbsa/
             ├── pages/                # Contains Page Object Model (POM) classes  
             ├── runners/              # Contains the TestRunner class     
             └── stepdefinations/      # Step definition files    
             └── ultis/                # Contains helper classes that support test execution  
      ├── resources/ │           
         └── features/                 # Gherkin .feature files 
├── target/   
      └── cucumber-reports/            # Generated HTML reports 
├── pom.xml                            # Maven project configuration 
└── README.md                          # Project documentation


## Cross-Browser Support
- This project supports **Chrome** and **Firefox** browsers using runtime WebDriver resolution:
- Note: While both Chrome and Firefox must be installed on the local machine or CI agent, this project fully complies with the requirement to avoid downloaded or machine-based drivers.
- WebDriver binaries are handled at runtime via WebDriverManager, which ensures a consistent and portable setup across environments.
- No drivers are manually downloaded or committed with the project.

## Test Report

After running the test suite with below run command, a Cucumber HTML report is automatically generated.
at location : 
target/cucumber-reports/report.html
# Steps to view report
- Right Click -> select Copy Path/Reference -> select Absolute Path
- Go to any browser and past the copied absolute path 

## How to Run Tests

- Just click on Run button appear it will trigger test

### Chrome
```bash
mvn clean test -Dbrowser=chrome

### Firefox
```bash
mvn clean test -Dbrowser=firefox



