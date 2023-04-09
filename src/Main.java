import java.io.*;
import java.util.*;



/* Данная программа реализует шифр Цезаря на основе нескольких способов, а именно:
1. Шифрование/расшифрование файла с текстом с помощью ключа;
2. Расшифрование файла способом "грубой силы" brute force;
3. Расшифрование файла способом, на основе сбора статистических данных;

Условия работы программы:
1. При расшифровании текста способом brute force, используемый ключ лежит в диапазоне [0-Integer.MAX_VALUE);
2. При расшифровании текста на основе сбора статистических данных исходный и статистический текст должны быть одной длины.
 */

public class Main {
    static List<Character> alphabet;
    public static void main(String[] args) {
        Alphabet();
        try(Scanner scanner = new Scanner(System.in)){
            System.out.print("Добро пожаловать!Номера вариантов работы программы:\n" +
                    " 1: Шифрование файла с помощью ключа\n" +
                    " 2: Расшифрование файла с помощью известного ключа\n" +
                    " 3: Расшифрование файла методом brute force\n" +
                    " 4: Расшифрование файла на основе стастических данных");
            System.out.println();
            System.out.print("Введите путь к исходному файлу:");
            String pathSourceFile = scanner.nextLine();
            System.out.print("Введите путь к конечному файлу: ");
            String pathDestFile = scanner.nextLine();
            System.out.print("Введите путь к статистическому файлу: ");
            String pathAdditionalFile = scanner.nextLine();
            System.out.print("Введите номер варианта: ");
            int numberIsVariable = scanner.nextInt();
            switch (numberIsVariable) {
                case 1 -> {
                    System.out.println("Вы выбрали: \"Шифрование файла с помощью ключа\"");
                    System.out.print("Введите ключ: ");
                    int encryptKey = scanner.nextInt();
                    writingToFile(
                            encryption(
                                    readingFromAFile(
                                            pathSourceFile), encryptKey),
                            pathDestFile); //чтение, шифрование и запись в файл
                }
                case 2 -> {
                    System.out.println("Вы выбрали: \"Расшифрование файла с помощью известного ключа\"");
                    System.out.print("Введите ключ: ");
                    int decryptKey = scanner.nextInt();
                    writingToFile(
                            decryption(
                                    readingFromAFile(pathSourceFile), -decryptKey),
                            pathDestFile); //чтение, расшифрование и запись в файл
                }
                case 3 -> {
                    System.out.println("Вы выбрали: \"Расшифрование файла методом brute force\"");
                    writingToFile(
                            bruteForce(
                                    readingFromAFile(pathSourceFile)),
                            pathDestFile); //чтение, расшифрование и запись в файл
                }
                case 4 -> {
                    System.out.println("Вы выбрали: \"Расшифрование файла на основе стастических данных\"");
                    writingToFile(
                            decryptionOnParsing(
                                    readingFromAFile(pathAdditionalFile),
                                    readingFromAFile(pathSourceFile)),
                            pathDestFile); //чтение, расшифрование и запись в файл
                }
            }
        }catch (NullPointerException e){
            System.out.println("Пустой файл, перезапусти программу!");
        }
    }

    //Создание алфавита
    private static void Alphabet(){
        alphabet = new ArrayList<>();
        for (char i = 'А'; i <= 'Я'; i++) {
            alphabet.add(i);
            if(i=='Е'){
                alphabet.add('Ё');
            }
        }
        for (char i = 'а'; i <= 'я'; i++) {
            alphabet.add(i);
            if(i=='е'){
                alphabet.add('ё');
            }
        }
        alphabet.add('.');
        alphabet.add(',');
        alphabet.add('"');
        alphabet.add(':');
        alphabet.add('-');
        alphabet.add('!');
        alphabet.add('?');
        alphabet.add(' ');
    }

