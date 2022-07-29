package EvilTester;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitingForJSAbstraction {

    private static WebDriver driver;

    @BeforeAll
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
    }

    @Test
    public void waitUsingExpectedConditions(){
        driver = new ChromeDriver();
        driver.get("https://eviltester.github.io/synchole/messages.html");

        // Building an abstraction layer to help debug Javascript errors
        GenericJSWaiter wait = new GenericJSWaiter(driver,20);

        // This block ...
        wait.forJSConditionToEvaluateTo(
                "window.liveMessages==0 && window.totalRequestsMade>0", true);

        // ... converts this block more effectively
//        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
//                ExpectedConditions.jsReturnsValue(
//                        "return (window.totalMessagesReceived>0 &&" +
//                                "window.renderingQueueCount==0 ? 'true' : null)"));

        final Long totalRequestSent = (Long)((JavascriptExecutor)driver).executeScript("return window.totalRequestsMade");

        // 2nd effective conversion ...
        wait.forJSConditionToEvaluateTo("window.totalMessagesReceived>0 && window.renderingQueueCount==0", true );

        // ... for this block
//        ExpectedCondition renderingQueueIsEmpty = driver ->{
//            JavascriptExecutor js = (JavascriptExecutor)driver;
//            String value = (String)js.executeScript(
//                    "return (window.totalMessagesReceived>0 &&" +
//                            "window.renderingQueueCount==0 ? 'true' : null)");
//            return value!=null;
//        };
//        new WebDriverWait(driver, Duration.ofSeconds(20)).until(renderingQueueIsEmpty);

        final Long totalMessagesReceived = (Long)((JavascriptExecutor)driver).executeScript("return window.totalMessagesReceived");

        final Long totalDisplayMessages = (Long)((JavascriptExecutor)driver).executeScript("return window.allMessages.length");

        Assertions.assertEquals(totalDisplayMessages,
                (totalRequestSent*2)+totalMessagesReceived);


    }

    @AfterEach
    public void closeDriver(){
        driver.close();
    }

    @AfterAll
    public static void quitDriver(){
        driver.quit();
    }

    private class GenericJSWaiter {
        private final WebDriverWait wait;

        public GenericJSWaiter(WebDriver driver, int timeOut){
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        }

        public void forJSConditionToEvaluateTo(String javascript, boolean value) {
            wait.until(EvaluateBooleanJSCondition(javascript, value));
        }

        // Creating a Custom ExpectedCondition to catch Javascript syntax error and throw an exception
        private ExpectedCondition<Boolean> EvaluateBooleanJSCondition(String javascript, boolean value) {
            return new ExpectedCondition<Boolean>(){
                @NullableDecl
                @Override
                public Boolean apply(@NullableDecl WebDriver webDriver){
                    try {
                        Boolean jsValue = (Boolean) ((JavascriptExecutor) webDriver).executeScript(
                                "return " + javascript);
                        return jsValue == value;
                    }catch (JavascriptException e){
                        throw new RuntimeException("Exception evaluating - return " + javascript, e);
                    }
                }
            };
        }
    }
}
