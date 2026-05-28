package ParallelMaxFinder;

//Паралелно пресметување на максимум од низа
//Дадена е низа a[n]. Треба да се пресмета:
//result = max(a[0], a[1], ..., a[n-1])
//Односно, треба да се најде максималниот елемент од низата.
//Барање:
//Да се паралелизира пресметувањето во Java. Почетниот код не користи нитки. Да се креираат m нитки, при што секоја нитка ќе го обработува својот дел од низата. Нитките треба да бидат поставени во класа со име:
//ParallelMaxFinder
//Секоја нитка треба да го пресмета локалниот максимум за својот дел, а главната програма на крај треба да го испечати глобалниот максимум.

import java.util.Scanner;

class MyThread extends Thread {
    private double[]arr;
    private int start;
    private int end;
    private double maxElement = 0;

    public MyThread(double[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
        maxElement = arr[start];
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            if (arr[i] > maxElement) {
                maxElement = arr[i];
            }
        }
    }

    public double getMaxElement() {
        return maxElement;
    }
}

public class ParallelMaxFinder {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int n = 1_000_000;
        int m=sc.nextInt();

        double[] a = new double[n];


        for (int i = 0; i < n; i++) {
            a[i] = Math.random() * 1_000_000;
        }

        double result = a[0];

        MyThread[] threads = new MyThread[m];
        int chunkSize = n / m;
        for (int i = 0; i < m; i++) {
            int start = i * chunkSize;
            int end = (i == m - 1 ) ? n : start + chunkSize;
            threads[i] = new MyThread(a, start, end);
            threads[i].start();
        }
        for (int i = 1; i < m; i++) {
            threads[i].join();
            if (threads[i].getMaxElement() > result) {
                result = threads[i].getMaxElement();
            }
        }

        System.out.println("Max = " + result);
    }
}