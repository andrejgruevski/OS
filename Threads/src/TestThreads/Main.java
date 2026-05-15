package TestThreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class SharedResource {
    int counter = 0;
}

class MyThread extends Thread {
    SharedResource sr;
    static Semaphore semaphore = new Semaphore(1);

    public MyThread(SharedResource sr) {
        this.sr = sr;
    }

    @Override
    public void run() {
        // pred da vlezime vo kritichniot region barame dozvola
        // otkako ke izlezime od kritichniot region vrakjame dozvola

        try {
            semaphore.acquire(); // barame dozvola
            //kritichen region
            for (int i = 0; i < 20000; i++) {
                sr.counter++;
            }
            semaphore.release(); // vrakjame dozvola
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<MyThread> threads = new ArrayList<>();

        SharedResource sr = new SharedResource();
        for (int i = 0; i < 10; i++) {
            MyThread myThread = new MyThread(sr);
            threads.add(myThread);
        }

        for (MyThread myThread : threads) {
            myThread.start();
        }
        for (MyThread myThread : threads) {
            myThread.join();
        }
        System.out.println(sr.counter);
    }
}
