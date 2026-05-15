package TestThreads;

import java.util.ArrayList;
import java.util.List;

class MyThread extends Thread {
    static int counter=0;
    @Override
    public void run() {
        for (int i=0;i<20000;i++) {
            counter++;
        }
    }
}
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<MyThread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MyThread myThread = new MyThread();
            threads.add(myThread);
        }

        for (MyThread myThread : threads) {
            myThread.start();
        }
        for(MyThread myThread : threads) {
            myThread.join();
        }
        System.out.println(MyThread.counter);
    }
}
