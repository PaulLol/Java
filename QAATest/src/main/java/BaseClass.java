import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;

public class BaseClass extends InitializeClass{

    private String url ="http://prestashop-automation.qatestlab.com.ua/ru/";
    public boolean testBool = true;
    private static final Logger LOGGER =Logger.getLogger(BaseClass.class.getSimpleName());

    @Before
    public void setUp()
    {
        initializeDriver();
        open(url);
        LOGGER.info("Page opened");
    }

    @After
    public  void tearDown()
    {
        delay(2000);
        getDriver().quit();
        LOGGER.info("Page closed");
    }

    @Test
    public void testAll()
    {
        LOGGER.info("Сurrency matching test started");
        String elementUsd = searchText("/html/body/main/header/nav/div/div/div[1]/div[2]/div[2]/div/span[2]");
        List<WebElement> article_All = listElement("/html/body/main/section/div/div/section/section/section/div", "article");
        for (int i = 1;i<=article_All.size();i++)
        {
            String stringElement = searchText("/html/body/main/section/div/div/section/section/section/div/article["+i+"]/div/div[1]/div/span");
            if (stringElement.charAt(stringElement.length() -1) != elementUsd.charAt(elementUsd.length() -1)) testBool =false;

            try{
                Assert.assertTrue(testBool);
            }catch (Exception e) { LOGGER.error("Error currency matching test"); }
        }

        LOGGER.info("Currency matching test passed successfully");

        clickElement("/html/body/main/header/nav/div/div/div[1]/div[2]/div[2]/div/a");
        clickElement("/html/body/main/header/nav/div/div/div[1]/div[2]/div[2]/div/ul/li[3]/a");
        LOGGER.info("Change to USD");

        enterText("/html/body/main/header/div/div/div[1]/div[2]/div/div[2]/form/input[2]","dress");
        LOGGER.info("Search product, dress");
        delay(2000);

        LOGGER.info("Test of quantity product started");
        List<WebElement> articleAll = listElement("/html/body/main/section/div/div/section/section/div[3]/div/div[1]", "article");
        String elementGoods = searchText("/html/body/main/section/div/div/section/section/div[1]/div/div[1]/p");
        String articleAllSize = Integer.toString(articleAll.size());
        elementGoods = elementGoods.substring(9, elementGoods.length()-1);

        try
        {
            Assert.assertEquals(articleAllSize, elementGoods);
        }catch (Exception e){LOGGER.error("Error test of quantity product");}
        LOGGER.info("Test of quantity product passed successfully");


        LOGGER.info("Test of currency of all goods found started");
        for(int i=1;i<=articleAll.size();i++)
        {
            String stringElement = searchText("/html/body/main/section/div/div/section/section/div[3]/div/div[1]/article["+i+"]/div/div[1]/div/span");
            if(stringElement.charAt(stringElement.length() -1) !='$') testBool=false;
            try{
                Assert.assertTrue(testBool);
            }catch (Exception e){LOGGER.error("Error test of currency of all goods found");}
        }
        LOGGER.info("Test of currency of all goods found passed successfully");

        clickElement("/html/body/main/section/div/div/section/section/div[1]/div/div[2]/div/div");
        clickElement("/html/body/main/section/div/div/section/section/div[1]/div/div[2]/div/div/div/a[5]");
        LOGGER.info("Change sort");
        delay(2000);

        LOGGER.info("Test sort of goods started");
        ArrayList<Float> prices = new ArrayList<Float>();
        for(int i=1;i<=articleAll.size();i++) {
            String articlePrice = searchText("/html/body/main/section/div/div/section/section/div[3]/div/div[1]/article["+i+"]/div/div[1]/div/span[1]");
            int indexCh = articlePrice.indexOf(' ');
            articlePrice = articlePrice.substring(0, indexCh);
            articlePrice = articlePrice.replace(',', '.');
            float price = Float.parseFloat(articlePrice);
            prices.add(price);
        }
        try
        {
            for(int i=0;i<=prices.size();i++)
            {
                if(prices.get(i+1) > prices.get(i)) testBool = false;
            }
        }catch (Exception IndexOutOfBoundsException){}

        try
        {
            Assert.assertTrue(testBool);
        }catch (Exception e){LOGGER.error("Error sort of goods");}
        LOGGER.info("Test sort of goods passed successfully");


        LOGGER.info("Test of discount started");
        for(int i=1;i<=articleAll.size();i++)
        {
            List<WebElement> ListQMark = listElement("/html/body/main/section/div/div/section/section/div[3]/div/div[1]/article["+i+"]/div/div[1]/div", "span");
            if(ListQMark.size() > 1)
            {
                String discountString = searchText("/html/body/main/section/div/div/section/section/div[3]/div/div[1]/article["+i+"]/div/div[1]/div/span[2]");
                int indexPS = discountString.indexOf('%');
                int discountInt = Integer.parseInt(discountString.substring(1,indexPS));
                String articlePriceAfter = searchText("/html/body/main/section/div/div/section/section/div[3]/div/div[1]/article["+i+"]/div/div[1]/div/span[3]");
                int indexCh = articlePriceAfter.indexOf(' ');
                articlePriceAfter = articlePriceAfter.substring(0,indexCh);
                articlePriceAfter=articlePriceAfter.replace(',','.');
                double priceAfter = Double.parseDouble(articlePriceAfter);
                double priceWithDiscount = Math.round((prices.get(i-1) - prices.get(i-1)/100*discountInt) *100d)/100d;
                if(priceWithDiscount != priceAfter) testBool = false;
            }
        }
        try
        {
            Assert.assertTrue(testBool);
        }catch (Exception e){LOGGER.error("Error test of discount");}
        LOGGER.info("Test of discount passed successfully");
    }
}
