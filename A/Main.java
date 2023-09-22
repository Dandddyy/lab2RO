import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

class Bee implements Runnable {
    private int id;
    private Queue<Integer> tasks;

    public Bee(int id, Queue<Integer> tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    @Override
    public void run() {
        try {
            searchForWinnie();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void searchForWinnie() throws InterruptedException {
        Random random = new Random();
        while (true) {
            Integer task;
            synchronized (tasks) {
                task = tasks.poll();
            }

            if (task == null) {
                break;
            }

            if (Main.FOREST[task] == 1) {
                System.out.println("Bee " + id + " found Winnie the Pooh at position " + task);
                return;
            }
            Thread.sleep(random.nextInt(100));
        }
    }
}

public class Main {
    public static final int NUM_BEES = 5;
    public static final int FOREST_SIZE = 100;
    public static final int[] FOREST = new int[FOREST_SIZE];

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        int winniePosition = random.nextInt(FOREST_SIZE);
        FOREST[winniePosition] = 1;

        Queue<Integer> tasks = new LinkedList<>();
        for (int i = 0; i < FOREST_SIZE; i++) {
            tasks.add(i);
        }

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUM_BEES; i++) {
            Bee bee = new Bee(i, tasks);
            Thread thread = new Thread(bee);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Search is complete!");
    }
}
