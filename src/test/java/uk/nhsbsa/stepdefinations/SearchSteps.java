package uk.nhsbsa.stepdefinations;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import uk.nhsbsa.utils.DriverFactory;
import uk.nhsbsa.pages.SearchPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class SearchSteps {
    WebDriver driver;
    SearchPage searchPage;

    @Before
    public void setUp() {
        driver = DriverFactory.getDriver();
        searchPage = new SearchPage(driver);
    }


    @Given("I am on the NHS Jobs website")
    public void i_am_on_the_nhs_jobs_website() {
        driver = DriverFactory.getDriver();
        driver.get("https://www.jobs.nhs.uk/candidate/search");
        searchPage = new SearchPage(driver);
        searchPage.acceptCookiesIfPresent();
    }

    @When("I enter {string} as the job title and {string} as the location")
    public void i_enter_preferences(String jobTitle, String location) {
        searchPage.enterJobPreferences(jobTitle, location);
    }

    @When("I click the search button")
    public void click_search_button() {
        searchPage.clickSearch();
    }

    @Then("I should see a list of jobs that match my preferences")
    public void verify_search_results() {
        searchPage.assertSearchResultsPresent();
    }

    @Then("I should be able to sort results by newest Date Posted")
    public void sort_by_newest() {
        searchPage.sortByDatePosted();
        searchPage.assertResultsSortedByDate();
    }

    @Then("a message indicating no result found should be displayed")
    public void verify_no_results() {
        searchPage.assertNoSearchResultsPresent();
    }

    @When("I update my search preferences and perform a new search")
    public void update_search_references() {
        searchPage.updateJobPreferences("Consultant", "Macclesfield");
        searchPage.clickSearch();
    }

    @Then("the sort order should reset to default")
    public void verify_sort_order() {
        boolean isReset = searchPage.isSortOrderResetToDefault();
        if (!isReset) {
            throw new AssertionError("Expected sort order to reset to 'Best match' but it did not.");
        }
    }

    @After
    public void cleanUp() {
        DriverFactory.quitDriver();
    }

}
