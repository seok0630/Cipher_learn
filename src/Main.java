import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main (String args[]) throws IOException {
        cipher ci = new cipher("", 0); //암호화를 위한 Cipher 객체 생성
        String pass;
        int num, skip;
        Scanner scanner = new Scanner(System.in);

        File file = new File("./cipher.txt"); //파일 입출력 관련 변수.
        if (!file.exists()) { //파일 생성. 없을경우 만들지 않음.
            file.createNewFile();
        }

        File file_size = new File("./fsize.txt"); //파일 사이즈 관련 변수.
        if (!file_size.exists()) { //파일 생성. 없을경우 만들지 않음.
            file_size.createNewFile();

            FileWriter fsizeWriter = new FileWriter(file_size);
            fsizeWriter.write("0\n");
            fsizeWriter.close();
        }

        for (;;) {
            System.out.println("----------------------------------");
            System.out.println("1. 암호화 | 2. 복호화 | 3. 나가기");
            System.out.println("----------------------------------");
            System.out.print("선택> ");
            num = scanner.nextInt();

            if (num == 3) {
                System.out.println("프로그램이 종료되었습니다.");
                break;
            }

            switch (num) {
                case 1:
                    printM("암호 초기설정");

                    System.out.print("암호화할 내용: ");
                    pass = scanner.next();

                    System.out.print("복구키 선택 (0~26): ");
                    skip = scanner.nextInt();

                    ci = new cipher(pass, skip);
                    ci.encrypt(file, file_size);
                    break;

                case 2:
                    ci.decrypt(file, file_size);
                    break;

                default:
                    System.out.println("잘못누르셨습니다. 종료를 원하시면 '3'을 입력해주세요.");
                    break;
            }
            //.print();
        }
    }

    static void printM(String say) {
        System.out.println("----------------------------------");
        System.out.printf("          %s\n", say);
        System.out.println("----------------------------------");
    }
}

class cipher { //암호화를 위한 Cipher 객체 선언
    char[] cipher_arr;
    int size;
    int skip;

    cipher(String cipher, int skip) {
        this.cipher_arr = cipher.toCharArray();
        this.size = cipher.length();
        this.skip = skip;
    }

    void encrypt(File file, File file_size) throws IOException {
        FileWriter fileWriter;
        FileReader fsizeReader = new FileReader(file_size);
        BufferedReader fbufReader = new BufferedReader(fsizeReader);

        String say;
        int number;

        say = fbufReader.readLine();
        number = Integer.parseInt(say);

        if (number >= 20) {
            fileWriter = new FileWriter(file);
            number = 0;
        }
        else {
            fileWriter = new FileWriter(file, true);
            number = number + 1;
        }

        say = String.valueOf(number);

        FileWriter fsizeWriter = new FileWriter(file_size);

        for (int i = 0; i < size; i++)
            cipher_arr[i] = (char) (cipher_arr[i] + skip);
        String str = String.valueOf(cipher_arr);

        fileWriter.write(str + "\r\n");
        fileWriter.flush();
        fileWriter.close();

        fsizeWriter.write(say);
        fsizeWriter.close();
        fbufReader.close();

        System.out.println("암호화 완료, 복구키를 잊지마세요");
    }

    void decrypt(File file, File file_size) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int select,  recovery_key;

        FileReader fileReader = new FileReader(file);
        BufferedReader bufReader = new BufferedReader(fileReader);

        FileReader fsizeReader = new FileReader(file_size);
        BufferedReader fbufReader = new BufferedReader(fsizeReader);

        String[] line = new String[Integer.parseInt(fbufReader.readLine())];

        String say;
        int i = 0;

        while ((say = bufReader.readLine()) != null) {
            line[i] = say;

            i = i + 1;
        }

        for (int j = 0; j < i; j++) {
            System.out.printf(j + ". " + line[j] + "\n");
        }

        System.out.print(("해독할 문장의 번호를 선택해주세요: "));
        select = scanner.nextInt();

        char[] user_cipher = new char[line[select].length()];

        System.out.print("복구키 입력 (0~26): ");
        recovery_key = scanner.nextInt();

        for (int k = 0; k < line[select].length(); k++) {
            user_cipher[k] = (char) (line[select].charAt(k) - recovery_key);
        }

        System.out.println(user_cipher);

        bufReader.close();
        fbufReader.close();
    }
}
