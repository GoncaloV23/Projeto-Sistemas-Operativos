package model;

import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnapBagTest {
    KnapBag bag;

    Item item1;
    Item item2;
    Item item3;
    Item item4;

    @BeforeEach
    void setUp(){
        List<Item> list = new ArrayList<>();

        //v = {15, 100, 90, 60, 40, 15, 10, 1}
        //w = { 2, 20, 20, 30, 40, 30, 60, 10}
        item1 = new Item(60, 30);
        item2 =new Item(100, 20);
        item3 =new Item(15, 2);
        item4 =new Item(90, 20);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);

        bag = new KnapBag(list);

        bag.sort();
    }

    @org.junit.jupiter.api.Test
    void sort() {
        List<Item> expectedList = new ArrayList<>();
        expectedList.add(item3);
        expectedList.add(item2);
        expectedList.add(item4);
        expectedList.add(item1);

        assertEquals(expectedList, bag.getItems());

    }

    @org.junit.jupiter.api.Test
    void putItemInBag() {
        bag.putItemInBag(0);
        bag.putItemInBag(1);

        assertEquals(115, bag.getBagValue());
        assertEquals(22, bag.getBagWeight());
    }

    @org.junit.jupiter.api.Test
    void takeItemFromBag() {
        bag.putItemInBag(0);
        bag.putItemInBag(1);
        bag.takeItemFromBag(0);
        bag.takeItemFromBag(1);

        assertEquals(0, bag.getBagValue());
        assertEquals(0, bag.getBagWeight());
    }

    @org.junit.jupiter.api.Test
    void putLastItemInBag() {
        bag.checkNextItem();

        bag.putLastCheckedItemInBag();

        assertEquals(15, bag.getBagValue());
        assertEquals(2, bag.getBagWeight());

        bag.checkNextItem();

        bag.putLastCheckedItemInBag();

        assertEquals(100 + 15, bag.getBagValue());
        assertEquals(20 + 2, bag.getBagWeight());
    }

    @org.junit.jupiter.api.Test
    void takeLastItemFromBag() {
        bag.checkNextItem();
        bag.putLastCheckedItemInBag();
        bag.checkNextItem();
        bag.putLastCheckedItemInBag();
        bag.takeLastCheckedItemFromBag();

        assertEquals(15, bag.getBagValue());
        assertEquals(2, bag.getBagWeight());
    }


    @org.junit.jupiter.api.Test
    void checkNextItem() {
        bag.checkNextItem();

        bag.putLastCheckedItemInBag();

        assertEquals(15, bag.getBagValue());
        assertEquals(2, bag.getBagWeight());
    }
}