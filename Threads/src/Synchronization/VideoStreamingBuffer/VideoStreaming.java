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

class VideoBuffer { // ova e shared resusrs koj go koristat dvete nishki
    private Queue<Integer> buffer = new LinkedList<>(); // tuka chuvame video chunks

    private final int CAPACITY = 5; // vo eden bufer mozhi max 5 chunks


    // ova e PRODUCER METHOD, so synchronized samo edna nishka smej da vlezi vnatre
    // za da nema race condition
    public synchronized void downloadChunk(int chunk) throws InterruptedException {
        while (buffer.size() == CAPACITY) { // ako bufferot e poln, znachi downloader ne smee da dodava, CHEKAJ!
            System.out.println("Buffer is full. Downloader waiting...");
            wait(); // 1. nishkata odi na pauza. 2. go osloboduva lock-ot za player da vlezi. 3. cheka notify() - nekoj da go razbudi
        }
        buffer.add(chunk); // ako bufferot ne e poln, se izvrshuva ova se dodava chunk

        System.out.println("Downloaded chunk: " + chunk);
        notify(); // notify go budi Player
    }

    // CONSUMER METHOD, player ovde troshi chunks
    public synchronized int playChunk() throws InterruptedException {
        while (buffer.isEmpty()) { // ako bufferot e prazen player ne mozhi da pushta video, mora da cheka producer da dodaj video
            System.out.println("Buffer is empty");
            wait(); // ako e prazen bufferot cheka.
        }
        int chunk = buffer.remove(); // ako player pushtil video, znachi se brishi toa e izgledano
        System.out.println("Played chunk: " + chunk);

        notify();// mu kazhuva na downloader ima mesto vo bufferot, simni novo video
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
            while (true) { // beskonechno simnuvame video chunks
                Thread.sleep(500); // simulira download time, downloader e pobrz od player
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
   public static void main(String[] args) {
        VideoBuffer buffer = new VideoBuffer();
        new Downloader(buffer).start();
        new Player(buffer).start();
    }
}
