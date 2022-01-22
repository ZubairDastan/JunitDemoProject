package C1;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static java.lang.Thread.sleep;

public class TestTitleImage {
    WebDriver driver;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headed");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

    }

    @Test
    public void getTitle() throws InterruptedException {
        driver.get("https://demoqa.com");
        String title=driver.getTitle();
        System.out.println(title);
        Assert.assertTrue(title.contains("ToolsQA"));
    }

    @Test
    public void checkImage() {
        driver.get("https://demoqa.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        Boolean status = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/images/Toolsqa.jpg']"))).isDisplayed();
        Assert.assertEquals(status, true);

    }

    @Test
    public void textBox(){
        driver.get("https://demoqa.com/elements");
        driver.findElement(By.xpath("//span[contains(text(),'Text Box')]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id=userName]"))).sendKeys("Zubair");
        driver.findElement(By.xpath("//button[@id='submit']")).click();
        String text= driver.findElement(By.cssSelector("[id=name]")).getText();
        Assert.assertTrue(text.contains("Zubair"));
    }

    @Test
    public void mouseClickButtons(){
        driver.get("https://demoqa.com/buttons");
        Actions action = new Actions(driver);
        List<WebElement> list= driver.findElements(By.cssSelector("button"));
        action.doubleClick(list.get(1)).perform();
        String text= driver.findElement(By.id("doubleClickMessage")).getText();
        Assert.assertTrue(text.contains("You have done a double click"));

        action.contextClick(list.get(2)).perform();
        String text2= driver.findElement(By.id("rightClickMessage")).getText();
        Assert.assertTrue(text2.contains("You have done a right click"));

        list.get(3).click();
        String text3= driver.findElement(By.id("dynamicClickMessage")).getText();
        Assert.assertTrue(text3.contains("You have done a dynamic click"));

    }

    @Test
    public void handleAlerts() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id(("alertButton"))).click();
        driver.switchTo().alert().accept();
        //driver.switchTo().alert().dismiss();
        driver.findElement(By.id(("promtButton"))).click();
        driver.switchTo().alert().sendKeys("Zubair");
        sleep(2000);
        driver.switchTo().alert().accept();
        String text= driver.findElement(By.id("promptResult")).getText();
        Assert.assertTrue(text.contains("Zubair"));

    }

    @Test
    public void datePicker(){
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("05/08/1993");
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.ENTER);

    }

    @Test
    public void selectDropdown(){
        driver.get("https://demoqa.com/select-menu");
        Select color=new Select(driver.findElement(By.id("oldSelectMenu")));
        color.selectByValue("1");
        Select cars=new Select(driver.findElement(By.id("cars")));
        if (cars.isMultiple()) {
            cars.selectByValue("volvo");
            cars.selectByValue("audi");
        }
    }

    @Test
    public void tabHandler() throws InterruptedException {
        driver.get("https://demoqa.com/links");
        driver.findElement(By.id("simpleLink")).click();
        sleep(5000);

        ArrayList<String> w = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(w.get(1));
        System.out.println("New tab title: " + driver.getTitle());

        Boolean status = driver.findElement(By.xpath("//img[@src='/images/Toolsqa.jpg']")).isDisplayed();
        Assert.assertEquals(true,status);
        driver.close();
        driver.switchTo().window(w.get(0));
    }

    @Test
    public void windowHandler(){
        driver.get("https://demoqa.com/browser-windows");

        driver.findElement(By.id(("windowButton"))).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                driver.switchTo().window(ChildWindow);
                String text= driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }

        }
    }

    @Test
    public void modalDialogHandler(){

        driver.get("https://demoqa.com/modal-dialogs");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("showSmallModal")));
        element.click();
        driver.findElement(By.id("closeSmallModal")).click();

    }

    @Test
    public void webTables(){
        driver.get("https://demoqa.com/webtables");
        driver.findElement(By.xpath("//span[@id='edit-record-1']//*[@stroke='currentColor']")).click();
        driver.findElement(By.id("submit")).click();

    }

    @Test
    public void scrapeData(){
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i=0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num["+i+"] "+ cell.getText());

            }
        }
    }

    @Test
    public void imageUploader(){
        driver.get("https://demoqa.com/upload-download");
        WebElement uploadElement = driver.findElement(By.id("uploadFile"));
        uploadElement.sendKeys("C:\\Users\\DevPC\\Downloads\\img-1.jpg");

        String text= driver.findElement(By.id("uploadedFilePath")).getText();
        Assert.assertTrue(text.contains("img-1.jpg"));
    }

    @Test
    public void hoverMouse() throws InterruptedException {
        driver.get("https://green.edu.bd/");
        WebElement mainMenu = driver.findElement(By.xpath("//a[@class='dropdown-toggle'][contains(text(),'About Us')]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(mainMenu).perform();
        Thread.sleep(3000);
        WebElement subMenu = driver.findElement(By.xpath("//li[@id='menu-item-325']//a[contains(text(),'History')]"));
        actions.moveToElement(subMenu);
        actions.click().build().perform();
    }

    @Test
    public void keyboardEvents() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions action = new Actions(driver);
        action.moveToElement(searchElement);
        action.keyDown(Keys.SHIFT);
        action.sendKeys("Selenium Webdriver").keyUp(Keys.SHIFT).doubleClick().contextClick().perform();
        Thread.sleep(5000);
    }

    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com");
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, DestFile);
    }

    public static void readFromExcel(String filePath,String fileName,String sheetName) throws IOException {
        File file = new File(filePath+"\\"+fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if(fileExtensionName.equals(".xls")){
            workbook = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
        for (int i = 0; i < rowCount+1; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                System.out.print((row.getCell(j).getStringCellValue()) +" || ");
            }
            System.out.println();
        }
    }

    @Test
    public void readExcelFile() throws IOException {
        readFromExcel("D:\\QA_projects", "TestExcel.xls", "Sheet1");
    }

    @After
    public void finishTest() {
        driver.close();
    }
}
