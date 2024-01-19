/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author Admin
 */
public class LoopThread {
    public static void main(String[] args) {
        Thread th1 = new Thread(new DNRunable2());
        th1.start();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        th1.interrupt();
    }
}

class DNRunable2 implements Runnable{
    @Override
    public void run(){
        System.out.println("Running - State:" 
                + Thread.currentThread().getState());
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("Stopped - State:"
                    + Thread.currentThread().getState());
        }
        System.out.println("Thread End:" 
                + Thread.currentThread().getState());
    } 
}
