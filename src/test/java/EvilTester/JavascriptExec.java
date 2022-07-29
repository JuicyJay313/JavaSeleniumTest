package EvilTester;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class JavascriptExec {
    private static WebDriver driver;

    @BeforeAll
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
    }


    @Test
    public void manipulatePageWithJS(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/collapseable.html");

        final WebElement heading = driver.findElement(By.cssSelector("section.synchole > h2"));

        // cast the driver to Javascript
        JavascriptExecutor exec = (JavascriptExecutor) driver;

        // arguments[0] is the actual heading.
        // arguments[1] is the argument we are passing in the script.
        exec.executeScript("arguments[0].innerText=arguments[1];", heading, "A New Heading Text");

        Assertions.assertEquals("A New Heading Text", heading.getText());
    }

    @Test
    public void manipulatePageWithAsyncJS(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/collapseable.html");

        final WebElement heading = driver.findElement(By.cssSelector("section.synchole > h2"));

        // cast the driver to Javascript
        JavascriptExecutor exec = (JavascriptExecutor) driver;

        // Asynchronous would fail unless we specifically call the callback function
        exec.executeAsyncScript(
                "window.setTimeout(function(arguments){" + // Calling a timeout ...
                "arguments[0].innerText=arguments[1];" +
                // Callback function is set to the last argument **** Not good practice. Only to demonstrate how to make it pass. ****
                "arguments[arguments.length-1]();" +
                "},5000, arguments);", // ... of 5000 milliseconds (5 seconds)
                heading, "A New Heading Text");

        Assertions.assertEquals("A New Heading Text", heading.getText());
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
