import java.security.SecureRandom;
import java.util.concurrent.SynchronousQueue;

public class Main {
    private static final SecureRandom rand = new SecureRandom();
    private static final Integer items = 10;

    public static void main(String[] args) throws InterruptedException {
        Threads threads = new Threads();

        Thread ivanovThread = new Thread(() -> {
            try {
                threads.ivanov(items);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        Thread petrovThread = new Thread(() -> {
            try {
                threads.petrov(items);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        Thread nechiporchukThread = new Thread(() -> {
            try {
                threads.nechiporchuk(items);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        ivanovThread.start();
        petrovThread.start();
        nechiporchukThread.start();

        ivanovThread.join();
        petrovThread.join();
        nechiporchukThread.join();
    }

    static class Threads {
        private final SynchronousQueue<Integer> Ivanov_Petrov;
        private final SynchronousQueue<Integer> Petrov_Nechiporchuk;

        public Threads() {
            this.Ivanov_Petrov = new SynchronousQueue<>();
            this.Petrov_Nechiporchuk = new SynchronousQueue<>();
        }

        public void ivanov(int numItems) throws InterruptedException {
            int current = 0;
            while (true) {
                System.out.println("Іванов виносить товар " + current);
                this.Ivanov_Petrov.put(current++);
                if (current == numItems)
                    break;

                Thread.sleep(rand.nextInt(700) + 10);
            }
        }

        public void petrov(int numItems) throws InterruptedException {
            int current = 0;
            while (true) {
                int removed = this.Ivanov_Petrov.take();
                System.out.println("Петров вантажить товар " + current);
                this.Petrov_Nechiporchuk.put(removed);
                current++;
                if (current == numItems)
                    break;

                Thread.sleep(rand.nextInt(700) + 10);
            }
        }

        public void nechiporchuk(int numItems) throws InterruptedException {
            while (true) {
                int removed = this.Petrov_Nechiporchuk.take();
                numItems--;
                System.out.println("Нечипорчук підраховує вартість товару " + removed);
                if (numItems == 0)
                    break;

                Thread.sleep(rand.nextInt(500) + 10);
            }
        }
    }
}
