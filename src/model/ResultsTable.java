package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Resultados
 * De modo a se validar a qualidade do algoritmo, deverá ser construída uma tabela com as seguintes colunas:
 * a) Número do teste (de 1 a 10).
 * b) Nome do teste e número de itens.
 * c) Tempo total de execução.
 * d) Número de threads usada (parâmetro m na descrição dos algoritmos).
 * e) Melhor valor da soma dos itens encontrado.
 * f) Valor da soma dos pesos da melhor solução.
 * g) Número de iterações necessárias para chegar ao melhor valor encontrado.
 * h) Tempo que demorou até o programa atingir o melhor valor encontrado.
 * Cada teste deverá ser repetido 10 vezes para os mesmos parâmetros de entrada, e deverá ser
 * possível obter valores médios de tempo e número de iterações, assim como o número de vezes em
 * que se encontrou o valor óptimo.
 * Os ficheiros de teste a utilizar serão disponibilizados no moodle da disciplina.
 */
public class ResultsTable {
    public static final int NUMBER_OF_TESTS = 10;
    private List<Result> results;
    private String name;
    private int itensAmount;
    private int numberOfThreads;

    public ResultsTable(String name, int itensAmount, int numberOfThreads) {
        this.name = name;
        this.itensAmount = itensAmount;
        this.numberOfThreads = numberOfThreads;

        results = new ArrayList();
    }

    public int getNumberNextTest() {
        return results.size() + 1;
    }

    public boolean addResult(Result toAdd){
        if(results.size() == NUMBER_OF_TESTS)return false;

        results.add(toAdd);

        return true;
    }
    @Override
    public String toString() {
        String result = String.format("Teste %s   Itens %s\nNº de Threads %d\n", name, itensAmount, numberOfThreads);
        result += "____________________________________________________________________________________________________________\n";
        result += String.format("| %-10s | %-10s | %-10s | %-10s | %-23s | %-27s |\n",
                "Teste", "Valor", "Peso", "Iterações", "Tempo de resolução usec", "Tempo total de execução sec");
        result += "|__________________________________________________________________________________________________________|\n";

        double iterationsAvg = 0;
        double solutionTimeAvg = 0;
        double valueAvg = 0;
        for(int i = 0;i<results.size();i++){
            result += String.format("| %-10d | %s |\n", i+1, results.get(i).toString());
            iterationsAvg += results.get(i).getIterationsToCalcBestSolution();
            solutionTimeAvg += results.get(i).getTimeToCalcBestSolutionInMicroSec();
            valueAvg += results.get(i).bestValue();
        }

        result += "|__________________________________________________________________________________________________________|\n";
        result += String.format("Valor Médio: %f\nNº Médio de iterações: %f\nNº Médio de tempo para calcular solução: %f usec\n", valueAvg/results.size(), iterationsAvg/results.size(), solutionTimeAvg/results.size());
        return result;
    }
}
