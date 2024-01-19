
package javaapplication2;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MultiThread {
    public static void main(String[] args) {
        Thread th1 = new Thread( new DBRunable("Luong 1"));
        th1.start();
        Thread th2 = new Thread( new DBRunable("Luong 1"));
        th2.start();
    }
}

class  DBRunable implements Runnable{
    private String threadname;
    public DBRunable (String name){
        this.threadname = name;
    }
    
    @Override
    public void run(){
        System.out.println("Start" + threadname);
        for(int i = 0;i<=5;i++)
        {
            System.out.println(threadname + "; Buoc" + i);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("End" + threadname);
    }
}
