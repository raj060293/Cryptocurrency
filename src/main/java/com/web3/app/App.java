package com.web3.app;

import com.web3.cryptocurrency.CryptographyHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyPair;
import java.security.Security;
import java.util.Base64;

public class App {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        KeyPair keyPair = CryptographyHelper.ellipticCurveCrypto();
        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));

    }
}
