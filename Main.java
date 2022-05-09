package encryptdecrypt;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static StringBuilder cipherCode(String str, int key, int operation, int alg) {
        Map<Character, Character> charMap = new HashMap<>();
        if (alg == 1) {
            for (int i = 32; i <= 64; i++) {
                charMap.put((char) i, (char) i);
            }
            for (int i = 91; i <= 96; i++) {
                charMap.put((char) i, (char) i);
            }
            for (int i = 123; i <= 126; i++) {
                charMap.put((char) i, (char) i);
            }
            for (int i = 65; i <= 90; i++) {
                int cypherI = i + key <= 90 ? i + key : (i + key) - 26;
                charMap.put((char) i, (char) cypherI);
            }
            for (int i = 97; i <= 122; i++) {
                int cypherI = i + key <= 122 ? i + key : (i + key) - 26;
                charMap.put((char) i, (char) cypherI);
            }
        }
        if (alg == 2) {
            for (int i = 32; i <= 126; i++) {
                int cypherI = i + key <= 126 ? i + key : (i + key) - 95;
                charMap.put((char) i, (char) cypherI);
            }
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (operation == 1) {
                result.append(charMap.get(str.charAt(i)));
            }
            if (operation == 2) {
                result.append(getKey(charMap, str.charAt(i)));
            }
        }
        return result;
    }

    public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (K key: map.keySet())
        {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        String[] userArgs = new String[(int) Math.floor(args.length / 2.0)];
        int counter = 0;
        for (int i = 0; i < args.length; i += 2) {
            userArgs[counter] = args[i] + "::" + args[i + 1];
            counter++;
        }
        String userStr = "";
        String fileInName;
        String fileOutName = "";
        int alg = 1;
        int userKey = 0;
        int userOps = 1;

        for (String uStr : userArgs) {
            if (uStr.contains("-mode") && uStr.split("::")[1].contains("dec")) {
                userOps = 2;
            }
            if (uStr.contains("-alg") && uStr.split("::")[1].contains("unicode")) {
                alg = 2;
            }
            if (uStr.contains("-key")) {
                userKey = Integer.parseInt(uStr.split("::")[1]);
            }

            if (uStr.contains("-data")) {
                userStr = uStr.split("::")[1];
            } else if (uStr.contains("-in")) {
                fileInName = uStr.split("::")[1];
                File inFile = new File(fileInName);
                StringBuilder userSb = new StringBuilder();
                try (Scanner scanner = new Scanner(inFile)) {
                    while (scanner.hasNext()) {
                        userSb.append(scanner.nextLine());
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error: file not found!");
                    return;
                }
                userStr = userSb.toString();
            }

            if (uStr.contains("-out")) {
                fileOutName = uStr.split("::")[1];
            }
        }


        if (fileOutName.compareTo("") == 0) {
            System.out.println(cipherCode(userStr, userKey, userOps, alg));
        } else {
            File outFile = new File(fileOutName);
            try (FileWriter writer = new FileWriter(outFile)) {
                writer.write(cipherCode(userStr, userKey, userOps, alg).toString());
            } catch (IOException e) {
                System.out.println("Error: IO error!");
            }
        }

    }
}
