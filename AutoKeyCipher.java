import java.util.Scanner;

public class AutoKeyCipher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Auto Key Cipher ---");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Brute Force");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(sc, 1, 4);

            switch (choice) {
                case 1:
                    String plaintext = getPlainText(sc);
                    int eKey = getKey(sc);
                    System.out.println("Ciphertext: " + encrypt(plaintext, eKey));
                    break;

                case 2:
                    String ciphertext = getCipherText(sc);
                    int dKey = getKey(sc);
                    System.out.println("Plaintext: " + decrypt(ciphertext, dKey));
                    break;

                case 3:
                    String bfPlain = getPlainText(sc);
                    String bfCipher = getCipherText(sc);
                    System.out.println("Brute force results:");
                    bruteForce(bfPlain, bfCipher);
                    break;

                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
            }
        }
    }

    private static int getIntInput(Scanner sc, int min, int max) {
        while (true) {
            if (sc.hasNextInt()) {
                int val = sc.nextInt();
                sc.nextLine();
                if (val >= min && val <= max) return val;
            } else sc.nextLine();
            System.out.print("Invalid! Enter a number between " + min + " and " + max + ": ");
        }
    }

    private static int getKey(Scanner sc) {
        System.out.print("Enter key (0-25): ");
        return getIntInput(sc, 0, 25);
    }

    private static String getPlainText(Scanner sc) {
        while (true) {
            System.out.print("Enter plaintext (a-z only): ");
            String text = sc.nextLine();
            if (text.matches("[a-z]+")) return text;
            System.out.println("Invalid! Only lowercase a-z allowed.");
        }
    }

    private static String getCipherText(Scanner sc) {
        while (true) {
            System.out.print("Enter ciphertext (A-Z only): ");
            String text = sc.nextLine();
            if (text.matches("[A-Z]+")) return text;
            System.out.println("Invalid! Only uppercase A-Z allowed.");
        }
    }

    public static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int enc = (c - 'a' + key) % 26;
            result.append((char) (enc + 'A'));
        }
        return result.toString();
    }

    public static String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int dec = (c - 'A' - key + 26) % 26;
            result.append((char) (dec + 'a'));
        }
        return result.toString();
    }

    public static void bruteForce(String plain, String cipher) {
        for (int key = 0; key <= 25; key++) {
            String test = encrypt(plain, key);
            if (test.equals(cipher)) {
                System.out.println("Key found: " + key + " -> " + test);
            }
        }
    }
}