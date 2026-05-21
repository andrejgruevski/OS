package OsMidterm;

import java.util.Random;
import java.util.concurrent.Semaphore;

class OSMidterm {

    static Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {

        // STARTING CODE, DON'T MAKE CHANGES
        //-----------------------------------------------------------------------------------------
        String final_text = "Bravo!!! Ja resi zadacata 🙂";
        int m = 10, n = 100;
        Object[][] data = new Object[m][n];
        Random rand = new Random();
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int random = rand.nextInt(100);
                if (random % 2 == 0 & k < final_text.length()) {
                    data[i][j] = final_text.charAt(k);
                    k++;
                } else {
                    data[i][j] = rand.nextInt(100);
                }
            }
        }

        DataMatrix matrix = new DataMatrix(m, n, data);
        StatisticsResource statisticsResource = new StatisticsResource();
        //-----------------------------------------------------------------------------------------

        // ONLY TESTING CODE - МОЖЕ ДА СЕ КОМЕНТИРА
        //-----------------------------------------------------------------------------------------
        Concatenation concatenation = new Concatenation(matrix, statisticsResource, 0);
        concatenation.concatenate();
        statisticsResource.printString();
        //-----------------------------------------------------------------------------------------

        // Ресетирање на стрингот пред да стартуваме нишки
        statisticsResource = new StatisticsResource();

        // Креирање на нишките - по една за секоја редица
        Concatenation[] threads = new Concatenation[m];
        for (int i = 0; i < m; i++) {
            threads[i] = new Concatenation(matrix, statisticsResource, i);
        }

        // Стартување на сите нишки
        for (int i = 0; i < m; i++) {
            threads[i].start();
        }

        // Чекање максимум 10 секунди за секоја нишка
        for (int i = 0; i < m; i++) {
            try {
                threads[i].join(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Принтање на финалниот стринг
        statisticsResource.printString();

        // Проверка за deadlock
        for (int i = 0; i < m; i++) {
            if (threads[i].isAlive()) {
                threads[i].interrupt();
                System.out.println("Possible deadlock");
            }
        }
    }

    // Concatenation е Thread класа
    static class Concatenation extends Thread {

        private DataMatrix matrix;
        private StatisticsResource statisticsResource;
        private int rowIndex;

        public Concatenation(DataMatrix matrix, StatisticsResource statisticsResource, int rowIndex) {
            this.matrix = matrix;
            this.statisticsResource = statisticsResource;
            this.rowIndex = rowIndex;
        }

        // Оригинална функција - за тестирање, НЕ СЕ КОРИСТИ во нишките
        public void concatenate() {
            for (int i = 0; i < this.matrix.getM(); i++) {
                for (int j = 0; j < this.matrix.getN(); j++) {
                    if (this.matrix.isString(i, j)) {
                        this.statisticsResource.concatenateString(this.matrix.getEl(i, j).toString());
                    }
                }
            }
        }

        // Изминува само една редица и конкатенира карактерите
        public void concatenate_by_row(int row) {
            for (int j = 0; j < this.matrix.getN(); j++) {
                if (this.matrix.isString(row, j)) {
                    try {
                        mutex.acquire();
                        this.statisticsResource.concatenateString(this.matrix.getEl(row, j).toString());
                        mutex.release();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }

        public void execute() {
            concatenate_by_row(this.rowIndex);
        }

        @Override
        public void run() {
            execute();
        }
    }

    //-------------------------------------------------------------------------
    // YOU ARE NOT CHANGING THE CODE BELOW
    static class DataMatrix {

        private int m, n;
        private Object[][] data;

        public DataMatrix(int m, int n, Object[][] data) {
            this.m = m;
            this.n = n;
            this.data = data;
        }

        public int getM() {
            return m;
        }

        public int getN() {
            return n;
        }

        public Object[][] getData() {
            return data;
        }

        public Object getEl(int i, int j) {
            return data[i][j];
        }

        public Object[] getRow(int pos) {
            return this.data[pos];
        }

        public Object[] getColumn(int pos) {
            Object[] result = new Object[m];
            for (int i = 0; i < m; i++) {
                result[i] = data[i][pos];
            }
            return result;
        }

        public boolean isString(int i, int j) {
            return this.data[i][j] instanceof Character;
        }
    }

    static class StatisticsResource {

        private String concatenatedString;

        public StatisticsResource() {
            this.concatenatedString = "";
        }

        public void concatenateString(String new_character) {
            concatenatedString += new_character;
        }

        public void printString() {
            System.out.println("Here is the phrase from the matrix: " + concatenatedString);
        }
    }
}