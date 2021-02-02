package kurs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*
класс Err доработан, чтобы можно было не создавать каждый раз его его объект
у него внутри есть приватное статическое финальное поле last типа Err, которое один раз
создается
статические методы clear и set без параметра-объекта err позволяют работать именно с last
wasError вернет ошибку именно из него, но можно использовать и getLast().hasError()
дальше я решил не дублировать методы, можно получить last через getLast() и
все его методы получения или печати информации об ошибке тогда доступны
!!! не забываем чистить last в каждом методе, где собираемся использовать
работа с любым обычным объектом Err, созданным через new, по логике класса
всегда заполняет и last теми же значениями ошибки
например, в чтении файла я использую отдельный объект, а проверю на выходе все равно last

новый статический метод wasError(int needPrint), который не только проверяет поспеднюю ошибку, но и
печатает сразу, конечно, если переда нужный параметр (константа), стало еще проще обрабатывать
*/

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            //запрос целого числа, где можно ввести нечаянно буквы или числа не из заданного диапазона
            int a = enterInt(scanner, null, 0, 9);
            if (!Err.wasError(Err.PRINT_FULL)) {
                System.out.println("ВВЕЛИ: " + a);
                break;
            }
        }

        //чтение несуществующего файла
        String text = readTextFile("file.txt");
        if (!Err.wasError(Err.PRINT_FULL)) {
            System.out.println("СЧИТАЛИ: \n" + text);
        }

        scanner.close();
    }
    
    private static int enterInt(Scanner scanner, String prompt, Integer leftBound, Integer rightBound) {
        //в этом примере мы используем только работу с last ошибкой
        Err.clear();
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
            if (leftBound != null && res < leftBound) {
                Err.set(String.format("Введенное число %d меньше допустимого %d!", res, leftBound));
                return 0;
            }
            //своя проверка на правую границу
            if (rightBound != null && res > rightBound) {
                Err.set(String.format("Введенное число %d больше допустимого %d!", res, rightBound));
                return 0;
            }
            //все ок, можн вернуть число
            return res;
        } catch (Exception e) {
            //это нужно, чтобы очистить неверный ввод в буфере сканера
            //иначе будет бесконечный цикл
            scanner.nextLine();
            //стандартная ошибка, можно МСЖ и не передавать
            Err.set("Ошибка ввода целого числа!", e);
            return 0;
        }
    }

    private static String readTextFile(String fileName) {
        //можно работать внутри со своим объектом ошибки
        //last параллельно все равно заполнится аналогичной информацией
        Err err = new Err();
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
