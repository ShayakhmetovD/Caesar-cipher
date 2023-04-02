import java.io.*;
import java.util.*;



/* Данная программа реализует шифр Цезаря на основе нескольких способов, а именно:
1. Шифрование/расшифрование файла с текстом;
2. Расшифрование файла способом "грубой силы" brute force;
3. Расшифрование файла способом, на основе сбора статистических данных;
 */

public class Main {
    static List<Character> alphabet;
    static char[] symbols = {'.',',','"',':','-','!','?',' '};
    public static void main(String[] args) {
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
            System.out.print("Введите номер варианта: ");
            int n = scanner.nextInt();
            switch (n) {
                case 1 -> {
                    System.out.println("Вы выбрали: \"Шифрование файла с помощью ключа\"");
                    System.out.print("Введите ключ: ");
                    int encryptKey = scanner.nextInt();
                    writingToFile(encryption(readingFromAFile(pathSourceFile), encryptKey), pathDestFile); //чтение, шифрование и запись в файл
                }
                case 2 -> {
                    System.out.println("Вы выбрали: \"Расшифрование файла с помощью известного ключа\"");
                    System.out.print("Введите ключ: ");
                    int decryptKey = scanner.nextInt();
                    writingToFile(decryption(readingFromAFile(pathSourceFile), decryptKey), pathDestFile); //чтение, расшифрование и запись в файл
                }
                case 3 -> {
                    System.out.println("Вы выбрали: \"Расшифрование файла методом brute force\"");
                    writingToFile(bruteForce(readingFromAFile(pathSourceFile)), pathDestFile); //чтение, расшифрование и запись в файл
                }
                case 4 -> {
                    System.out.println("Вы выбрали: \"Расшифрование файла на основе стастических данных\"");
                    System.out.print("Введите путь к статистическому файлу: ");
                    String pathUseFile = scanner.nextLine();
                }
            }
        }catch (NullPointerException e){
            System.out.println("Пустой файл, перезапусти программу!");
        }
    }

    //Создание алфавита
    private static List<Character> Alphabet(){
        alphabet = new ArrayList<>();
        for (char i = 'А'; i <= 'Я'; i++) {
            if(i=='Е'){
                alphabet.add(i+1,'Ё');
            }
            alphabet.add(i);
        }
        for (char i = 'а'; i <= 'я'; i++) {
            if(i=='е'){
                alphabet.add(i+1,'ё');
            }
            alphabet.add(i);
        }
        for (char i = 0; i < symbols.length; i++) {
            alphabet.add(i);
        }
        return alphabet;
    }

    //Данный метод производит чтение зашифрованного/расшифрованного текста из исходного файла
    private static String readingFromAFile(String pathSourceFile) {
        String sourceText = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathSourceFile))) {
            while (bufferedReader.ready()) {
                sourceText = bufferedReader.readLine();
            }
        } catch (NullPointerException e) {
            System.out.println("Пустой файл");
        } catch (Exception e) {
            System.out.println("Увы, я бессилен!");
        }
        return sourceText;
    }

    //Данный метод производит шифрование исходного текста с помощью ключа
    private static StringBuilder encryption(String sourceText, int encryptKey) {

        char[] array = sourceText.toCharArray();
        StringBuilder encryptedText = new StringBuilder();
        List<Character> list = new ArrayList<>(100);
        list = Alphabet();
        for (int i = 0; i < array.length; i++) {
            char symbol = sourceText.charAt(i);
            int oldIndex = list.indexOf(symbol);
            int newIndex = (oldIndex + encryptKey) % list.size();
            if (newIndex < 0) {
                newIndex = list.size() + newIndex;
            }
            char newSymbol = list.get(newIndex);
            encryptedText.append(newSymbol);
        }
        return encryptedText;
    }

    //Данный метод производит расшифрование зашифрованного текста с помощью известного ключа
    private static StringBuilder decryption(String sourceText, int key){
        int decryptKey = -key;
        char[] array = sourceText.toCharArray();
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            char symbol = sourceText.charAt(i);
            if((symbol >= 'а') && (symbol <= 'я')) // если символ в нижнем регистре
            {
                symbol = (char)(symbol + (decryptKey % 32)); // ключ % 32 бит
                if(symbol < 'а') {
                    symbol = (char) (symbol + 32); // если символ стоит левее, чем начало алфавита
                }
                if(symbol > 'я') {
                    symbol = (char) (symbol - (decryptKey % 32));// если символ стоит правее, чем конец алфавита
                }
            }else if((symbol >= 'А') && (symbol <= 'Я'))// если символ в верхнем регистре (далее аналогично)
            {
                symbol = (char)(symbol + (decryptKey % 32));
                if(symbol < 'А') {
                    symbol = (char) (symbol + (decryptKey % 32));
                }
                if(symbol > 'Я') {
                    symbol = (char) (symbol - (decryptKey % 32));
                }
            }
            decryptedText.append(symbol);// Добавляем расшифрованные символы
        }
        return decryptedText;
    }

    //Данный метод производит расшифрование текста методом brute force
    private static StringBuilder bruteForce(String sourceText){
        Map<Integer,Integer> map = new HashMap<>();
        int count = 0;
        for (int key = 0; key < Integer.MAX_VALUE; key++) {
            String destText = decryption(sourceText,key).toString(); // создаем строку, расшифровываем текст на основе перебора ключей
            char[] array = destText.toCharArray();
            for (int j = 0; j < array.length; j++) {
                char symbol = sourceText.charAt(j);
                if(symbol == ' '){
                    count++;
                }
            }
            map.put(count,key);
            count = 0;
        }
        List<Integer> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        Integer bruteKey = list.get(list.size() - 1);
        System.out.println("Ваш ключ: " + bruteKey);
        return decryption(sourceText,bruteKey);
    }

    //Данный метод производит запись зашифрованного/расшифрованного текста в конечный файл
    private static void writingToFile(StringBuilder destText, String pathDestFile) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathDestFile))) {
            bufferedWriter.write(String.valueOf(destText));
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        }