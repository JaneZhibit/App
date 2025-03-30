import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Sorting extends JPanel implements Runnable {
    private final int N;
    private int[] arr;
    private volatile boolean isSorting = false;
    private int currentIndex = -1;
    private int compareIndex = -1;

    public Sorting(int N) {
        this.N = N;
        arr = new int[N];
        gen();
    }

    void gen() {
        Random rng = new Random();
        for (int i = 0; i < N; i++) {
            arr[i] = rng.nextInt(250) + 50;
        }
        repaint();
    }

    public void startSorting() {
        if (!isSorting) {
            isSorting = true;
            new Thread(this).start();
        }
    }

    @Override
    public void paint(Graphics g) {
        setBackground(Color.LIGHT_GRAY);

        super.paint(g);
        int barWidth = getWidth() / N;

        for (int i = 0; i < N; i++) {
            if (i == currentIndex) {
                g.setColor(Color.RED);
            } else if (i == compareIndex) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.GREEN);
            }

            // Отрисовка столбца
            int barHeight = arr[i];
            g.fillRect(i * barWidth, getHeight() - barHeight, barWidth - 1, barHeight);
        }
    }

    @Override
    public void run() {
        quickSort(arr, 0, arr.length - 1);
        isSorting = false;
        repaint();
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int p = partition(arr, low, high);
            updateIndices(p, -1);

            quickSort(arr, low, p - 1);
            quickSort(arr, p + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int middle = low + (high - low) / 2;
        int pivot = arr[middle];

        // Визуализация выбора опорного элемента
        updateIndices(middle, high);
        swap(arr, middle, high);

        int i = low - 1;
        for (int j = low; j < high; j++) {
            updateIndices(j, i);
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        repaint();
    }

    private void updateIndices(int curr, int comp) {
        currentIndex = curr;
        compareIndex = comp;
        repaint();
    }
}

