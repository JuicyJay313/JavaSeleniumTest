package TheInternet;

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

public class SynchronizedTests {
    private static WebDriver driver;

    @BeforeAll
    public static void createDriver() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    // Verify that a clicked button is clickable (enabled) again before asserting the returned value.
    @Test
    public void waitForEnabled() {
        driver.get("http://the-internet.herokuapp.com/dynamic_controls");
        Assertions.assertEquals("It's enabled!", getContentAfterClick(driver));
    }

    // Asserting the text of a DOM objects that appears only after a lengthy loading.
    @Test
    public void waitForText(){
        driver.get("http://the-internet.herokuapp.com/dynamic_loading/2");
        Assertions.assertEquals("Hello World!", getFinishState(driver));
    }

    @AfterAll
    public static void closeDriver() {

        driver.quit();
    }

    public static String getContentAfterClick(WebDriver driver) {
        WebElement enableButton = driver.findElement(By.xpath("//*[@id=\"input-example\"]/button"));
        enableButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));

        enableButton = wait.until(ExpectedConditions.elementToBeClickable(enableButton));
        if(enableButton.isEnabled()){
            WebElement disableText = driver.findElement(By.id("message"));
            return disableText.getText();
        }

        return null;
    }

    public static String getFinishState(WebDriver driver) {
        WebElement startButton = driver.findElement(By.xpath("//*[@id=\"start\"]/button"));
        startButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement finishText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));

        return finishText.getText();
    }
}
