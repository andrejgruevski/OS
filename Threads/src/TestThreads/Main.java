package TestThreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class MyThread extends Thread {
    static int counter=0;
    static Semaphore semaphore = new Semaphore(1);

    @Override
    public void run() {
        // pred da vlezime vo kritichniot region barame dozvola
        // otkako ke izlezime od kritichniot region vrakjame dozvola

        try {
            semaphore.acquire(); // barame dozvola
            //kritichen region
            for (int i=0;i<20000;i++) {
                counter++;
            }
            semaphore.release(); // vrakjame dozvola
        }catch (InterruptedException e){
            e.printStackTrace();
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
