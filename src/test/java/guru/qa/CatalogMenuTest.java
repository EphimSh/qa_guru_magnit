package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CatalogMenuTest {

    static {
        Configuration.pageLoadStrategy = "eager";
    }

    @BeforeEach
    void init() {
        open("https://dostavka.magnit.ru");
    }

    static Stream<Arguments> getMagnitCategoriesAndCatalogItems() {
        return Stream.of(Arguments.of("Овощи и фрукты",
                        List.of(TestData.fruitsAndVegetables)),
                Arguments.of("Молоко, сыр, яйца",
                        List.of(TestData.milkCheeseEggs)),
                Arguments.of("Мясо, птица",
                        List.of(TestData.meatAndPoultry)),
                Arguments.of("Готовая еда",
                        List.of(TestData.instantFood)),
                Arguments.of("Напитки",
                        List.of(TestData.drinks)),
                Arguments.of("Чай, кофе, какао",
                        List.of(TestData.teaCoffeeCacao)),
                Arguments.of("Хлеб, выпечка",
                        List.of(TestData.breadPastries))
        );
    }

    @Tag("web")
    @MethodSource("getMagnitCategoriesAndCatalogItems")
    @DisplayName("Magnit dostavka test")
    @ParameterizedTest(name = "Header catalog item: {0}, contains next categories: {1}")
    void catalogCategoryItemsTest(String catalogName, List<String> categoryMenuItems) {

        $$("[data-test-id = bottom-header-section] a").find(text(catalogName)).click();
        $$("[data-test-id=filter-category]")
                .filter(visible)
                .shouldHave(CollectionCondition.texts(categoryMenuItems));
    }

    @Tag("web")
    @ValueSource(strings = {
            "Фрукты",
            "Овощи",
            "Ягоды",
            "Зелень",
            "Грибы",
            "Орехи и сухофрукты"
    }
    )
    @ParameterizedTest(name = "Catalog menu contains {0} button")
    void fruitsAndVegetablesCatalogMenuButtonsTest(String categoryMenuItem) {
        $$("[data-test-id = bottom-header-section] a")
                .find(text("Овощи и фрукты")).click();
        $("[data-test-id=filter-category] ul").shouldHave(text(categoryMenuItem));
    }

    @Tag("web")
    @CsvSource(value = {
            "Сладкая радость| Молочный Шоколад Шоколад с Начинкой Темный шоколад " +
                    "Конфеты в коробках Батончики Мармелад",
            "Лучшие цены июня| Мясо, птица " + "Рыба, морепродукты " +
                    "Молоко, сыр " + "Заморозка " + "Бакалея " +
                    "Консервация " + "Кондитерские изделия " + "Снэки " +
                    "Напитки " + "Кофе, чай " + "Бытовая химия " + "Гигиена и уход",
            "Фестиваль мороженого | Эскимо " + "В рожке " + "В стаканчике " +
                    "Брикет, батончик " + "Фруктовый лёд " + "Семейное",
            "Сезон гриля | Мангалы, сервировка " + "Мясо, птица " + "Рыба и морепродукты " +
                    "Овощи " + "Соусы, пряности " + "Напитки",
            "Товары для дома, дачи, авто | Свечи, подсвечники " + "Электрика " + "Канцтовары, все для праздника " +
                    "Зажигалки, спички " + "Одноразовая посуда " + "Автотовары " + "Домашний текстиль " +
                    "Садовые товары " + "Посуда для хранения " + "Товары для заготовок " +
                    "Упаковка для выпечки и хранения продуктов " + "Фильтры для воды и аксессуары " +
                    "Посуда для приготовления " + "Пакеты и упаковка " + "Товары для ремонта " +
                    "Товары для ванной " + "Посуда для сервировки"
    }, delimiter = '|')
    @ParameterizedTest(name = "Catalog {0} should contain {0} item")
    void subCatalogItemsHoverTest(String catalogButton, String itemName) {
        $("[data-test-id=catalog-btn]").click();
        $$("[data-test-id=left-catalog] ul li").filter(visible)
                .find(text(catalogButton)).hover();
        $("[data-test-id=subcatalog]").shouldHave(text(itemName));

    }

}
