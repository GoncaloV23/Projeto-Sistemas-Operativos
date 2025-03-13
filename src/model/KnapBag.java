package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KnapBag {

    private List<Item> bag;
    private int numbOfCheckedItems;
    private int totalValue;
    private int totalWeigth;

    public KnapBag(List<Item> bag) {
        this.bag = bag;
        numbOfCheckedItems = 0;
        totalValue = 0;
        totalWeigth = 0;
    }
    public KnapBag(List<Item> bag, int numbOfCheckeditems, int totalValue, int totalWeigth) {
        this.bag = bag;
        this.numbOfCheckedItems = numbOfCheckeditems;
        this.totalValue = totalValue;
        this.totalWeigth = totalWeigth;
    }
    public KnapBag() {
        this.bag = new ArrayList<>();
        numbOfCheckedItems = 0;
        totalValue = 0;
        totalWeigth = 0;
    }

    public void sort(){
        bag.sort((v1, v2)->{
            return ((int) (v2.getRatioValueWeigth()*100000 - v1.getRatioValueWeigth()*100000));
        });
    }
    public static KnapBag copy(KnapBag bestKnapBagValue) {
        return new KnapBag(new ArrayList<>(bestKnapBagValue.bag), bestKnapBagValue.numbOfCheckedItems,
                bestKnapBagValue.totalValue, bestKnapBagValue.totalWeigth);
    }
    public Item getItem(int index){return bag.get(index);}
    public List<Item> getItems(){return bag;}
    public void putItemInBag(int index){
        Item item = bag.get(index);
        item.putInBag();

        totalValue += item.getValue();
        totalWeigth += item.getWeight();
    }
    public void takeItemFromBag(int index){
        Item item = bag.get(index);
        item.takeFromBag();

        totalValue -= item.getValue();
        totalWeigth -= item.getWeight();
    }
    public void putLastCheckedItemInBag(){
        putItemInBag(numbOfCheckedItems-1);
    }
    public void takeLastCheckedItemFromBag(){
        takeItemFromBag(numbOfCheckedItems-1);
    }
    public int getNumberOfCheckedItems(){
        return numbOfCheckedItems;
    }
    public int calculateUBFirstNonInsertableItemPosition(int maxWeigth){
        int aux = numbOfCheckedItems;

        int sum = getBagWeight();
        while(sum <= maxWeigth){
            if(aux >= getNumberOfItems())return -1;
            sum += getItem(aux++).getWeight();
        }

        return aux;
    }
    public int getNumberOfItems(){
        return bag.size();
    }
    public int getBagValue(){
        return totalValue;
    }
    public int getBagWeight(){
        return totalWeigth;
    }
    public void checkNextItem(){
        bag.get(numbOfCheckedItems++).checkItem();
    }
    public KnapBag checkNextItemAndDuplicate(){
        checkNextItem();

        KnapBag bagCopy = KnapBag.copy(this);
        bagCopy.putLastCheckedItemInBag();

        return bagCopy;
    }
    public void resetItemsState(){
        numbOfCheckedItems = 0;
        totalValue = 0;
        totalWeigth = 0;
        for(Item i: bag){
            i.resetItem();
        }
    }
    @Override
    public String toString() {
        String result = "";
        String aux1 = "";
        String aux2 = "";
        for (int i =0;i<bag.size();i++){
            result += " | " + bag.get(i).getValue();
            aux1 += " | " + bag.get(i).getWeight();
            aux2 += " | " + bag.get(i).getItemState().getState();
        }
        result += " |\n";
        aux1 += " |\n";
        aux2 += " |\n";
        result += aux1 + aux2;
        result += "Valor " + totalValue +"\nPeso " + totalWeigth +"\n";
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnapBag knapBag = (KnapBag) o;
        return numbOfCheckedItems == knapBag.numbOfCheckedItems && totalValue == knapBag.totalValue && totalWeigth == knapBag.totalWeigth && bag.equals(knapBag.bag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bag, numbOfCheckedItems, totalValue, totalWeigth);
    }
}
