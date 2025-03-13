package aj_kp;

import model.Item;
import model.KnapBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AJ_KP_BSTest {
    AJ_KP_BS aj;
    KnapBag bag;
    int maxWeigth;
    int size;
    @BeforeEach
    void setUp(){
        maxWeigth = 80;
        size = 5;


        List<Item> list = new ArrayList<>();
        list.add(new Item(33, 15));
        list.add(new Item(24, 20));
        list.add(new Item(36, 17));
        list.add(new Item(37, 8));
        list.add(new Item(12, 31));

        bag = new KnapBag(list);

        bag.sort();

        aj = new AJ_KP_BS(maxWeigth, bag);
    }

    @Test
    void beamSearch() {

    }

    @Test
    void lowerBound() {
        assertEquals(130, aj.lowerBound(bag));
        assertEquals(130, bag.getBagValue());
        assertEquals(60, bag.getBagWeight());
    }

    @Test
    void upperBound() {
        assertEquals(130, aj.upperBound(bag));

        bag.checkNextItem();
        bag.putLastCheckedItemInBag();
        assertEquals(130, aj.upperBound(bag));

        bag.checkNextItem();
        assertEquals(109, aj.upperBound(bag));
    }

    @Test
    void getChilds() {
        List<KnapBag> aux = new ArrayList<KnapBag>();
        aux.add(bag);
        List<KnapBag> res = aj.getChilds(aux);
    }

    @Test
    void selectSolutions() {
        List<KnapBag> aux = new ArrayList<KnapBag>();
        aux.add(bag);
        aj.selectSolutions(aux);
    }
}