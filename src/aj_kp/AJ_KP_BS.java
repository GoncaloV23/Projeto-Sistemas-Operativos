package aj_kp;

import model.KnapBag;
import model.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class AJ_KP_BS extends Thread{
    private final int MAX_WEIGHT;
    private final int NUMBER_OF_ITEMS;
    private Result bestLocalResult;
    private long start;
    private static Result bestResult = new Result(new KnapBag());
    private static boolean timeOut = false;
    private static boolean timesUp = false;
    public static Semaphore sem = new Semaphore(1);
    public static Semaphore sem2 = new Semaphore(0);
    public AJ_KP_BS(int maxWeight, KnapBag bag) {
        this.MAX_WEIGHT = maxWeight;
        this.NUMBER_OF_ITEMS = bag.getNumberOfItems();
        this.bestLocalResult = new Result(bag);
    }
    @Override
    public void run(){
        aj_kp_bs();

        AJ_KP_BS.setBestResult(bestLocalResult);
    }
    public static void resetSem2(int numberOfThreads){

        AJ_KP_BS.sem2 = new Semaphore(1 - numberOfThreads);
    }
    public static void setTimeOut(boolean timeOut){
        AJ_KP_BS.timeOut = timeOut;
    }
    public static void setTimesUp(boolean timesUp){
        AJ_KP_BS.timesUp = timesUp;
    }
    public static Result getBestResult(){
        return bestResult;
    }
    public static void reset(int numberOfThreads){
        AJ_KP_BS.timeOut = false;
        AJ_KP_BS.timesUp = false;
        AJ_KP_BS.bestResult = new Result(new KnapBag());
        AJ_KP_BS.sem = new Semaphore(1);
        AJ_KP_BS.sem2 = new Semaphore(1 - numberOfThreads);
    }
    public static synchronized void setBestResult(Result result){
        if(AJ_KP_BS.bestResult.getBestKnapBagValue().getBagValue() < result.getBestKnapBagValue().getBagValue()){
            AJ_KP_BS.bestResult = result;
        }
    }

    /**
     * 1. Ordena-se os itens de acordo com o critério definido no anexo.
     * 2. Calcula-se a solução de Lower Bound e define-se essa solução como a melhor solução até o
     * momento.
     * 3. Aplica-se o algoritmo Beam Search (BS), obtendo-se a melhor solução possível. Este
     * algoritmo deve receber como entrada a solução de Lower Bound inicial.
     * 4. Se a solução obtida no ponto 2 for melhor que a melhor solução actual, deve-se actualizar a
     * solução actual.
     * 5. O algoritmo volta ao ponto 2 enquanto não houver uma condição de término dada pelo
     * tempo ou pelo número de iterações máxima.
     */
    public void aj_kp_bs(){
        start = System.nanoTime();

        lowerBound(bestLocalResult.getBestKnapBagValue());

        long end = System.nanoTime();
        bestLocalResult.setTimeToCalcBestSolutionInMicroSec((end-start)/1000);

        KnapBag lb = KnapBag.copy(bestLocalResult.getBestKnapBagValue());

        while (!timesUp){

            if(!timeOut){

                Result res = beamSearch(lb);

                if(res.getBestKnapBagValue().getBagValue() > lb.getBagValue()){
                    lb = KnapBag.copy(res.getBestKnapBagValue());
                    if(res.getBestKnapBagValue().getBagValue() > bestLocalResult.getBestKnapBagValue().getBagValue())
                        setLocalBestResult(res);
                }
            }else{

                AJ_KP_BS.setBestResult(bestLocalResult);
                AJ_KP_BS.sem2.release();


                try {
                    sem.acquire();
                    sem.release();
                } catch (InterruptedException e) {

                }
            }
        }

        end = System.nanoTime();
        bestLocalResult.setTotalTimeInSec((end - start)/1000000000);

    }
    public Result getBestLocalResult(){
        return bestLocalResult;
    }
    public synchronized void setLocalBestResult(Result bestResult){
        this.bestLocalResult = bestResult;
    }
    public  Result beamSearch(KnapBag lb){
        List<KnapBag> solutions = new ArrayList<>();
        KnapBag root = KnapBag.copy(lb);
        root.resetItemsState();
        solutions.add(root);

        Result result = new Result(KnapBag.copy(lb));

        int iterations = 0;
        while(!solutions.isEmpty() && !timesUp && !timeOut){

            iterations++;
            List<KnapBag> solutionsChilds = getChilds(solutions);


            List<KnapBag> toKeep = new ArrayList<>();;
            for(KnapBag solution : solutionsChilds){
                int upperBound = upperBound(solution);

                if(upperBound >= lb.getBagValue()){
                    if(solution.getBagValue() > lb.getBagValue()){
                        lb = solution;

                        result.setBestKnapBagValue(solution);
                        result.setIterationsToCalcBestSolution(iterations);
                        long end = System.nanoTime();
                        result.setTimeToCalcBestSolutionInMicroSec((end - start)/1000);

                    }
                    toKeep.add(solution);
                }
            }

            solutions = selectSolutions(toKeep);
        }

        return result;

    }
    public int lowerBound(KnapBag bag){
        bag.sort();
        int index = 0;
        while(index++ < bag.getNumberOfItems() &&
                bag.getBagWeight() + bag.getItem(bag.getNumberOfCheckedItems()).getWeight() < MAX_WEIGHT){
            bag.checkNextItem();
            bag.putLastCheckedItemInBag();
        }

        return bag.getBagValue();
    }
    public int upperBound(KnapBag incompleteSolution) {
        int numberOfInsertedItems = incompleteSolution.getNumberOfCheckedItems();

        int firstNonInsertableItemPosition = incompleteSolution.calculateUBFirstNonInsertableItemPosition(MAX_WEIGHT);

        int w = solutionOuterWeight(incompleteSolution, firstNonInsertableItemPosition);


        int sum = incompleteSolution.getBagValue();
        for (int i = numberOfInsertedItems; i < firstNonInsertableItemPosition - 1; i++) {
            sum += incompleteSolution.getItem(i).getValue();
        }
        if (firstNonInsertableItemPosition == -1){
            for (int i = numberOfInsertedItems; i < incompleteSolution.getNumberOfItems(); i++) {
                sum += incompleteSolution.getItem(i).getValue();
            }
            return sum;
        }

        int aux1;
        int aux2;
        if (firstNonInsertableItemPosition == incompleteSolution.getNumberOfItems()){
            aux1 = 0;
        }else {
            aux1 = ((int) (w * incompleteSolution.getItem(firstNonInsertableItemPosition).getRatioValueWeigth()));


        }
        aux2 =
            ((int)(
                    incompleteSolution.getItem(firstNonInsertableItemPosition-1).getValue() - (
                            incompleteSolution.getItem(firstNonInsertableItemPosition-1).getWeight() - w
                    ) * incompleteSolution.getItem(firstNonInsertableItemPosition - 2).getRatioValueWeigth()
            ));


        int ub1 = sum + aux1;
        int ub2 = sum + aux2;

        if(ub1 > ub2)return ub1;
        else return ub2;
    }
    private int solutionOuterWeight(KnapBag bag, int firstNonInsertableItemPosition){
        int sum = 0;
        for (int i = bag.getNumberOfCheckedItems(); i<firstNonInsertableItemPosition - 1; i++){
            sum += bag.getItem(i).getWeight();
        }
        return MAX_WEIGHT - sum - bag.getBagWeight();
    }
    public List<KnapBag> getChilds(List<KnapBag> list){
        KnapBag aux = list.get(0);

        List<KnapBag> childs = new ArrayList<>();

        if(aux.getNumberOfItems()<=aux.getNumberOfCheckedItems())return childs;



        for(KnapBag bag : list){
            KnapBag newBag = bag.checkNextItemAndDuplicate();

            if(bag.getBagWeight()<=MAX_WEIGHT)
                childs.add(bag);
            if(newBag.getBagWeight()<=MAX_WEIGHT)
                childs.add(newBag);
        }

        return childs;
    }
    public List<KnapBag> selectSolutions(List<KnapBag> solutions){
        int numberOfSoulutions = NUMBER_OF_ITEMS/2;
        if (solutions.size()<=numberOfSoulutions) {
            return solutions;
        }

        List<KnapBag> bestSolutions = new ArrayList<>();


        int solutionsIndexes = -1;
        for(int i=0;i<numberOfSoulutions;i++){
            solutionsIndexes = ((int)(Math.random() *100)) % solutions.size();
            KnapBag bag = solutions.get(solutionsIndexes);
            if(!bestSolutions.contains(bag)){
                bestSolutions.add(bag);
            }
        }

        return bestSolutions;
    }
}
