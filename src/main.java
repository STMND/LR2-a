//Вариант 3

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class ParallelSearchTask extends RecursiveTask<Integer> {
    private final int[] array;
    private final int target;
    private final int start;
    private final int end;

    public ParallelSearchTask(int[] array, int target, int start, int end) {
        this.array = array;
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        //Если в массиве 5 или меньше значений, то ищем цель
        if (end - start <= 5) {
            for (int i = start; i < end; i++) {
                if (array[i] == target) {
                    return i;
                }
            }
            return -1;
        } else {
            // Иначе делим
            int mid = (start + end) / 2;
            ParallelSearchTask leftTask = new ParallelSearchTask(array, target, start, mid);
            ParallelSearchTask rightTask = new ParallelSearchTask(array, target, mid, end);

            leftTask.fork();
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();

            if (leftResult != -1) {
                return leftResult;
            } else {
                return rightResult;
            }
        }
    }
}

public class main {
    public static void main(String[] args) {
        int[] array;
        array = new int[100];
        for (int i=0;i<100;i++) {
            array[i]=i*2;
        }
        int target = 82; //Значение - цель

        ForkJoinPool pool = new ForkJoinPool();
        int result = pool.invoke(new ParallelSearchTask(array, target, 0, array.length));

        if (result != -1) {
            System.out.println("Element found: " + result);
        } else {
            System.out.println("Element not found");
        }
    }
}

