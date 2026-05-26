package ParallelSumComputation;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Во програмата се генерира низа од случајни броеви со должина 10000.
//Потребно е да се пресмета вкупниот збир на сите парни броеви во низата.
//Барања: Да се креираат 10 нишки (SumThread). Секоја нишка треба да обработува подниза со еднаква должина.
//Секоја нишка локално да го пресметува збирот на парните броеви во својата подниза.
//Глобалната променлива totalSum треба безбедно да се ажурира со користење на Lock.
//Главната нишка треба да почека сите нишки да завршат пред да го испечати резултатот.
//Не менувајте го кодот означен со DO NOT CHANGE

class EvenSum {

    static long totalSum = 0;

    static final NumberGenerator random = new NumberGenerator();

    private static final int ARRAY_LENGTH = 10000;
    private static final int NUM_THREADS = 10;

    // TODO: Define synchronization elements and initialize
    static final Lock lock = new ReentrantLock();

    // DO NOT CHANGE
    public static int[] getSubArray(int[] array, int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public static void main(String[] args) throws InterruptedException {

        int[] arr = ArrayGenerator.generate(ARRAY_LENGTH);

        // TODO: Make the SumThread class a thread and start 10 instances
        // Each instance should take a subarray from the original array with equal length

        int subArrayLength = ARRAY_LENGTH / NUM_THREADS;
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * subArrayLength;
            int end = start + subArrayLength;
            int[] subArray = getSubArray(arr, start, end);

            threads[i] = new Thread(new SumThread(subArray));
            threads[i].start();
        }

        // all threads done
        for (Thread thread : threads) {
            thread.join();
        }

        // DO NOT CHANGE

        System.out.println("The total sum of even numbers is: " + totalSum);

    }

    // TODO: Make the SumThread class a thread
    static class SumThread implements Runnable {

        private int[] arr;

        public SumThread(int[] arr) {
            this.arr = arr;
        }

        public void run() {

            long localSum = 0;

            for (int num : this.arr) {

                if (num % 2 == 0) {
                    localSum += num;
                }
            }

            // TODO: Add localSum to totalSum using lock
            lock.lock();
            try {
                totalSum += localSum;
            } finally {
                lock.unlock();
            }

        }
    }


    //************ DO NOT CHANGE ************//

    static class NumberGenerator {

        static final Random random = new Random();
        static final int RANDOM_BOUND = 100;

        public int nextInt() {
            return random.nextInt(RANDOM_BOUND);
        }
    }

    static class ArrayGenerator {

        static int[] generate(int length) {

            int[] array = new int[length];

            for (int i = 0; i < length; i++) {

                array[i] = EvenSum.random.nextInt();
            }

            return array;
        }
    }
}