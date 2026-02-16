import java.security.*;
import java.util.*;

public class digitalsignature {

    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== DIGITAL SIGNATURE =====");
            System.out.println("1. Generate Key Pair (RSA)");
            System.out.println("2. Sign a Message");
            System.out.println("3. Verify a Signature");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    generateKeys();
                    break;

                case 2:
                    if (privateKey == null) {
                        System.out.println("Please generate keys first.");
                        break;
                    }
                    System.out.print("\nEnter message to sign: ");
                    String msg = sc.nextLine();
                    signMessage(msg);
                    break;

                case 3:
                    if (publicKey == null) {
                        System.out.println("Please generate keys first.");
                        break;
                    }
                    System.out.print("\nEnter original message: ");
                    String orig = sc.nextLine();

                    System.out.print("Enter signature (Base64): ");
                    String sig = sc.nextLine();

                    boolean ok = verifySignature(orig, sig);
                    System.out.println(ok ? "\nSignature is VALID " : "\nSignature is INVALID");
                    break;

                case 4:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    private static void generateKeys() {
        try {
            System.out.println("\nGenerating RSA Key Pair...");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();

            System.out.println("Key Pair Generated Successfully!");

            System.out.println("\nPUBLIC KEY (Base64):");
            System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));

            System.out.println("\nPRIVATE KEY (Base64):");
            System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        } catch (Exception e) {
            System.out.println("Error generating keys: " + e.getMessage());
        }
    }
    private static void signMessage(String message) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message.getBytes());
            byte[] signatureBytes = sign.sign();

            String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);

            System.out.println("\nSignature (Base64):");
            System.out.println(signatureBase64);

        } catch (Exception e) {
            System.out.println("Error signing message: " + e.getMessage());
        }
    }
    private static boolean verifySignature(String message, String signatureStr) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(publicKey);
            sign.update(message.getBytes());

            byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);

            return sign.verify(signatureBytes);

        } catch (Exception e) {
            System.out.println("Error verifying: " + e.getMessage());
            return false;
        }
    }
}
