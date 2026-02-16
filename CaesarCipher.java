import java.util.Scanner;

public class CaesarCipher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Caesar Cipher ====");
            System.out.println(""); 
            System.out.println("1. Encryption");
            System.out.println("2. Decryption");
            System.out.println("3. Exit");
            System.out.println("");
 
            System.out.print("Choose option (1-3): ");
            String choiceInput = scanner.nextLine();

            if (!choiceInput.matches("[1-3]")) {
                System.out.println("Invalid input. Enter 1, 2, or 3.");
                continue;
            }

            int choice = Integer.parseInt(choiceInput);

            if (choice == 1) {

                String plaintext;
                while (true) {
                    System.out.print("Enter plain text (a-z and spaces only): ");
                    plaintext = scanner.nextLine();
                    if (plaintext.matches("[a-z ]+")) break;
                    System.out.println("Invalid input. Only lowercase letters (a-z) and spaces are allowed.");
                }

                int key = getValidKey(scanner);

                StringBuilder cipher = new StringBuilder();
                for (char ch : plaintext.toCharArray()) {
                    if (ch == ' ') {
                        cipher.append(' ');
                    } else {
                        char c = (char) ((ch - 'a' + key) % 26 + 'A');
                        cipher.append(c);
                    }
                }

                System.out.println("Encrypted Cipher Text (A-Z, spaces preserved): " + cipher.toString());

            } else if (choice == 2) {

                String ciphertext;
                while (true) {
                    System.out.print("Enter cipher text (A-Z and spaces only): ");
                    ciphertext = scanner.nextLine();
                    if (ciphertext.matches("[A-Z ]+")) break;
                    System.out.println("Invalid input. Only uppercase letters (A-Z) and spaces are allowed.");
                }

                System.out.println("Choose decryption method:");
                System.out.println("1. Enter key manually");
                System.out.println("2. Brute-force all possibilities");

                String methodInput = scanner.nextLine();
                while (!methodInput.matches("[1-2]")) {
                    System.out.println("Invalid input. Enter 1 or 2.");
                    methodInput = scanner.nextLine();
                }

                int method = Integer.parseInt(methodInput);

                if (method == 1) {
                    int key = getValidKey(scanner);

                    StringBuilder decrypted = new StringBuilder();
                    for (char ch : ciphertext.toCharArray()) {
                        if (ch == ' ') {
                            decrypted.append(' ');
                        } else {
                            char p = (char) ((ch - 'A' - key + 26) % 26 + 'a');
                            decrypted.append(p);
                        }
                    }

                    System.out.println("Decrypted Plain Text: " + decrypted.toString());

                } else {
                    System.out.println("Brute Force Results:");
                    for (int key = 0; key < 26; key++) {
                        StringBuilder attempt = new StringBuilder();
                        for (char ch : ciphertext.toCharArray()) {
                            if (ch == ' ') {
                                attempt.append(' ');
                            } else {
                                char p = (char) ((ch - 'A' - key + 26) % 26 + 'a');
                                attempt.append(p);
                            }
                        }
                        System.out.println("Key " + key + ": " + attempt.toString());
                    }
                }

            } else if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            }
        }
        scanner.close();
    }

    private static int getValidKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter key (0-25): ");
            String input = scanner.nextLine();

            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            int key = Integer.parseInt(input);
            if (key < 0 || key > 25) {
                System.out.println("Key must be between 0 and 25.");
            } else {
                return key;
            }
        }
    }
}
