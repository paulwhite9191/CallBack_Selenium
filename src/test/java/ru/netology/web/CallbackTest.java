package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;


import static org.junit.jupiter.api.Assertions.assertEquals;


class CallbackTest {


    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--remote-allow-origins=*");
        option.addArguments("--disable-dev-shm-usage");
        option.addArguments("--no-sandbox");
        option.addArguments("--headless");
        driver = new ChromeDriver(option);
        driver.get("http://localhost:9999/");

    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestPositive() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Арбузов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }


    @Test
    void shouldTestNegativeEmptyFieldName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input"));
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }


    @Test
    void shouldTestNegativeLatinLettersName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Xenomorph Igorevich");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }


    @Test
    void shouldTestNegativeEmptyFieldPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Арбузов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input"));
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldTestNegativePhoneNumberWithoutPrefix() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Арбузов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("9270000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldTestNegativeNotMarkedCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Арбузов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }

}


