import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main (String args[]) {
        cipher ci = new cipher("", 0); //암호화를 위한 Cipher 객체 생성
        String pass;
        int skip;
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. 암호화      2.복호화");
        System.out.print("선택: ");
        switch (scanner.nextInt()) {
            case 1:
                System.out.print("암호화할 내용: ");
                pass = scanner.next();

                System.out.print("암호숫자 선택: ");
                skip = scanner.nextInt();

                ci = new cipher(pass, skip);
                ci.encrypt();
                break;
            case 2: ci.decrypt();
                break;

            default:
                break;
        }
        ci.print();
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

    void encrypt() {
        for (int i = 0; i < size; i++)
            cipher_arr[i] = (char) (cipher_arr[i] + skip);

        System.out.print("암호화 완료: ");
    }

    void decrypt() {
        File f = new File("./cipher.txt");

        if (f.exists()) {
            for (int i = 0; i < size; i++)
                cipher_arr[i] = (char) (cipher_arr[i] - skip);
        }
        else {
            System.out.println("복호화 시킬 파일이 없습니다!");
            return;
        }

        System.out.print("복호화 완료: ");
    }

    void print() {
        System.out.println(cipher_arr);
    }
}
