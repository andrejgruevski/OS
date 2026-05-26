package Synchronization.VideoStreamingBuffer;

import java.util.LinkedList;
import java.util.Queue;
//Го имаме следниот случај:
//• Една нитка симнува податоци од интернет
//• Втора нитка е видео плеерот
//• Имаат споделен бафер- видеото (податоците)
//Правила:
//• Ако баферот е полн, симнувањето на податоци се паузира
//• Ако баферот е празен, плеерот паузира

class VideoBuffer {
    private Queue<Integer> buffer = new LinkedList<>();

    private final int CAPACITY = 5;

    public synchronized void downloadChunk(int chunk) throws InterruptedException {
        while (buffer.size() == CAPACITY) {
            System.out.println("Buffer is full. Downloader waiting...");
            wait();
        }
        buffer.add(chunk);

        System.out.println("Downloaded chunk: " + chunk);
        notify();
    }

    public synchronized int playChunk() throws InterruptedException {
        while (buffer.isEmpty()) {
            System.out.println("Buffer is empty");
            wait();
        }
        int chunk = buffer.remove();
        System.out.println("Played chunk: " + chunk);

        notify();
        return chunk;
    }
}

class Downloader extends Thread {
    private VideoBuffer buffer;
    public Downloader(VideoBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int chunk = 1;

        try {
            while (true) {
                Thread.sleep(500);
                buffer.downloadChunk(chunk++);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
class Player extends Thread {
    private VideoBuffer buffer;
    public Player(VideoBuffer buffer) {
        this.buffer = buffer;
    }
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                buffer.playChunk();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
public class VideoStreaming {
    static void main(String[] args) {
        VideoBuffer buffer = new VideoBuffer();
        new Downloader(buffer).start();
        new Player(buffer).start();
    }
}
