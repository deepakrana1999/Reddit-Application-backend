package com.programming.ranatech.springredditclone;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;



public class ExportKeys {
	
    public static void main(String[] args) {

        try {
            // Update these values according to your keystore
            String keystorePath = "src/main/resources/jwt.jks";
            String keystorePassword = "Deepak@123";
            String alias = "jwt";
            String keyPassword = "Deepak@123";

            // Load the JKS file
            KeyStore keyStore = KeyStore.getInstance("JKS");

            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                keyStore.load(fis, keystorePassword.toCharArray());
            }

            // Read Private Key
            PrivateKey privateKey =
                    (PrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());
            System.out.println("===="+privateKey+"=======");
            
            // Read Public Key
            PublicKey publicKey =
                    keyStore.getCertificate(alias).getPublicKey();
            System.out.println("======"+publicKey+"=======");
            KeyPair keyPair = new KeyPair(publicKey, privateKey);

            // Export Private Key
            exportKey(
                    keyPair.getPrivate().getEncoded(),
                    "PRIVATE KEY",
                    "src/main/resources/app.key"
            );

            // Export Public Key
            exportKey(
                    keyPair.getPublic().getEncoded(),
                    "PUBLIC KEY",
                    "src/main/resources/app.pub"
            );

            System.out.println("====================================");
            System.out.println("Private Key exported : app.key");
            System.out.println("Public Key exported  : app.pub");
            System.out.println("====================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void exportKey(byte[] key, String type, String fileName)
            throws Exception {

        String encoded = Base64.getMimeEncoder(64, "\n".getBytes())
                .encodeToString(key);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("-----BEGIN " + type + "-----\n");
            writer.write(encoded);
            writer.write("\n-----END " + type + "-----");
        }
    }

}
