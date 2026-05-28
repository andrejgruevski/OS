package Synchronization.ParkingLot;

//Паркинг со ограничен капацитет
//За дадениот код кој го решава проблемот на влез и излез од паркинг, да се додадат сите потребни елементи за синхронизација со што безбедно ќе се елиминираат сите услови за трка, без притоа да се доведе во опасност од блокада. For the given code that solves the problem of entering and exiting a parking lot, add all the necessary synchronization elements, which will eliminate all of the race conditions, without adding a possibility of a deadlock.
//Возилата не смеат да влезат ако паркингот е полн (чекаат додека не се ослободи место), а паркингот не смее да стане негативен. Да се додадат сите потребни елементи за синхронизација така што:
//
//нема да има race conditions,
//occupiedSpots никогаш нема да стане негативен,
//occupiedSpots никогаш нема да стане поголем од capacity,
//нема да се создаде deadlock,
//возилата ќе чекаат при полн паркинг.
class ParkingLots {
    int occupiedSpots = 0;
    int capacity = 10;

    public synchronized void enter() {

        while (occupiedSpots == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        occupiedSpots++;
        System.out.println("Enter: " + occupiedSpots);
        notifyAll();
//        if (occupiedSpots < capacity) {
//            occupiedSpots++;
//        }
    }

    public synchronized void exit() {
        while (occupiedSpots == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        occupiedSpots--;
        System.out.println("Exit: " + occupiedSpots);
        notifyAll();
    }

    public int getOccupied() {
        return occupiedSpots;
    }
}

class CarEntering extends Thread {
    ParkingLots parking;
    int repetitions;

    public CarEntering(ParkingLots parking, int repetitions) {
        this.parking = parking;
        this.repetitions = repetitions;
    }

    public void run() {
        for (int i = 0; i < repetitions; i++) {
            parking.enter();
        }
    }
}

class CarExiting extends Thread {
    ParkingLots parking;
    int repetitions;

    public CarExiting(ParkingLots parking, int repetitions) {
        this.parking = parking;
        this.repetitions = repetitions;
    }

    public void run() {
        for (int i = 0; i < repetitions; i++) {
            parking.exit();
        }
    }
}

public class ParkingLot {
    public static void main(String[] args) throws Exception {
        ParkingLots parking = new ParkingLots();

        CarEntering e1 = new CarEntering(parking, 50000);
        CarEntering e2 = new CarEntering(parking, 50000);
        CarEntering e3 = new CarEntering(parking, 50000);

        CarExiting x1 = new CarExiting(parking, 75000);
        CarExiting x2 = new CarExiting(parking, 75000);

        e1.start();
        e2.start();
        e3.start();
        x1.start();
        x2.start();

        e1.join();
        e2.join();
        e3.join();
        x1.join();
        x2.join();

        System.out.println("Final occupied = " + parking.getOccupied());
    }
}