package placement;

import java.io.FileNotFoundException;

public class CreatingIntermediate {
    Data d;
    void downloadData(){
        try{
            d=new Data();
        }catch (FileNotFoundException e){
            System.out.println("Error: File not found");
        }

    }
}
