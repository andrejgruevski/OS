package Synchronization.ProducerConsumer;


//За дадениот код кој го решава проблемот на произведувач и конзумер, да се додадат сите потребни елементи за синхронизација
//со што безбедно ќе се елиминираат сите услови за трка, без притоа да се доведе во опасност од блокада.


class Warehouse {
    int stock = 0;

    // producer
    public synchronized void addProduct() {
        stock++;

        notifyAll(); // gi budime site shto chekaat
    }

    // consumer
    public synchronized void removeProduct() {

        while (stock == 0){

            //ako nema proizvodi chekaj
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stock--;
    }

    public int getStock() {
        return stock;
    }
}

class ProducerThread extends Thread {
    Warehouse warehouse;
    int repetitions;

    public ProducerThread(Warehouse warehouse, int repetitions) {
        this.warehouse = warehouse;
        this.repetitions = repetitions;
    }

    public void run() {
        for (int i = 0; i < repetitions; i++) {
            warehouse.addProduct();
        }
    }
}

class ConsumerThread extends Thread {
    Warehouse warehouse;
    int repetitions;

    public ConsumerThread(Warehouse warehouse, int repetitions) {
        this.warehouse = warehouse;
        this.repetitions = repetitions;
    }

    public void run() {
        for (int i = 0; i < repetitions; i++) {
            warehouse.removeProduct();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) throws Exception {
        Warehouse warehouse = new Warehouse();

        ProducerThread p1 = new ProducerThread(warehouse, 100000);
        ProducerThread p2 = new ProducerThread(warehouse, 100000);

        ConsumerThread c1 = new ConsumerThread(warehouse, 100000);
        ConsumerThread c2 = new ConsumerThread(warehouse, 100000);

        p1.start();
        p2.start();
        c1.start();
        c2.start();

        p1.join();
        p2.join();
        c1.join();
        c2.join();

        System.out.println("Final stock = " + warehouse.getStock());
    }
}