package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Reader {
    private File file;
    private BufferedReader bufferedReader;

    public Reader(String filePath) {
        this.file = new File(filePath);
        System.out.println(file.getAbsolutePath());
    }
    public boolean fileExists(){return file.exists();};
    public void open() throws FileNotFoundException {
        if(!fileExists())throw new FileNotFoundException();
        try{
            bufferedReader = new BufferedReader(new FileReader(file));
        }catch(IOException | NullPointerException e){
            System.err.println(e.getMessage());
        }
    }
    public List<String> read(){
        List<String> output = new ArrayList<>();

        try{
            String line;
            do{
                line = bufferedReader.readLine();
                if(line!=null)output.add(line);
            }while(line != null);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }



        return output;
    }
    public void close(){
        try{
            bufferedReader.close();
        }catch(IOException | NullPointerException e){
            System.err.println(e.getMessage());
        }
    }
}
