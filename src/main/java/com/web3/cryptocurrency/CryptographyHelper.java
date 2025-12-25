package com.web3.cryptocurrency;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class CryptographyHelper {

    public static String generateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexadecimalString = new StringBuilder();
            for(byte b : hash) {
                String hexString = Integer.toHexString(0xff & b);
                if (hexString.length() == 1) {
                    hexadecimalString.append('0');
                }
                hexadecimalString.append(hexString);
            }
            return hexadecimalString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair ellipticCurveCrypto() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec params = new ECGenParameterSpec("prime256v1");
            keyPairGenerator.initialize(params);
            return keyPairGenerator.generateKeyPair();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    //ECC to sign the  given transaction(message)
    //elliptic curve digital signature algorithm(ECDSA)
    public static byte[] sign(PrivateKey privateKey, String input) {
        Signature signature;
        byte[] output;
        try {
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(input.getBytes());
            output = signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    //Checks whether the given transaction belongs to the sender based on signature
    public static boolean verify(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
            ecdsaSignature.initVerify(publicKey);
            ecdsaSignature.update(data.getBytes());
            return ecdsaSignature.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
