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
public class SumClass {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9};
        int numThreads = 2;
        Thread[] ths = new Thread[numThreads];
        SumCal[] cals = new SumCal[numThreads];
        for (int i = 0; i < numThreads; i++) {
            cals[i] = new SumCal(arr, i, numThreads);
            ths[i] = new Thread(cals[i]);
            ths[i].start();
        }
        
        for (int i = 0; i < numThreads; i++) {
            try {
                ths[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            int totalSum = 0;
            for (SumCal cal : cals) {
                totalSum += cal.getSum();
            }
            System.out.println("Sum: "+totalSum);
        }
    }
}

class SumCal implements Runnable{
    private int[] arr;
    private int startIndex;
    private int div;
    private int sum;

    public SumCal(int[] arr, int startIndex, int div) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.div = div;
        this.sum = 0;
    }
    
    @Override
    public void run(){
        for (int i = startIndex; i < arr.length; i += div) {
            sum += arr[i];
        }
    }
    
    public int getSum(){
        return sum;
    }
}