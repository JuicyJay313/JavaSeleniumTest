package EvilTester;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Clock;
import java.time.Duration;

public class WaitingForAbsentElement {

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

    @Test
    public void clickWithSlowLoadable(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/buttons.html");

        ButtonComponent startButton = new ButtonComponent(driver, By.id("button00"));
        // the get method (inherited by the SlowLoadableComponent class) provides a built-in wait mechanism ...
        //  ... that will wait for a DOM element even if not present at first.
        startButton.get();
        startButton.click();

        ButtonComponent buttonOne = new ButtonComponent(driver, By.id("button01"));
        buttonOne.get();
        buttonOne.click();

        ButtonComponent buttonTwo = new ButtonComponent(driver, By.id("button02"));
        buttonTwo.get();
        buttonTwo.click();

        ButtonComponent buttonThree = new ButtonComponent(driver, By.id("button03"));
        buttonThree.get();
        buttonThree.click();

        WebElement buttonMessage = driver.findElement(By.id("buttonmessage"));
        Assertions.assertEquals("All Buttons Clicked", buttonMessage.getText());

    }

    private class ButtonComponent extends SlowLoadableComponent<ButtonComponent>{
        private final By locator;
        private final WebDriver myDriver;

        public ButtonComponent(WebDriver myDriver, final By elementLocator){
            super(Clock.systemDefaultZone(), 10);
            this.myDriver = myDriver;
            this.locator = elementLocator;
        }


        public void click(){
            this.myDriver.findElement(this.locator).click();
        }

        @Override
        protected void load() {

        }

        @Override
        protected void isLoaded() throws Error {
            try {
                WebElement elem = myDriver.findElement(this.locator);
                if (elem.isDisplayed() && elem.isEnabled()) {
                    return;
                } else {
                    throw new Error(String.format("Button %s not ready", this.locator));
                }
            }catch (Exception e){
                throw new Error(e.getMessage(), e);
            }

        }
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
