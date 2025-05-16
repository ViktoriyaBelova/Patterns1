package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.DataGenerator.Registration;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.*;

public class DeliveryFormTest {

    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should Register Delivery")
    void shouldRegisterDelivery() {

        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstDeliveryDate = DataGenerator.generateDate(4);
        var secondDeliveryDate = DataGenerator.generateDate(7);

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstDeliveryDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.text("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text("Встреча успешно запланирована на " + firstDeliveryDate));

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondDeliveryDate);
        $$("button").findBy(Condition.text("Запланировать")).click();
        $("[data-test-id='replan-notification']")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button").click();

        $("[data-test-id='success-notification']")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text("Встреча успешно запланирована на " + secondDeliveryDate));
    }
}
