package demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    private static RemoteWebDriver driver;
    private WebDriverWait wait;

    public TestCases() throws MalformedURLException {
        System.out.println("Constructor: TestCases");
        WebDriverManager.chromedriver().timeout(120).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        // Set browser to maximize and wait
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 30);
    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        if (driver != null) {
            driver.quit();
        }
    }

    public void testCase01() {
        System.out.println("Start Test case: testCase01");
        driver.get("https://xflix-qa.vercel.app/");

        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("xflix")) {
            System.out.println("Testcase01: Pass");
        }else{
            System.out.println("Testcase01: Fail");
        }
        System.out.println("End Test case: testCase01");
    }

    public void testCase02() {
        System.out.println("Start Test case: testCase01");
        driver.get("https://xflix-qa.vercel.app/");

        WebElement searchBox = driver.findElement(By.className("search-input"));
        searchBox.sendKeys("frameworks");

        List<WebElement> titleElements = driver.findElements(By.className("video-title"));

        for (WebElement titleElement : titleElements) {
            String text = titleElement.getText();
            if (text.contains("frameworks")) {
                System.out.println("Testcase02:Valid Search: Pass");
            }else{
                System.out.println("Testcase02:Valid Search: Fail");
            }
        }
        searchBox.clear();
        searchBox.sendKeys("selenium");

        WebElement noSearchFound =driver.findElement(By.className("no-search-txt"));
        if (noSearchFound.isDisplayed()) {
            System.out.println("Testcase02:Invalid Search: Pass");
        } else {
            System.out.println("Testcase02:Invalid Search: Fail");
        }
   
        System.out.println("End Test case: testCase02");
    }

    public void testCase03() {
        System.out.println("Start Test case: testCase03");
        driver.get("https://xflix-qa.vercel.app/");

        List<WebElement> titleElementsBefore = driver.findElements(By.className("video-title"));
        List<String> titlesBefore = new ArrayList<>();
        for (WebElement titleElementBefore : titleElementsBefore) {
            String text = titleElementBefore.getText();
            titlesBefore.add(text);   
        }

        WebElement sortDropdown = driver.findElement(By.id("sortBySelect"));

        Select select = new Select(sortDropdown);
        select.selectByVisibleText("Sort By: View Count");

        List<WebElement> titleElementsAfter = driver.findElements(By.className("video-title"));
        List<String> titlesAfter = new ArrayList<>();
        for (WebElement titleElementAfter : titleElementsAfter) {
            String text = titleElementAfter.getText();
            titlesBefore.add(text);   
        }
        if (!titlesAfter.equals(titlesBefore)) {
            System.out.println("TestCase03: PASS");
        }else{
            System.out.println("TestCase03: FAIL");
        }
      
        System.out.println("End Test case: testCase03");
    }


    public void testCase04() {
        System.out.println("Start Test case: testCase04");
        driver.get("https://xflix-qa.vercel.app/");
        WebElement uploadButton = driver.findElement(By.className("btn-upload"));
        uploadButton.click();

        WebElement uploadVideoButton = driver.findElement(By.className("btn-modal-upload"));
        uploadVideoButton.click();

        Alert alert1 = driver.switchTo().alert();
        String alertTextError = alert1.getText();

        if (alertTextError.contains("should not be empty")) {
            System.out.println("Testcase04 : Error alert text: PASS");

        }else{
            System.out.println("Testcase04 : Error alert text: FAIL");
        }
        alert1.accept();

        WebElement videoLink = driver.findElement(By.name("videoLink"));
        WebElement videoThumbnailImage = driver.findElement(By.name("previewImage"));
        WebElement videoTitle = driver.findElement(By.name("title"));

        videoLink.sendKeys("https://www.youtube.com/embed/GWfYHEuWh-k?si=UDwVjalFUBUEXxUT");   
        videoThumbnailImage.sendKeys("https://www.crio.do/blog/content/images/size/w1000/2020/08/Aug_04--1-.png");
        videoTitle.sendKeys("Embedded Youtube Video");

        WebElement genreDropdownElement = driver.findElement(By.id("genre-modal-dropdown"));
        Select genreDropdown = new Select(genreDropdownElement);
        genreDropdown.selectByVisibleText("Education");

        Select ageGroupDropdown = new Select(driver.findElement(By.id("age-modal-dropdown")));
        ageGroupDropdown.selectByVisibleText("7+");

        WebElement date = driver.findElement(By.name("releaseDate"));
        date.sendKeys("25/05/2026");
        uploadVideoButton.click();

        Alert alert2  = driver.switchTo().alert();
        String alertTextSuccess = alert2.getText();

        if (alertTextSuccess.equals("Video Posted Successfully!")) {
            System.out.println("Testcase04 : Success alert text: PASS");
        }else{
            System.out.println("Testcase04 : Success alert text: FAIL");
        }

        alert2.dismiss();
        
        System.out.println("End Test case: testCase04");
    }

    public void testCase05() {
        System.out.println("Start Test case: testCase01");
        driver.get("https://xflix-qa.vercel.app/");

        WebElement titleElement = driver.findElement(By.className("video-title"));
        titleElement.click();

        WebElement likeBUttonBefore = driver.findElement(By.className("btn-like"));
        String likeCountBefore = likeBUttonBefore.getText();
        likeBUttonBefore.click();

        String currentUrl = driver.getCurrentUrl();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(currentUrl);
        WebElement likeButtonAfter  = driver.findElement(By.className("btn-like"));
        String likeCountAfter = likeButtonAfter.getText();

        if (!likeCountAfter.equals(likeCountBefore)) {
            System.out.println("Testcase05 : PASS");
        } else {
            System.out.println("Testcase05 : FAIL");
        }


        System.out.println("End Test case: testCase01");
    }
}

