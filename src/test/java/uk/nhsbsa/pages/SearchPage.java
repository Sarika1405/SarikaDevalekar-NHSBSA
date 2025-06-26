package uk.nhsbsa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SearchPage {
    WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterJobPreferences(String jobTitle, String location) {
        driver.findElement(By.id("keyword")).sendKeys(jobTitle);
        driver.findElement(By.id("location")).sendKeys(location);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(),'" + location + "')]")));
        suggestion.click();
    }

    public void clickSearch() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement search = wait.until(ExpectedConditions.elementToBeClickable(By.id("search")));
        search.click();
    }

    public void assertSearchResultsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // This selector targets each job listing container
        List<WebElement> results = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("li.search-result")
        ));

        if (results.isEmpty()) {
            throw new AssertionError("No search results found.");
        }
    }

    public void assertNoSearchResultsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        List<WebElement> results = driver.findElements(By.cssSelector("li.search-result"));

        if (!results.isEmpty()) {
            throw new AssertionError("Expected no search results, but some were found.");
        }
    }

    public void sortByDatePosted() {
        WebElement sortDropdown = driver.findElement(By.id("sort")); // Adjust if needed
        Select select = new Select(sortDropdown);
        select.selectByVisibleText("Date Posted (newest)");

        // Wait for the first result to update
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("li.search-result")
        ));
    }

    public void assertResultsSortedByDate() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> results = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("li.search-result")));

        List<LocalDate> postedDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

        for (WebElement result : results) {
            String text = result.getText();

            Optional<String> dateLine = Arrays.stream(text.split("\n"))
                    .filter(line -> line.toLowerCase().contains("date posted"))
                    .findFirst();

            if (dateLine.isPresent()) {
                String dateStr = dateLine.get().replace("Date posted:", "").replace("date posted:", "").trim();
                try {
                    LocalDate date = LocalDate.parse(dateStr, formatter);
                    postedDates.add(date);
                } catch (DateTimeParseException e) {
                    throw new AssertionError("Failed to parse date: " + dateStr);
                }
            } else {
                throw new AssertionError("No 'Date posted' found in result:\n" + text);
            }
        }

        for (int i = 0; i < postedDates.size() - 1; i++) {
            if (postedDates.get(i).isBefore(postedDates.get(i + 1))) {
                throw new AssertionError("Results are not sorted by newest date:\n" + postedDates);
            }
        }
    }


    public void acceptCookiesIfPresent() {
        try {
            WebElement acceptButton = driver.findElement(By.id("nhsuk-cookie-banner__link_accept_analytics"));
            if (acceptButton.isDisplayed()) {
                acceptButton.click();
            }
        } catch (NoSuchElementException ignored) {
            // Cookie banner not present—carry on
        }
    }

    public void updateJobPreferences(String jobTitle, String location) {
        clearPreferences();
        enterJobPreferences(jobTitle, location);
    }

    public void clearPreferences() {
        WebElement keywordInput = driver.findElement(By.id("keyword"));
        WebElement locationInput = driver.findElement(By.id("location"));

        keywordInput.clear();
        locationInput.click();

        locationInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        locationInput.sendKeys(Keys.DELETE);
    }

    public boolean isSortOrderResetToDefault() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By dropdownLocator = By.id("sort");

        try {
            // Wait for the dropdown to be present and visible
            WebElement sortDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(dropdownLocator));
            sortDropdown = wait.until(ExpectedConditions.visibilityOf(sortDropdown));

            // Refresh the reference to avoid staleness
            Select select = new Select(sortDropdown);
            String selectedOption = select.getFirstSelectedOption().getText();
            return selectedOption.contains("Best Match");

        } catch (StaleElementReferenceException e) {
            // Retry once if the element went stale
            WebElement refreshedDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(dropdownLocator));
            Select select = new Select(refreshedDropdown);
            String selectedOption = select.getFirstSelectedOption().getText();
            return selectedOption.contains("Best Match");
        }
    }
}