package aj_kp;

import model.Item;
import model.KnapBag;
import model.Result;
import model.ResultsTable;
import utils.Reader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AJ_KP_Controller{
    ResultsTable results;
    String fileName;
    int numbOfItems, maxWeigth, numbOfThreads, timeOfExecInSec, advencedPercentage;
    List<String> file;
    public AJ_KP_Controller(String fileName, int numbOfThreads, int timeOfExecInSec, int advencedPercentage) throws FileNotFoundException{
        this.file = readTestFile(fileName);
        this.fileName = fileName;
        this.numbOfItems = Integer.parseInt(file.get(0));
        this.maxWeigth = Integer.parseInt(file.get(1));
        this.numbOfThreads = numbOfThreads;
        this.timeOfExecInSec = timeOfExecInSec;
        this.advencedPercentage = advencedPercentage;

        results = new ResultsTable(fileName, this.numbOfItems, numbOfThreads);
        //version = new AJ_KP_Base();
    }

    private void executeTest(KnapBag bag){
        AJ_KP_BS[] threads = new AJ_KP_BS[numbOfThreads];
        AJ_KP_BS.reset(numbOfThreads);

        for(int i=0;i<threads.length;i++){
            threads[i] = new AJ_KP_BS(maxWeigth, KnapBag.copy(bag));
            threads[i].start();
        }
        try {
            mainThreadOperationsControll(threads);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        AJ_KP_BS.setTimesUp(true);


        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(AJ_KP_BS.getBestResult());
        results.addResult(AJ_KP_BS.getBestResult());
    }
    private void mainThreadOperationsControll(AJ_KP_BS[] threads) throws InterruptedException {
        int millSec = timeOfExecInSec * 1000;
        int intervall = millSec * advencedPercentage / 100;

        int countOfIntervals = millSec / intervall;

        for(int i=1;i<=countOfIntervals;i++){
            Thread.sleep(intervall);
            if(i<countOfIntervals)checkThreads(threads);
        }
    }
    private void checkThreads(AJ_KP_BS[] threads){
        try {
            AJ_KP_BS.sem.acquire();
        } catch (InterruptedException e) {}


        AJ_KP_BS.setTimeOut(true);
        try {
            AJ_KP_BS.sem2.acquire();
        }catch (InterruptedException e) {}

        AJ_KP_BS.setTimeOut(false);


        Result bestResult = AJ_KP_BS.getBestResult();

        for (int i=0;i<threads.length;i++){
            Result aux = threads[i].getBestLocalResult();
            if(bestResult.bestValue() > aux.bestValue())
                threads[i].setLocalBestResult(bestResult);
        }

        AJ_KP_BS.sem.release();


        AJ_KP_BS.resetSem2(threads.length);
    }
    public ResultsTable executeTests(){
        KnapBag bag = createBag();

        System.out.println("Ficheiro: " + fileName + "\nPeso max: " + maxWeigth + " Nº de Itens: " + numbOfItems +
                "\nNº de Threads: " + numbOfThreads + " Tempo de execução: " + timeOfExecInSec);


        int count = 0;
        while (count++ < 10){
            executeTest(bag);
            System.out.println("Teste " + count + " Completo");
        }

        System.out.println("\n\n\n");
        return results;
    }
    private KnapBag createBag(){
        List<Item> items = new ArrayList<>();
        for(int i=2;i<numbOfItems+2;i++){
            String[] aux = file.get(i).split(" ");

            int value = Integer.parseInt(aux[0]);
            int weight = Integer.parseInt(aux[1]);
            items.add(new Item(value, weight));
        }

        return new KnapBag(items);
    }
    public List<String> readTestFile(String fileName) throws FileNotFoundException{
        String aux = "knap_tests_extended\\" + fileName;
        Reader reader = new Reader(aux);

        List<String> fileContent = new ArrayList<>();


        reader.open();
        fileContent = reader.read();
        reader.close();

        return fileContent;
    }
}
