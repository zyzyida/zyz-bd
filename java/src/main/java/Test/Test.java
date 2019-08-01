package Test;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println("90度的正弦值：" + Math.sin(Math.PI / 2));
        Character ch = new Character('a');

        char[] helloArray = {'r', 'u', 'n', 'o', 'o', 'b'};
        String helloString = new String(helloArray);
        int len = helloString.length();
        System.out.println(helloString);
        System.out.println(len);

        StringBuffer sBuffer = new StringBuffer("菜鸟教程");
        sBuffer.append("www");
        System.out.println(sBuffer);

        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine()) {
            String str1 = scan.nextLine();
            System.out.println(str1);
        }
        scan.close();

    }

}

