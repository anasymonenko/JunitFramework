import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.concurrent.TimeUnit;

public class BasketCheckoutTest {

    private static WebDriver driver;

    public double parseToDouble (WebElement element){
        double doubleElement = Double.parseDouble(element.getText().replace(",",".")
                .replace("€","").trim());
        return doubleElement;
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
        Assertions.assertEquals(parseToDouble(itemTotal), parseToDouble(basketTotal),
                "Totals are not equal!");

        WebElement checkoutButton = driver.findElement(By.xpath("//div[@class = 'checkout-head-wrap']//" +
                "a[@class = 'checkout-btn btn original-bucket']"));
        checkoutButton.click();

        WebElement emailInputField = driver.findElement(By.xpath("//input[@name = 'emailAddress']"));
        emailInputField.sendKeys("test@user.com");

        WebElement orderSummaryTotal = driver.findElement(By.xpath("//dd[@class = 'text-right total-price']"));
        WebElement orderSummarySubtotal = driver.findElement(By.xpath("//dd[@class = 'text-right']"));
        Assertions.assertEquals(parseToDouble(orderSummaryTotal), parseToDouble(orderSummarySubtotal),
                "Order Summary totals are not equal!");
        }

    @AfterAll
    public static void browserTearDown(){
        driver.quit();
        driver=null;
    }
}
