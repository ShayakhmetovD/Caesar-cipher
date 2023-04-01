import java.io.*;
import java.util.Scanner;



/* Данная программа реализует шифр Цезаря на основе нескольких способов, а именно:
1. Шифрование/расшифрование файла с текстом;
2. Расшифрование файла способом "грубой силы" brute force;
3. Расшифрование файла способом, на основе сбора статистических данных;
 */

public class Main {
    char[] sym = {'.',',','"',':','-','!','?',' '};
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine();
            System.out.print("Введите ключ: ");
            int key = scanner.nextInt();
            writingToFile(encryption(readingFromAFile(path),key)); //чтение, шифрование и запись в файл
        }catch (NullPointerException e){
            System.out.println("Пустой файл, перезапусти программу!");
        }

    }

    //Данный метод производит чтение из файла и выдает исходный текст
    public static String readingFromAFile(String path) {
        String sourceText = null;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
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

    //Данный метод производит шифрование исходного текста
    public  static StringBuilder encryption(String sourceText, int key){

        char[] array = sourceText.toCharArray();
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            char symbol = sourceText.charAt(i);
            if((symbol >= 'а') && (symbol <= 'я')) // если символ в нижнем регистре
            {
                symbol = (char)(symbol + (key % 32)); // ключ % 32 бит
                if(symbol < 'а') {
                    symbol = (char) (symbol + 32); // если символ стоит левее, чем начало алфавита
                }
                if(symbol > 'я') {
                    symbol = (char) (symbol - (key % 32));// если символ стоит правее, чем конец алфавита
                }
            }else if((symbol >= 'А') && (symbol <= 'Я'))// если символ в верхнем регистре (далее аналогично)
            {
                symbol = (char)(symbol + (key % 32));
                if(symbol < 'А') {
                    symbol = (char) (symbol + (key % 32));
                }
                if(symbol > 'Я') {
                    symbol = (char) (symbol - (key % 32));
                }
            }
            encryptedText.append(symbol);// Добавляем зашифрованные символы
        }
        return encryptedText;
        }

    //Данный метод производит расшифрование зашифрованного текста
    public  static StringBuilder decryption(String message, int key2){
        int key = -key2;
        char[] array = message.toCharArray();
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            char symbol = message.charAt(i);
            if((symbol >= 'а') && (symbol <= 'я')) // если символ в нижнем регистре
            {
                symbol = (char)(symbol + (key % 32)); // ключ % 32 бит
                if(symbol < 'а') {
                    symbol = (char) (symbol + 32); // если символ стоит левее, чем начало алфавита
                }
                if(symbol > 'я') {
                    symbol = (char) (symbol - (key % 32));// если символ стоит правее, чем конец алфавита
                }
            }else if((symbol >= 'А') && (symbol <= 'Я'))// если символ в верхнем регистре (далее аналогично)
            {
                symbol = (char)(symbol + (key % 32));
                if(symbol < 'А') {
                    symbol = (char) (symbol + (key % 32));
                }
                if(symbol > 'Я') {
                    symbol = (char) (symbol - (key % 32));
                }
            }
            encryptedText.append(symbol);// Добавляем расшифрованные символы
        }
        return encryptedText;
    }

    //Данный метод создает файл и производит запись зашифрованного текста
    public static void writingToFile(StringBuilder encryptedText) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter("C:\\Users\\Danil\\IdeaProjects\\" +
                        "Project - Caesar-cipher\\src\\DestFile\\destFile"))) {
            bufferedWriter.write(String.valueOf(encryptedText));
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        }