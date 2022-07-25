package Test01;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.Test;

public class InitialTest {

    private static WebDriver driver;

    @BeforeAll
    public static void createDriver() {

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        driver.get("https://formy-project.herokuapp.com/");
    }

    @Test
    public void anInitialTest() {

        Assertions.assertEquals("Formy", driver.getTitle());
    }

    @AfterAll
    public static void closeDriver() {

        driver.quit();
    }
}
