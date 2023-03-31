import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine();
            System.out.print("Введите ключ: ");
            int key = scanner.nextInt();
            encryption(readingFromAFile(path),key);
        }catch (NullPointerException e){
            System.out.println("Пустой файл, перезапусти программу!");
        }

    }


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

    public  static void encryption(String message, int key){

        char[] array = message.toCharArray();
        String result = "";
        for (int i = 0; i < array.length; i++) {
            char c = (char)(message.charAt(i) + key);
            if (c > 'я')
                result += (char)(message.charAt(i) - (32-key));
            else
                result += (char)(message.charAt(i) + key);
        }
        System.out.println(result);
        }
    }