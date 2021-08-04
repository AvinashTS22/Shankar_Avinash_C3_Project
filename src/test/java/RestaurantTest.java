import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class RestaurantTest {
    Restaurant restaurant;
    //REFACTOR ALL THE REPEATED LINES OF CODE
    static LocalTime openingTime;
    static LocalTime closingTime;

    @BeforeAll
    public static void varSetup(){
        openingTime = LocalTime.parse("10:30:00");
        closingTime = LocalTime.parse("22:00:00");
    }
    @BeforeEach
    public void setup(){
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        //WRITE UNIT TEST CASE HERE
        Restaurant mockRestaurant = Mockito.spy(restaurant);
        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("11:00:00"));
        assertTrue(mockRestaurant.isRestaurantOpen());

        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("10:30:00"));
        assertTrue(mockRestaurant.isRestaurantOpen());

        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("22:00:00"));
        assertTrue(mockRestaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        //WRITE UNIT TEST CASE HERE
        Restaurant mockRestaurant = Mockito.spy(restaurant);
        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("10:00:00"));
        assertFalse(mockRestaurant.isRestaurantOpen());

        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("10:29:59"));
        assertFalse(mockRestaurant.isRestaurantOpen());

        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("22:00:01"));
        assertFalse(mockRestaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Test
    public void total_value_of_bill_should_match_the_actual_amount_of_all_the_items_selected(){
        List<String> selectedItems = new ArrayList<String>();
        selectedItems.add("Sweet corn soup");
        selectedItems.add("Vegetable lasagne");
        Double totalAmountExpected = 119.0 + 269.0;
        Double actualAmount = restaurant.totalOrderValue(selectedItems);
        assertEquals(actualAmount,totalAmountExpected);
    }

    @Test
    public void total_value_of_bill_must_be_zero_if_no_items_selected(){
        List<String> selectedItems = new ArrayList<String>();
        Double actualAmount = restaurant.totalOrderValue(selectedItems);
        assertEquals(actualAmount,0.0);
    }

    @Test
    public void total_value_of_bill_must_match_the_value_of_items_selected_when_only_some_items_are_selected(){
        List<String> selectedItems = new ArrayList<String>();
        selectedItems.add("Vegetable lasagne");
        Double totalAmountExpected = 269.0;
        Double actualAmount = restaurant.totalOrderValue(selectedItems);
        assertEquals(actualAmount,totalAmountExpected);
    }
}