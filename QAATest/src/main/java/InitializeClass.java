import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

public class InitializeClass
{
    static WebDriver driver;
    public WebDriver getDriver() {
        return driver;
    }

    public WebDriver initializeDriver()
    {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Paul\\Desktop\\Test\\Drivers\\geckodriver-v0.24.0-win64\\geckodriver.exe");
        driver = new FirefoxDriver();
        return driver;
    }

    public void open(String url) {
        getDriver().get(url);
    }

    public void delay(long millisec)
    {
        try {
            Thread.sleep(millisec);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public String searchText(String xpath)
    {
        String string = getDriver().findElement(By.xpath(xpath)).getText();
        return string;
    }

    public WebElement searchElement (String xpath)
    {
        return getDriver().findElement(By.xpath(xpath));
    }

    public List<WebElement> listElement(String xpath, String tagName)
    {
        WebElement element = searchElement(xpath);
        List<WebElement> listElement = element.findElements(By.tagName(tagName));
        return listElement;
    }

    public WebDriver enterText( String xpath, String text)
    {
        getDriver().findElement(By.xpath(xpath)).sendKeys(text, Keys.ENTER);
        return getDriver();
    }

    public WebDriver clickElement (String xpath)
    {
        getDriver().findElement(By.xpath(xpath)).click();
        return getDriver();
    }

}
