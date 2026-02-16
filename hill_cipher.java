import java.util.Scanner;

public class hill_cipher {

    private static final int MOD = 26;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Hill Cipher =====");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                encrypt(sc);
            } else if (choice == 2) {
                decrypt(sc);
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
        sc.close();
    }

    private static void encrypt(Scanner sc) {
        System.out.print("Enter plaintext (letters only): ");
        String plaintext = sc.next().toUpperCase().replaceAll("[^A-Z]", "");

        System.out.print("Enter size of key matrix (n for n×n): ");
        int n = sc.nextInt();

        int[][] key = new int[n][n];
        System.out.println("Enter key matrix (row-wise): ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = sc.nextInt() % MOD;
            }
        }
        while (plaintext.length() % n != 0) {
            plaintext += "X";
        }

        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += n) {
            int[] ptVector = new int[n];
            for (int j = 0; j < n; j++) {
                ptVector[j] = plaintext.charAt(i + j) - 'A';
            }

            int[] ctVector = multiplyMatrixVector(key, ptVector);
            for (int val : ctVector) {
                cipher.append((char) (val + 'A'));
            }
        }

        System.out.println("Ciphertext: " + cipher);
    }

    private static void decrypt(Scanner sc) {
        System.out.print("Enter ciphertext (letters only): ");
        String ciphertext = sc.next().toUpperCase().replaceAll("[^A-Z]", "");

        System.out.print("Enter size of key matrix (n for n×n): ");
        int n = sc.nextInt();

        int[][] key = new int[n][n];
        System.out.println("Enter key matrix (row-wise): ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = sc.nextInt() % MOD;
            }
        }

        int[][] invKey = invertMatrixMod26(key);
        if (invKey == null) {
            System.out.println("Key matrix is not invertible mod 26!");
            return;
        }

        System.out.println("Inverse key matrix mod 26:");
        printMatrix(invKey);

        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += n) {
            int[] ctVector = new int[n];
            for (int j = 0; j < n; j++) {
                ctVector[j] = ciphertext.charAt(i + j) - 'A';
            }

            int[] ptVector = multiplyMatrixVector(invKey, ctVector);
            for (int val : ptVector) {
                plain.append((char) (val + 'A'));
            }
        }

        System.out.println("Decrypted plaintext: " + plain);
    }

    private static int[] multiplyMatrixVector(int[][] matrix, int[] vector) {
        int n = matrix.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = ((sum % MOD) + MOD) % MOD;
        }
        return result;
    }

    private static int[][] invertMatrixMod26(int[][] key) {
        int n = key.length;
        int det = determinant(key, n) % MOD;
        if (det < 0) det += MOD;

        int detInv = modInverse(det, MOD);
        if (detInv == -1) return null;

        int[][] adj = adjoint(key);
        int[][] inv = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = (adj[i][j] * detInv) % MOD;
                if (inv[i][j] < 0) inv[i][j] += MOD;
            }
        }
        return inv;
    }
    private static int determinant(int[][] matrix, int n) {
        if (n == 1)
            return matrix[0][0];
        int det = 0;
        int sign = 1;
        int[][] temp = new int[n][n];
        for (int f = 0; f < n; f++) {
            getCofactor(matrix, temp, 0, f, n);
            det += sign * matrix[0][f] * determinant(temp, n - 1);
            sign = -sign;
        }
        return det;
    }

    private static void getCofactor(int[][] mat, int[][] temp, int p, int q, int n) {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = mat[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }
    private static int[][] adjoint(int[][] matrix) {
        int n = matrix.length;
        if (n == 1) {
            return new int[][]{{1}};
        }

        int[][] adj = new int[n][n];
        int sign;
        int[][] temp = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                getCofactor(matrix, temp, i, j, n);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = (sign * determinant(temp, n - 1)) % MOD;
                if (adj[j][i] < 0) adj[j][i] += MOD;
            }
        }
        return adj;
    }

    private static int modInverse(int a, int m) {
        a = ((a % m) + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1;
    }

    private static void printMatrix(int[][] mat) {
        for (int[] row : mat) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}
