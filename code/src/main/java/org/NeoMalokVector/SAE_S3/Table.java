package org.NeoMalokVector.SAE_S3;

public class Table
{
    private static int qty=0;
    public int num;
    public Student student;
    public Table(){
        qty++;
        num=qty;
    }
    public String description()
    {
        String res=String.valueOf(num);
        if (student!=null){
            return (res+" "+student.id);
        }else{
            return res;
        }
    }
    public void destruction(){
        qty--;
    }
}
