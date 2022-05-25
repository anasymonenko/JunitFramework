import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasketCheckoutTest {

    private static WebDriver driver;

    public static final String BOOK_PRICE = "9.38";

    public double parseToDouble (WebElement element){
        return Double.parseDouble(element.getText().replace(",",".")
                .replace("€","").trim());
    }

    @BeforeAll
    public static void browserSetUp() {
        System.setProperty("webdriver.chrome.driver", "src/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.bookdepository.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    void checkTotalWithSubtotal() {
        WebElement searchField = driver.findElement(By.xpath("//input[@name = 'searchTerm']"));
        searchField.sendKeys("flowers for algernon");

        WebElement searchButton = driver.findElement(By.xpath("//span[text()='Search']"));
        searchButton.click();

        WebElement bookTitle = driver.findElement(By.xpath("//meta[@content = '9780156030083']/..//a[contains(text()," +
                " 'Flowers for Algernon')]"));
        bookTitle.click();

        WebElement addToBasketButton = driver.findElement(By.xpath("//div[@class = 'btn-wrap']/a[@href = " +
                "'/basket/addisbn/isbn13/9780156030083']"));
        addToBasketButton.click();

        WebElement basketCheckoutButton = driver.findElement(By.xpath("//div[@class = 'modal-dialog modal-md']" +
                "//a[@class ='btn btn-primary pull-right continue-to-basket string-to-localize link-to-localize']"));
        basketCheckoutButton.click();

        WebElement itemTotal = driver.findElement(By.xpath("//p[@class = 'item-total']"));
        WebElement basketTotal = driver.findElement(By.xpath("//dl[@class = 'total']/dd"));

        assertAll("Check Order Subtotal and Order Total ",
                () -> assertEquals(Double.parseDouble(BOOK_PRICE), parseToDouble(itemTotal), "Basket Total is incorrect"),
                () -> assertEquals(Double.parseDouble(BOOK_PRICE), parseToDouble(basketTotal), "Basket Subtotal is incorrect")
        );

        WebElement checkoutButton = driver.findElement(By.xpath("//div[@class = 'checkout-head-wrap']//" +
                "a[@class = 'checkout-btn btn original-bucket']"));
        checkoutButton.click();

        WebElement emailInputField = driver.findElement(By.xpath("//input[@name = 'emailAddress']"));
        emailInputField.sendKeys("test@user.com");

        WebElement orderSummaryTotal = driver.findElement(By.xpath("//dd[@class = 'text-right total-price']"));
        WebElement orderSummarySubtotal = driver.findElement(By.xpath("//dd[@class = 'text-right']"));

        assertAll("Check the following final review",
                () -> assertEquals(Double.parseDouble(BOOK_PRICE),parseToDouble(orderSummaryTotal), "Order Total is incorrect"),
                () -> assertEquals(Double.parseDouble(BOOK_PRICE),parseToDouble(orderSummarySubtotal), "Order Subtotal is incorrect")
        );
        }

    @AfterAll
    public static void browserTearDown(){
        driver.quit();
        driver=null;
    }
}
