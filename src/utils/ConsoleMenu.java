package utils;

import aj_kp.AJ_KP_Controller;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConsoleMenu {
    private Scanner inputReader;
    private String version;
    private int percentage;
    public ConsoleMenu() {
        inputReader = new Scanner(System.in);
        version = "Base";
        percentage = 100;
        clearConsole();
        mainMenu();
    }
    public void mainMenu(){
        String option = "";
        while(!option.equalsIgnoreCase("S")){

            System.out.print("************Menu**************\n" +
                    "Versão atual - " + version + "\n" +
                    "1 - Mudar Para Versão Base\n" +
                    "2 - Mudar Para Versão Avançada\n" +
                    "S - Sair\n" +
                    "Comando - (ex: \"kp ex23.txt 10 60\")");
            option = getOption();
            //option = "kp ex05.txt 1 1";
            switch(option){
                case "1": clearConsole();
                    version = "Base";
                    percentage = 100;
                    break;
                case "2": clearConsole();
                    int auxPercentage = 100;
                    try{auxPercentage = Integer.parseInt(
                            getInput("Atualização do melhor resultado de x% em x%\nQual a Percentagem?"));
                    }catch (NumberFormatException e){
                        System.out.println("\nNúmero não reconhecido.");
                        break;
                    }
                    if(auxPercentage<1 || auxPercentage >100){
                        System.out.println("\nNúmero tem que ser maior que 0 e menor que 100.");

                    }else{
                        percentage = auxPercentage;
                        version = "Avançada " + "de " + percentage + "% em " + percentage + "%";
                    }
                    break;
                case "s": break;
                case "S": break;
                default: clearConsole();
                    if(!checkCommand(option))System.out.println("\nOpção não reconhecida.");
            }
        }
    }
    private boolean checkCommand(String command){
         if(command.length()<3 || !command.substring(0, 2).equalsIgnoreCase("kp"))return false;
        String[] parameters = command.split(" ");
        if (parameters.length != 4)return false;

        String file = parameters[1];
        int numbOfThreads;
        int execTime;
        AJ_KP_Controller controller;
        try{
            numbOfThreads = Integer.parseInt(parameters[2]);
            execTime = Integer.parseInt(parameters[3]);
            controller = new AJ_KP_Controller(file, numbOfThreads, execTime, percentage);
        }catch (NumberFormatException | FileNotFoundException e){return false;}

        System.out.println(controller.executeTests());

        return true;
    }


    /**
     * Método lê o input do utilizador.
     * @return String lida.
     */
    private String getOption(){
        System.out.print("Opção>");
        return inputReader.nextLine();
    }
    /**
     * Dá 30 quebras de linha simulando assim a limpesa da consola.
     */
    private void clearConsole(){
        for(int i=0;i<10;i++){System.out.println("\n\n\n");}
    }
    /**
     * Confirma uma escolha pelo utilizador.
     * @return boolean
     */
    private boolean getConfirmation(){
        System.out.println("Tesn a certesa desta opção? (Y se sim)");
        return getOption().equals("Y");
    }
    /**
     * Faz uma pergunta ao utilizador e lê a sua resposta.
     * @param question
     * @return
     */
    private String getInput(String question){
        while(true){
            System.out.println("\n"+question);
            String response = getOption();
            if(response!=null && !response.isEmpty())return response;
        }
    }
}
