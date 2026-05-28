package Synchronization.PrinterSync;

import java.util.LinkedList;
import java.util.Queue;

// Го имаме следниот случај:
// • Повеќе нитки испраќаат документи за печатење
// • Една нитка е принтерот кој печати еден по еден документ
// • Имаат споделен ред на чекање
// Правила:
// • Ако редот е полн, испраќањето се паузира
// • Ако редот е празен, принтерот паузира

class PrinterQueue {
    private Queue<String> queue = new LinkedList<>();
    private final int CAPACITY = 5;

    public synchronized void sendDocument(String doc) throws InterruptedException {
        // TODO: додади синхронизација
        while (queue.size() == CAPACITY) {
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        queue.add(doc);
        System.out.println("Sent: " + doc);
        notify();
    }

    public synchronized String printDocument() throws InterruptedException {
        // TODO: додади синхронизација
        while (queue.isEmpty()) {
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        String doc = queue.remove();
        System.out.println("Printed: " + doc);
        notify();
        return doc;
    }
}

class DocumentSender extends Thread {
    private PrinterQueue queue;
    private String name;

    public DocumentSender(PrinterQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        int doc = 1;
        try {
            while (true) {
                Thread.sleep(300);
                queue.sendDocument(name + "_doc_" + doc++);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Printer extends Thread {
    private PrinterQueue queue;

    public Printer(PrinterQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(800);
                queue.printDocument();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class PrinterSync {
    public static void main(String[] args) {
        PrinterQueue queue = new PrinterQueue();
        new DocumentSender(queue, "User1").start();
        new DocumentSender(queue, "User2").start();
        new Printer(queue).start();
    }
}