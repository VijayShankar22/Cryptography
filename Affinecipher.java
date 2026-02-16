import java.util.Scanner;

public class Affinecipher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Affine Cipher ---");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Brute Force");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(sc, 1, 4);

            switch (choice) {
                case 1:
                    String plaintext = getPlainText(sc);
                    int k1 = getValidMultiplicativeKey(sc);
                    int k2 = getKey(sc, "Enter additive key (0-25): ");
                    System.out.println("Ciphertext: " + encrypt(plaintext, k1, k2));
                    break;

                case 2:
                    String ciphertext = getCipherText(sc);
                    int dk1 = getValidMultiplicativeKey(sc);
                    int dk2 = getKey(sc, "Enter additive key (0-25): ");
                    System.out.println("Plaintext: " + decrypt(ciphertext, dk1, dk2));
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

    private static int getKey(Scanner sc, String msg) {
        System.out.print(msg);
        return getIntInput(sc, 0, 25);
    }
    private static int getValidMultiplicativeKey(Scanner sc) {
        while (true) {
            System.out.print("Enter multiplicative key (1-25, coprime with 26): ");
            int k1 = getIntInput(sc, 1, 25);
            if (isKeyValid(k1)) return k1;
            System.out.println("Invalid multiplicative key! Must be coprime with 26.");
        }
    }

    private static String getPlainText(Scanner sc) {
        while (true) {
            System.out.print("Enter plaintext (a-z only, spaces allowed): ");
            String text = sc.nextLine();
            if (text.matches("[a-z ]+")) return text;
            System.out.println("Invalid! Only lowercase a-z and spaces allowed.");
        }
    }

    private static String getCipherText(Scanner sc) {
        while (true) {
            System.out.print("Enter ciphertext (A-Z only, spaces allowed): ");
            String text = sc.nextLine();
            if (text.matches("[A-Z ]+")) return text;
            System.out.println("Invalid! Only uppercase A-Z and spaces allowed.");
        }
    }

    private static boolean isKeyValid(int k1) {
        return gcd(k1, 26) == 1;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int modInverse(int k1) {
        for (int i = 1; i < 26; i++) {
            if ((k1 * i) % 26 == 1) return i;
        }
        return -1;
    }
    public static String encrypt(String text, int k1, int k2) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                result.append(' ');
                continue;
            }
            int enc = (k1 * (c - 'a') + k2) % 26;
            result.append((char) (enc + 'A'));
        }
        return result.toString();
    }

    public static String decrypt(String text, int k1, int k2) {
        int invK1 = modInverse(k1);
        if (invK1 == -1) return "Cannot decrypt. No modular inverse for key " + k1;

        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                result.append(' ');
                continue;
            }
            int dec = (invK1 * ((c - 'A' - k2 + 26)) % 26) % 26;
            result.append((char) (dec + 'a'));
        }
        return result.toString();
    }

    public static void bruteForce(String plain, String cipher) {
        for (int k1 = 1; k1 <= 25; k1++) {
            if (!isKeyValid(k1)) continue;
            for (int k2 = 0; k2 <= 25; k2++) {
                String test = encrypt(plain, k1, k2);
                if (test.equals(cipher)) {
                    System.out.println("Keys found: k1=" + k1 + ", k2=" + k2 + " -> " + test);
                }
            }
        }
    }
}
