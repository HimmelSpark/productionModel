import details.Detail;
import details.DetailA;
import details.DetailB;
import details.DetailC;
import production.Convey;
import production.Line;
import production.Position;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;




//TODO:: СИМУЛЯЦИЯ УСКОРЕНА В 1000 РАЗ
public class Main {

    /**
     * Конвейер производсвта: содержит логику очередей между позициями
     */
    private static Convey convey = new Convey();

    /**
     * Список позиций конвейера
     */
    private static List<Position> positions = new ArrayList<>();

    private static int COUNT_OF_POSITIONS = 8;

    private static Map<String, int[]> delays = new HashMap<>();

    public static void main(String[] args) {

        delays.put("A", new int[]{37, 39, 41, 33, 31, 35, 41, 37});
        delays.put("B", new int[]{46, 27, 38, 41, 24, 32, 51, 35});
        delays.put("C", new int[]{39, 23, 47, 35, 51, 34, 52, 49});

        /*
            Поднимаем линии производства
         */
        Line l1 = new Line();
        Line l2 = new Line();
        Line l3 = new Line();

        /*
            Устанавливаем типы деталей для каждой линии производства
         */
        l1.setDetail(new DetailA());
        l2.setDetail(new DetailB());
        l3.setDetail(new DetailC());

        /*
            Устанавливаем генераторы на линии производства
         */
        l1.setRunner(() ->  generateDetail(l1) );
        l2.setRunner(() ->  generateDetail(l2) );
        l3.setRunner(() ->  generateDetail(l3) );

        /*
            Создаем позиции конвейера (позиция – есть поток)
         */
        for (int i = 0; i < COUNT_OF_POSITIONS; i++) {
            positions.add(new Position(i)); //TODO:: скорее всего ошибка
        }

        /*
            Устанавливаем логику позиций
         */
        for (final Position position : positions) {
            position.setRunner(() -> serveDetail(position));
        }

        /*
            Запускаем линии производства, конвейер и обработчик результатов
         */
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(12);
        executor.submit(l1.getRunner());
        executor.submit(l2.getRunner());
        executor.submit(l3.getRunner());
        for (final Position position: positions) {
            executor.submit(position.getRunner());
        }
        executor.submit(Main::writeResults);
    }


    private static void generateDetail(Line line) {
        while (!line.isKillFlag()) {
            double time = line.getDetail().generateDelay();

            try {
                convey.addToConvey(line.getDetail().getClass().newInstance(), 0); // сразу добавляем в первую очередь
                Thread.sleep((long) time);
            } catch ( InterruptedException |
                    IllegalAccessException |
                    InstantiationException e) {
                e.printStackTrace();
            }
        }

        Thread.currentThread().interrupt();
    }

    private static void serveDetail(Position position) {
        int timeToSleep = 0;
        while (true) {
            ConcurrentLinkedQueue queue = convey.getListOfQueues().get(position.getPositionNumber());

            Detail detail = (Detail) queue.poll();
            if (detail != null) {

                //TODO:: добавить проверку типа и если что перестроить оборудование! но пока без этого
                try {
                    String type = null;

                    if (detail instanceof DetailA) {
                        type = "A";
                    } else if (detail instanceof DetailB) {
                        type = "B";
                    } else {
                        type = "C";
                    }
                    timeToSleep += delays.get(type)[position.getPositionNumber()];
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (position.getPositionNumber() < 7) {
                    //Передаем деталь в следующую очередь
                    convey.getListOfQueues().get(position.getPositionNumber() + 1).add(detail);
                } else { // Если выходная позиция
                    detail = null;
                }

                timeToSleep = 0;
            }
        }
    }

    private static void writeResults() {

        try (FileWriter fileWriter = new FileWriter("results.csv")) {
            while (true) {
                Thread.sleep(60);
                StringBuilder result = new StringBuilder(Convey.getLoss().toString() + " ");
                for (int i = 0; i < convey.getListOfQueues().size(); i++) {
                    result.append(Integer.toString(convey.getListOfQueues().get(i).size()) + " ");
                }
                System.out.println(result.toString());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
