package Lab4.ParallelVectorDifference;

import java.util.Scanner;

class MyThreads extends Thread {
    private double[] a;
    private double[] b;

    private int start;
    private int end;

    private double localSum = 0;

    public MyThreads(double[] a, double[] b, int start, int end) {
        this.a = a;
        this.b = b;
        this.start = start;
        this.end = end;

    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            localSum += Math.abs(a[i] - b[i]);
        }
    }

    public double getSum() {
        return localSum;
    }
}

public class ParallelVectorDifference {
    public static void main(String[] args) throws InterruptedException {
        int n = 1_000_000;

        double[] a = new double[n];
        double[] b = new double[n];
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();

        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = i + 5;
        }

//        int m=4;

        MyThreads[]threads = new MyThreads[m];

        int chunkSize = n/m; // golemina na sekoe parche pr: niza od 12 elementi, 12/4 = 3, sekoja nishka obrabotuva 3 elementi

        for (int i = 0; i < m; i++) {
            int start = i*chunkSize; // sekoja nishka pochnuva kade prethodnata zavrshila pr: 1 * 3 = 3, 2 * 3 = 6...

            int end;

            if (i == m-1) {
                end = n; // za nishkata da odi do posledniot element
            }else{
                end = start + chunkSize; // primer ako sme na i=0, znachi 0 + 3 = 3, 3 + 3 = 6....
            }
            threads[i] = new MyThreads(a, b, start, end);
            threads[i].start(); // go povikuvame run() za sekoja nishka
        }

        double result = 0;

        for (int i = 0; i < m; i++) {
            threads[i].join(); // gi chkeame site nishki da zavrshat pa da gi soberime
            result += threads[i].getSum();
        }

        System.out.println("Sum of differences = " + result);
    }
}
