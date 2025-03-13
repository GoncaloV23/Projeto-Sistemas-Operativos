package model;

import java.util.Objects;

public class Item {
    private int value;
    private int weight;
    private ItemState itemState;

    public Item(int value, int weight) {
        this.value = value;
        this.weight = weight;
        this.itemState = ItemState.NOT_CHECKED;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public ItemState getItemState() {
        return itemState;
    }

    public void checkItem() {
        itemState = ItemState.NOT_IN_BAG;
    }
    public void resetItem(){
        itemState = ItemState.NOT_CHECKED;
    }

    public void putInBag(){itemState = ItemState.IN_BAG;}
    public void takeFromBag(){checkItem();}

    public double getRatioValueWeigth(){
        return ((double) value) / ((double) weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return value == item.value && weight == item.weight && itemState == itemState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, weight);
    }

    public enum ItemState{
        NOT_CHECKED, NOT_IN_BAG, IN_BAG;

        public int getState(){
            switch (this){
                case NOT_CHECKED: return -1;
                case NOT_IN_BAG: return 0;
                case IN_BAG: return 1;
                default: return -1;
            }
        }
    }
}
