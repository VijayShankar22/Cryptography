import java.util.*;

public class playfaircipher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String plaintext = "";
        String key = "";
        String ciphertext = "";
        boolean added = false;

        while (true) {
            System.out.println("==== Play Fair cipher ====");
            System.out.println("1. Encryption");
            System.out.println("2. Decryption");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter the plain text: ");
                plaintext = sc.nextLine().toLowerCase().replaceAll("[^a-z]", "");
                System.out.print("Enter the key: ");
                key = sc.nextLine().toLowerCase();
                if (plaintext.length() % 2 != 0)plaintext += "x"; added = true;
                encrypt(plaintext, key);
                break;
            } else if (choice == 2) {
                System.out.print("Enter the cipher text: ");
                ciphertext = sc.nextLine().toLowerCase().replaceAll("[^a-z]", "");
                System.out.print("Enter the key: ");
                key = sc.nextLine().toLowerCase();
                decrypt(ciphertext, key);
                break;
            } else if (choice == 3) {
                System.exit(0);
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }
    }
    static char[][] generateKeyMatrix(String key) {
        key = key.replace('j', 'i');
        boolean[] used = new boolean[26];
        StringBuilder filteredKey = new StringBuilder();

        for (char c : key.toCharArray()) {
            if (c < 'a' || c > 'z') continue;
            if (!used[c - 'a']) {
                used[c - 'a'] = true;
                filteredKey.append(c);
            }
        }

        for (char c = 'a'; c <= 'z'; c++) {
            if (c == 'j') continue;
            if (!used[c - 'a']) {
                used[c - 'a'] = true;
                filteredKey.append(c);
            }
        }

        char[][] matrix = new char[5][5];
        int k = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = filteredKey.charAt(k++);
            }
        }
        return matrix;
    }

    static int[] findPosition(char[][] matrix, char c) {
        if (c == 'j') c = 'i';
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) return new int[]{i, j};
            }
        }
        return null;
    }

    static void encrypt(String text, String key) {
        char[][] matrix = generateKeyMatrix(key);

        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < sb.length() - 1; i += 2) {
            if (sb.charAt(i) == sb.charAt(i + 1)) {
                sb.insert(i + 1, 'x');
            }
        }
        if (sb.length() % 2 != 0) sb.append('x');

        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < sb.length(); i += 2) {
            char a = sb.charAt(i);
            char b = sb.charAt(i + 1);
            int[] posA = findPosition(matrix, a);
            int[] posB = findPosition(matrix, b);

            if (posA[0] == posB[0]) {
                encrypted.append(matrix[posA[0]][(posA[1] + 1) % 5]);
                encrypted.append(matrix[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) {
                encrypted.append(matrix[(posA[0] + 1) % 5][posA[1]]);
                encrypted.append(matrix[(posB[0] + 1) % 5][posB[1]]);
            } else {
                encrypted.append(matrix[posA[0]][posB[1]]);
                encrypted.append(matrix[posB[0]][posA[1]]);
            }
        }

        System.out.println("Encrypted Text: " + encrypted.toString().toUpperCase());
    }

    static void decrypt(String text, String key) {
        char[][] matrix = generateKeyMatrix(key);
        StringBuilder decrypted = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(matrix, a);
            int[] posB = findPosition(matrix, b);

            if (posA[0] == posB[0]) {
                decrypted.append(matrix[posA[0]][(posA[1] + 4) % 5]);
                decrypted.append(matrix[posB[0]][(posB[1] + 4) % 5]);
            } else if (posA[1] == posB[1]) {
                decrypted.append(matrix[(posA[0] + 4) % 5][posA[1]]);
                decrypted.append(matrix[(posB[0] + 4) % 5][posB[1]]);
            } else {
                decrypted.append(matrix[posA[0]][posB[1]]);
                decrypted.append(matrix[posB[0]][posA[1]]);
            }
        }
        System.out.println("Decrypted Text: " + decrypted.toString());
    }
}
