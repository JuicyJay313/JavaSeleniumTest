package EvilTester;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExplicitWaits {

    private static WebDriver driver;

    @BeforeAll
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
    }


    @Test
    public void waitOnClickable(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/buttons.html");

        Assertions.assertEquals("Buttons", driver.findElement(By.cssSelector("h1")).getText());

        // Instantiate an WebDriverWait (Explicit Wait) with a maximum duration.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Waits either 10 seconds or for the DOM element to appear.
        WebElement button0 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button00")));
        button0.click();

        // Waits for all three subsequent buttons to be clickable.
        WebElement button1 = wait.until(ExpectedConditions.elementToBeClickable(
                driver.findElement(By.id("button01"))));
        button1.click();

        WebElement button2 = wait.until(ExpectedConditions.elementToBeClickable(
                driver.findElement(By.id("button02"))));
        button2.click();

        WebElement button3 = wait.until(ExpectedConditions.elementToBeClickable(
                driver.findElement(By.id("button03"))));
        button3.click();

        // Verify that the p1 message has been changed after all buttons have been clicked.
        WebElement buttonMessage = driver.findElement(By.id("buttonmessage"));
        Assertions.assertEquals("All Buttons Clicked", buttonMessage.getText());

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
