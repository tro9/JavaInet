
package javaapplication2;
import java.net.InetAddress;

public class MyThread extends Thread{
    @Override
    public void run(){
        System.out.println("Luong dang chay");
        
    }
    
    public static void main(String[] args) {
        MyThread th = new MyThread();
        th.start();
    }
}
