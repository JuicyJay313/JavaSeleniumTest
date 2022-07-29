package EvilTester;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitingForJavascript {

    private static WebDriver driver;

    @BeforeAll
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
    }


    @Test
    public void waitUsingExpectedConditions(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/messages.html");


        // Synchronize on the internal state of the application first for a more robust synchronization ...
       new WebDriverWait(driver, Duration.ofSeconds(20)).until(
               ExpectedConditions.jsReturnsValue(
                       "return (window.totalMessagesReceived>0 &&" +
                       "window.renderingQueueCount==0 ? 'true' : null)"));

       // ... and then wait for the messages to appear on the GUI (Graphical User Interface) ...
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("messagecount"),
                        "Message Count: 0 : 0"));

       //  ... before making the assertion on the GUI.
       Assertions.assertEquals("Message Count: 0 : 0",
               driver.findElement(By.id("messagecount")).getText());

    }

    @Test
    public void waitUsingJavaClosures(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/messages.html");

        // Java Closure
        // This block executes the same test in a more performant approach
        // but in a less readable form then ...
        ExpectedCondition renderingQueueIsEmpty = driver ->{
            JavascriptExecutor js = (JavascriptExecutor)driver;
            String value = (String)js.executeScript(
                    "return (window.totalMessagesReceived>0 &&" +
                    "window.renderingQueueCount==0 ? 'true' : null)");
            return value!=null;
        };

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(renderingQueueIsEmpty);

        /// ... this block
//        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
//                ExpectedConditions.jsReturnsValue(
//                        "return (window.totalMessagesReceived>0 &&" +
//                                "window.renderingQueueCount==0 ? 'true' : null)"));

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("messagecount"),
                        "Message Count: 0 : 0"));

        Assertions.assertEquals("Message Count: 0 : 0",
                driver.findElement(By.id("messagecount")).getText());

    }

    @Test
    public void waitUsingAsyncAsTimeout(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/messages.html");

        JavascriptExecutor exec = (JavascriptExecutor)driver;
        exec.executeAsyncScript(
                "window.onMessageQueueEmpty(arguments[arguments.length-1])");

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("messagecount"),
                        "Message Count: 0 : 0"));

        Assertions.assertEquals("Message Count: 0 : 0",
                driver.findElement(By.id("messagecount")).getText());

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
