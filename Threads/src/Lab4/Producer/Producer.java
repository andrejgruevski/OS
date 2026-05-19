package Lab4.Producer;

class Warehouse {
    int stock = 0;
    int capacity = 100;


    // vo prvichniot kod vo klasite AddProduct i removeProduct stock < capacity = stock ++ i stock > 0 = stock--, so ova imame race condtition (povekje nishki menuvaat istovremeno zaednichka promenliva)
    // todo: sinhronizacija, wait, notifyAll,
    // producer da cheka ako magacinot e poln
    // consumer da cheka ako e prazen
    // da nema race condtion
    // da nema deadlock



    // idea ako stock == capacity ne smej da dodava mora da ima WAIT()
    // ako stock == 0 ne smej da dodava mora da ima WAIT()
    //producer
    public synchronized void addProduct() {
        if (stock == capacity) {
//            System.out.println("producer cheka - full e");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        stock++;
//        System.out.println("dodavame " + stock);
        notifyAll();
    }

    // consumer
    public synchronized void removeProduct() {
        if (stock == 0) {
//            System.out.println("consumer cheka - prazno e");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        stock--;
//        System.out.println("izbrishavme" + stock);
        notifyAll();

    }

    public synchronized int getStock() {
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

public class Producer {
    public static void main(String[] args) throws Exception {
        Warehouse warehouse = new Warehouse();

        ProducerThread p1 = new ProducerThread(warehouse, 100000);
        ProducerThread p2 = new ProducerThread(warehouse, 100000);
        ProducerThread p3 = new ProducerThread(warehouse, 100000);

        ConsumerThread c1 = new ConsumerThread(warehouse, 100000);
        ConsumerThread c2 = new ConsumerThread(warehouse, 100000);

        p1.start();
        p2.start();
        p3.start();

        c1.start();
        c2.start();

        p1.join();
        p2.join();
        p3.join();

        c1.join();
        c2.join();

        System.out.println("Final stock = " + warehouse.getStock());
    }
}