package production;

import details.Detail;
import details.DetailA;
import details.DetailB;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Данный класс представляет конвейер сборки деталей
 */
public class Convey {

    private final static int COUNT_OF_QUEUES = 8; // 7

    private static AtomicInteger loss = new AtomicInteger(0);

    private List<ConcurrentLinkedQueue<? super  Detail>> listOfQueues = new ArrayList<>();

    public Convey() {
        for (int i = 0; i < COUNT_OF_QUEUES; i++ ) {
            listOfQueues.add(new ConcurrentLinkedQueue<>());
        }
    }

    public int getDetailsCount() {
        int result = 0;
        for (ConcurrentLinkedQueue<? super Detail> listOfQueue : listOfQueues) {
            result += listOfQueue.size();
        }
        return result;
    }

    public static AtomicInteger getLoss() {
        return loss;
    }

    public void addToConvey(Detail detail, int queueNumber) {
        if (getDetailsCount() < 40) {
            listOfQueues.get(queueNumber).add(detail);
        } else {
            //TODO:: удалить объект и записать в убытки
            if (detail instanceof DetailA) {
                loss.addAndGet(15);
            } else if (detail instanceof DetailB) {
                loss.addAndGet(10);
            } else {
                loss.addAndGet(12);
            }

            detail = null;
        }
    }

    public List<ConcurrentLinkedQueue<? super Detail>> getListOfQueues() {
        return listOfQueues;
    }
}
