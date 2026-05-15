package TestThreads;

import java.util.ArrayList;
import java.util.List;

class MyThread extends Thread {
    private String name;

    public MyThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Name of thread is " + name);
    }
}
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<MyThread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MyThread myThread = new MyThread("Thread " + i);
            threads.add(myThread);
        }

        for (MyThread myThread : threads) {
            myThread.start();
        }
        for(MyThread myThread : threads) {
            myThread.join();
        }
        System.out.println("END");
    }
}
