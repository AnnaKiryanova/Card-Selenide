package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.holdBrowserOpen;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class RegistrationTest {
    public static String getLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru")));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        holdBrowserOpen = true;
    }

    @Test
    void shouldBeValidCity() {
        String meetingDate = getLocalDate(19);
        $("[placeholder=Город]").setValue("Санкт-Петербург");
        $("[data-test-id=\"date\"] span.input__box [placeholder=\"Дата встречи\"").doubleClick().sendKeys(meetingDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + meetingDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNotValidCity() {
        String planningDate = getLocalDate(5);
        $x("//input[@placeholder=\"Город\"]").val("Павловск");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иванов Иван");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityEnglish() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Moscow");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityEmpty() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Поле обязательно")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityWithSpecSymbols() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Санкт_Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameWithYO() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Семён Семёнов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldTestDoubleFirstName() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов-Петров");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameWithNum() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("14567 1564");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameEnglish() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Ivan Ivanov");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameWithSpecSymbols() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов!");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithoutPlus() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("89998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithOneNumber() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+7999888776");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithSpecSymbols() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+7(999)-888-77 66");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneEmpty() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("обязательно для заполнения")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNextDayMeeting() {
        String planningDate = getLocalDate(1);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPlus0days() {
        String planningDate = getLocalDate(0);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestMinus5Days() {
        String planningDate = getLocalDate(-5);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestFebruaryDays() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys("30.02.2023");
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Неверно введена дата")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestUncheckedBox() {
        String planningDate = getLocalDate(4);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").doubleClick();
        $x("//*[@class=\"button__text\"]").click();
        $(".input_invalid[data-test-id=\"agreement\"]").should(exist);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldTestCheckedCheckedBox() {
        String planningDate = getLocalDate(4);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $("[data-test-id=agreement].checkbox_checked").should(exist);
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }
}