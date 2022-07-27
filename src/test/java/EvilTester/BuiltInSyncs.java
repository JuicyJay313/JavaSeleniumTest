package EvilTester;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BuiltInSyncs {

    private static WebDriver driver;

    @BeforeAll
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
    }


    @Test
    public void syncOnPageLoad(){
        driver = new ChromeDriver();
        // Loading a page has an automatic sync on contents loaded.
        driver.get("https://eviltester.github.io/synchole/collapseable.html");

        // WebDriver automatically waits for the page to load.
        Assertions.assertEquals("SyncHole", driver.findElement(By.cssSelector("h2")).getText());
    }

    @Test
    public void submitResultsPageLoadWait(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/form.html");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("John");
        // Submit actions implicitly tells the WebDriver to wait for the results.
        username.submit();

        // This will pass regardless of the time it takes for the DOM element to appear.
        Assertions.assertEquals("Thanks For Your Submission", driver.findElement(By.id("thanks")).getText());
    }

    @Test
    public void noSyncOnJavaScriptExecutionPopulation() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/form.html");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("John");
        username.submit();

        Assertions.assertEquals("Thanks For Your Submission", driver.findElement(By.id("thanks")).getText());

        // Explicit wait
        Thread.sleep(2000);

        // Only works because of the above Wait since WebDriver does not wait for a Javascript Execution to populate a DOM element.
        Assertions.assertEquals("John", driver.findElement(By.cssSelector("li[data-name='username']")).getAttribute("data-value"));
    }

    @AfterEach
    public void closeDriver(){
        driver.close();
    }

    @AfterAll
    public static void quitDriver(){
        driver.quit();
    }
}
