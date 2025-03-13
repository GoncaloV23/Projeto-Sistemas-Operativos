package model;


public class Result {
    private double totalTimeInSec;
    private KnapBag bestKnapBagValue;
    private int iterationsToCalcBestSolution;
    private double timeToCalcBestSolutionInMicroSec;

    public Result(KnapBag bestKnapBagValue) {
        this.bestKnapBagValue = bestKnapBagValue;
        this.totalTimeInSec = 0;
        this.iterationsToCalcBestSolution = 0;
        this.timeToCalcBestSolutionInMicroSec = 0;
    }

    public void updateResult(KnapBag bestKnapBagValue, int iterationsToCalcBestSolution,
                             double  timeToCalcBestSolutionInMicroSec, double totalTimeInSec){
        setBestKnapBagValue(bestKnapBagValue);
        setIterationsToCalcBestSolution(iterationsToCalcBestSolution);
        setTimeToCalcBestSolutionInMicroSec(timeToCalcBestSolutionInMicroSec);
        setTotalTimeInSec(totalTimeInSec);

    }
    public double getTotalTimeInSec() {
        return totalTimeInSec;
    }

    public void setTotalTimeInSec(double totalTimeInSec) {
        this.totalTimeInSec = totalTimeInSec;
    }

    public KnapBag getBestKnapBagValue() {
        return bestKnapBagValue;
    }

    public void setBestKnapBagValue(KnapBag bestKnapBagValue) {
        this.bestKnapBagValue = KnapBag.copy(bestKnapBagValue);
    }

    public int getIterationsToCalcBestSolution() {
        return iterationsToCalcBestSolution;
    }

    public void setIterationsToCalcBestSolution(int iterationsToCalcBestSolution) {
        this.iterationsToCalcBestSolution = iterationsToCalcBestSolution;
    }

    public double getTimeToCalcBestSolutionInMicroSec() {
        return timeToCalcBestSolutionInMicroSec;
    }

    public void setTimeToCalcBestSolutionInMicroSec(double timeToCalcBestSolutionInMicroSec) {
        this.timeToCalcBestSolutionInMicroSec = timeToCalcBestSolutionInMicroSec;
    }

    public int bestValue(){return getBestKnapBagValue().getBagValue();}
    public int bestWeigth(){return getBestKnapBagValue().getBagWeight();}

    @Override
    public String toString() {
        return String.format("%-10d | %-10d | %-10d | %-23f | %-27f",
                bestKnapBagValue.getBagValue(), bestKnapBagValue.getBagWeight(),
                iterationsToCalcBestSolution, timeToCalcBestSolutionInMicroSec, totalTimeInSec);
    }
}
