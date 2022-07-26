package Test01;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DynamicControls {
    private static WebDriver driver;

    @BeforeAll
    public static void createDriver() {

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        driver.get("http://the-internet.herokuapp.com/dynamic_controls");
    }


    @Test
    public void waitForEnabled() {
        Assertions.assertEquals("It's enabled!", getLoadedData(driver));
    }

    @AfterAll
    public static void closeDriver() {

        driver.quit();
    }

    public static String getLoadedData(WebDriver driver) {
        WebElement enableButton = driver.findElement(By.xpath("//*[@id=\"input-example\"]/button"));
        enableButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));

        enableButton = wait.until(ExpectedConditions.elementToBeClickable(enableButton));
        if(enableButton.isEnabled()){
            WebElement disableText = driver.findElement(By.id("message"));
            return disableText.getText();
        }

        return null;

    }
}
