package Test01;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DynamicLoading {
    private static WebDriver driver;

    @BeforeAll
    public static void createDriver() {

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        driver.get("http://the-internet.herokuapp.com/dynamic_loading/2");
    }


    @Test
    public void waitForText() {
        Assertions.assertEquals("Hello World!", getLoadedData(driver));
    }

    @AfterAll
    public static void closeDriver() {

        driver.quit();
    }

    public static String getLoadedData(WebDriver driver) {
        WebElement startButton = driver.findElement(By.xpath("//*[@id=\"start\"]/button"));
        startButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement finishText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));

        return finishText.getText();
    }
}