    //Данный метод производит чтение зашифрованного/расшифрованного текста из исходного файла
    private static String readingFromAFile(String pathSourceFile) {
        String sourceText = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathSourceFile))) {
            System.out.println("Начинается чтение файла...");
            while (bufferedReader.ready()) {
                sourceText = bufferedReader.readLine();
            }
        } catch (NullPointerException e) {
            System.out.println("Пустой файл");
        } catch (Exception e) {
            System.out.println("Увы, я бессилен!");
        }
        System.out.println("Файл прочитан!");
        return sourceText;
    }

    //Данный метод производит шифрование исходного текста с помощью ключа
    private static StringBuilder encryption(String sourceText, int encryptKey) {
        StringBuilder encryptedText = new StringBuilder();
        System.out.println("Начинается шифрование файла...");
        for (int i = 0; i < sourceText.length(); i++) {
            char symbol = sourceText.charAt(i);
            int oldIndex = alphabet.indexOf(symbol);
            int newIndex = (oldIndex + encryptKey) % alphabet.size();
            if (newIndex < 0) {
                newIndex = alphabet.size() + newIndex;
            }
            char newSymbol = alphabet.get(newIndex);
            encryptedText.append(newSymbol);
        }
        System.out.println("Файл зашифрован!");
        return encryptedText;
    }

    //Данный метод производит расшифрование зашифрованного текста с помощью известного ключа
    private static StringBuilder decryption(String sourceText, int decryptKey){
        StringBuilder decryptedText = new StringBuilder();
        System.out.println("Начинается расшифрование файла...");
        for (int i = 0; i < sourceText.length(); i++) {
            char symbol = sourceText.charAt(i);
            int oldIndex = alphabet.indexOf(symbol);
            int newIndex = (oldIndex + decryptKey) % alphabet.size();
            if (newIndex < 0) {
                newIndex = alphabet.size() + newIndex;
            }
            char newSymbol = alphabet.get(newIndex);
            decryptedText.append(newSymbol);
        }
        System.out.println("Файл расшифрован!");
        return decryptedText;
    }

    //Данный метод производит расшифрование текста методом brute force
    private static StringBuilder bruteForce(String sourceText){
        Map<Integer,Integer> map = new TreeMap<>();
        int count = 0;
        int maxCount = 0;
        System.out.println("Начинается перебор ключей...");
        for (int key = 0; key < Integer.MAX_VALUE; key++) {
            StringBuilder destText = decryption(sourceText,(-key)); // создаем строку, расшифровываем текст на основе перебора ключей
            for (int j = 0; j < destText.length(); j++) {
                char symbol = destText.charAt(j);
                if(symbol == ' '){ // сравниваем символы на наличие пробелов
                    count++;
                }
            }
            if(count>maxCount){
                maxCount = count;
                map.put(maxCount,key);
            }
            count = 0;
        }
        System.out.println("Перебор ключей завершен!");
        List<Integer> list = new ArrayList<>(map.keySet());
        Integer key = list.get(list.size()-1);
        Integer bruteKey = map.get(key);
        System.out.println("Ваш ключ: " + bruteKey);
        return decryption(sourceText,(-bruteKey));
    }

    //Данный метод производит расшифрование текста на основе статистических данных
    private static StringBuilder decryptionOnParsing(String sourceText, String AdditionalText) {
        StringBuilder decryptedText = new StringBuilder();
        Map<Double, Character> frequencyCharInAdditionalText = new HashMap<>();
        int countInAdditionalText = 0;
        int countInSourceText = 0;
        for (int i = 0; i < AdditionalText.length(); i++) { // вычисление частоты повторений символа во вспомогательном тексте
            char symbolI = sourceText.charAt(i);
            for (int j = 0; j < AdditionalText.length(); j++) {
                char symbolJ = sourceText.charAt(j);
                if (symbolI == symbolJ) {
                    if (frequencyCharInAdditionalText.containsValue(symbolI)) {
                        i++;
                        break;
                    } else {
                        countInAdditionalText++;
                    }
                }
            }
            Double percent = Math.ceil(((double) countInAdditionalText / AdditionalText.length()) * 100); // вычисление процента повторений
            frequencyCharInAdditionalText.put(percent, symbolI);
            countInAdditionalText = 0;
        }

        for (int i = 0; i < sourceText.length(); i++) { // вычисление частоты повторений символа в зашифрованном тексте
            char symbolI = sourceText.charAt(i);
            for (int j = 0; j < sourceText.length(); j++) {
                char symbolJ = sourceText.charAt(j);
                if (symbolI == symbolJ) {
                    countInSourceText++;
                }
            }
            Double percent2 = Math.ceil(((double) countInSourceText / sourceText.length()) * 100);
            if(frequencyCharInAdditionalText.containsKey(percent2)){ // сравнение процентов поворений символов в обоих текстах
                decryptedText.append(frequencyCharInAdditionalText.get(percent2)); //расшифрование текста на основе сравнения процентов повторений символов
            }
            countInSourceText = 0;
        }
        return decryptedText;
    }

    //Данный метод производит запись зашифрованного/расшифрованного текста в конечный файл
    private static void writingToFile(StringBuilder destText, String pathDestFile) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathDestFile))) {
            System.out.println("Начинается запись в файл...");
            bufferedWriter.write(String.valueOf(destText));
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Файл записан!");
    }
        }