package Lab4.ParallelVectorDifference;

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

        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = i + 5;
        }

        int m=4;

        MyThreads[]threads = new MyThreads[m];

        int chunkSize = n/m;

        for (int i = 0; i < m; i++) {
            int start = i*chunkSize;

            int end;

            if (i == m-1) {
                end = n;
            }else{
                end = start + chunkSize;
            }
            threads[i] = new MyThreads(a, b, start, end);
            threads[i].start();
        }

        double result = 0;

        for (int i = 0; i < m; i++) {
            threads[i].join();
            result += threads[i].getSum();
        }

        System.out.println("Sum of differences = " + result);
    }
}
