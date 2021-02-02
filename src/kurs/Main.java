package kurs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //по умолчанию создается объект без ошибки
        Err err = new Err();
        //три раза запрашиваем с разными параметрами, каждый раз - пока введут без ошибок
        for (int i = 0; i <= 2; i++) {
            while (true) {
                int a = 0;
                if (i == 0) a = enterInt(err, scanner, null, null, null);
                if (i == 1) a = enterInt(err, scanner, null, 0, 9);
                if (i == 2) a = enterInt(err, scanner, "Тут моя подсказка, а что проверяется, никто не знает: ",
                        0, 9);
                if (err.hasError()) {
                    //печатаем всю доступную информацию об ошибке
                    err.printMsg();
                    err.printDetailMsg();
                    err.printStackTrace();
                } else {
                    System.out.println("ВВЕЛИ: " + a);
                    break;
                }
            }
        }
        //ЕРР при нормальном выходе всегда очищена, можно ее использовать и дальше
        //наример, для чтения несуществующего файла
        String text = readTextFile(err, "file.txt");
        if (err.hasError()) {
            //печатаем всю доступную информацию об ошибке
            err.printMsg();
            err.printDetailMsg();
            err.printStackTrace();
        } else {
            System.out.println("СЧИТАЛИ: \n" + text);
        }

        scanner.close();
    }
    
    private static int enterInt(Err err, Scanner scanner, String prompt, Integer leftBound, Integer rightBound) {
        //чтобы не возиться с обработкой ошибки, могут и НУЛЛ передать
        //поэтому работаем с ЕРР только для НЕ НУЛЛ
        if (err != null)
            err.clear();
        //если не передали подсказку, то выводим свою
        //можно даже границы задать в ней
        String bounds = String.format(" (%d..%d)",
                leftBound == null ? Integer.MIN_VALUE : leftBound,
                rightBound == null ? Integer.MAX_VALUE : rightBound);
        System.out.print(prompt == null || prompt.isEmpty() ? "Введите целое" + bounds + " число: " : prompt);
        //перехватывем все возможные ошибки
        try {
            int res = scanner.nextInt();
            //своя проверка на левую границу
            if (leftBound != null && err != null && res < leftBound) {
                err.set(String.format("Введенное число %d меньше допустимого %d!", res, leftBound));
                return 0;
            }
            //своя проверка на правую границу
            if (rightBound != null && err != null && res > rightBound) {
                err.set(String.format("Введенное число %d больше допустимого %d!", res, rightBound));
                return 0;
            }
            //все ок, можн вернуть число
            return res;
        } catch (Exception e) {
            //это нужно, чтобы очистить неверный ввод в буфере сканера
            //иначе будет бесконечный цикл
            scanner.nextLine();
            //стандартная ошибка, можно МСЖ и не передавать
            if (err != null)
                err.set("Ошибка ввода целого числа!", e);
            return 0;
        }
    }

    private static String readTextFile(Err err, String fileName) {
        //всегда чистим ошибку, мало ли что
        //используем новые статические методы класса, которые проверяют сами на НУЛЛ
        Err.clear(err);
        //тут накапливаем результат, если не будет ошибки, иначе вернем пустую строку
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fr = new FileReader(fileName);
            Scanner sc = new Scanner(fr);
            while (sc.hasNextLine()) {
                stringBuilder.append(sc.nextLine());
                stringBuilder.append("\n");
            }
            fr.close();
        } catch (IOException e) {
            //тоже исполбзуем безопасный метод класса
            //и мы не знаем, какая тут ошибка возникнет в общем случае
            //поэтому сообщение можно взять детальное
            Err.set(err, null, e);
        }
        return stringBuilder.toString();
    }
}
