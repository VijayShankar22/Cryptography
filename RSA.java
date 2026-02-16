import java.math.BigInteger;
import java.util.Scanner;

public class RSA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== RSA MENU =====");
            System.out.println("1. Encryption (generate keys and encrypt message)");
            System.out.println("2. Decryption (using private key d and n)");
            System.out.println("3. Brute Force (factor n, recover private key and decrypt)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception ex) {
                System.out.println("Invalid choice.");
                continue;
            }
            if (choice == 1) {
                try {
                    System.out.print("Enter prime p: ");
                    BigInteger p = new BigInteger(sc.nextLine().trim());
                    System.out.print("Enter prime q (different from p): ");
                    BigInteger q = new BigInteger(sc.nextLine().trim());

                    if (p.equals(q)) {
                        System.out.println("p and q must be different.");
                        continue;
                    }

                    BigInteger n = p.multiply(q);
                    BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

                    BigInteger e = BigInteger.valueOf(65537);
                    if (!phi.gcd(e).equals(BigInteger.ONE) || e.compareTo(phi) >= 0) {
                        e = BigInteger.valueOf(3);
                        while (!phi.gcd(e).equals(BigInteger.ONE)) {
                            e = e.add(BigInteger.TWO);
                        }
                    }
                    BigInteger d = e.modInverse(phi);

                    System.out.println("\nPublic Key (e, n): " + e + ", " + n);
                    System.out.println("Private Key (d, n): " + d + ", " + n);

                    System.out.print("\nEnter plaintext message: ");
                    String message = sc.nextLine();
                    byte[] bytes = message.getBytes();

                    StringBuilder cipherOut = new StringBuilder();
                    for (byte b : bytes) {
                        int unsigned = b & 0xFF;
                        BigInteger mByte = BigInteger.valueOf(unsigned);
                        BigInteger cByte = mByte.modPow(e, n);
                        cipherOut.append(cByte.toString()).append(" ");
                    }

                    System.out.println("\nCiphertext (space-separated integers):");
                    System.out.println(cipherOut.toString().trim());

                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
            else if (choice == 2) {
                try {
                    System.out.print("Enter private key d: ");
                    BigInteger d = new BigInteger(sc.nextLine().trim());
                    System.out.print("Enter modulus n: ");
                    BigInteger n = new BigInteger(sc.nextLine().trim());

                    System.out.print("Enter ciphertext (space-separated integers): ");
                    String[] parts = sc.nextLine().trim().split("\\s+");

                    byte[] outBytes = new byte[parts.length];

                    for (int i = 0; i < parts.length; i++) {
                        BigInteger cPart = new BigInteger(parts[i]);
                        BigInteger plainByte = cPart.modPow(d, n);
                        outBytes[i] = (byte) plainByte.intValue();
                    }

                    String recovered = new String(outBytes);
                    System.out.println("\nDecrypted text: " + recovered);

                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
            else if (choice == 3) {
                try {
                    System.out.print("Enter public exponent e: ");
                    BigInteger e = new BigInteger(sc.nextLine().trim());
                    System.out.print("Enter modulus n: ");
                    BigInteger n = new BigInteger(sc.nextLine().trim());

                    System.out.print("Enter ciphertext (space-separated integers): ");
                    String cLine = sc.nextLine().trim();
                    String[] cipherParts = cLine.split("\\s+");

                    System.out.println("\nFactoring n... (trial division)");
                    BigInteger p = null, q = null;
                    BigInteger two = BigInteger.valueOf(2);

                    if (n.mod(two).equals(BigInteger.ZERO)) {
                        p = two;
                        q = n.divide(two);
                    } else {
                        BigInteger i = BigInteger.valueOf(3);
                        BigInteger limit = sqrt(n);

                        while (i.compareTo(limit) <= 0) {
                            if (n.mod(i).equals(BigInteger.ZERO)) {
                                p = i;
                                q = n.divide(i);
                                break;
                            }
                            i = i.add(two);
                        }
                    }
                    if (p == null) {
                        System.out.println("Could not factor n. Try smaller n.");
                        continue;
                    }

                    System.out.println("Found p = " + p);
                    System.out.println("Found q = " + q);

                    BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
                    BigInteger d = e.modInverse(phi);

                    System.out.println("Recovered private key d = " + d);

                    byte[] outBytes = new byte[cipherParts.length];
                    for (int i = 0; i < cipherParts.length; i++) {
                        BigInteger c = new BigInteger(cipherParts[i]);
                        BigInteger plainByte = c.modPow(d, n);
                        outBytes[i] = (byte) plainByte.intValue();
                    }

                    String result = new String(outBytes);
                    System.out.println("Decrypted text: " + result);

                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
            else if (choice == 4) {
                System.out.println("Goodbye!");
                break;
            }

            else {
                System.out.println("Invalid choice.");
            }
        }
    }
    private static BigInteger sqrt(BigInteger x) {
        BigInteger right = x;
        BigInteger left = BigInteger.ZERO;
        BigInteger mid;

        while (left.compareTo(right) <= 0) {
            mid = left.add(right).shiftRight(1);
            BigInteger midSq = mid.multiply(mid);
            int cmp = midSq.compareTo(x);

            if (cmp == 0) return mid;
            else if (cmp < 0) left = mid.add(BigInteger.ONE);
            else right = mid.subtract(BigInteger.ONE);
        }
        return left.subtract(BigInteger.ONE);
    }
}
