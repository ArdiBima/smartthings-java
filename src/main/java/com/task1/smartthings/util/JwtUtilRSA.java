package com.task1.smartthings.util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JwtUtilRSA {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public JwtUtilRSA() {
        try {
            String privateKeyPem = System.getProperty("JWT_PRIVATE_KEY");
            String publicKeyPem = System.getProperty("JWT_PUBLIC_KEY");
            // System.out.println("Private key PEM: " + privateKeyPem);
            if (privateKeyPem == null || publicKeyPem == null) {
                throw new IllegalStateException("JWT keys must be set as system properties.");
            }

            privateKey = loadPrivateKey(privateKeyPem);
            publicKey = loadPublicKey(publicKeyPem);
            System.out.println("RSA keys successfully loaded.");
        } catch (Exception e) {
            System.err.println("Error initializing RSA keys:");
            e.printStackTrace();
            // throw new RuntimeException(e);
        }
    }

    public PrivateKey loadPrivateKey(String pemKey) throws Exception {
        String key = pemKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");  // Remove all whitespace including newlines and spaces
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public PublicKey loadPublicKey(String pemKey) throws Exception {
        String key = pemKey
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");  // Remove all whitespace including newlines and spaces
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public String createToken(int userId) throws Exception {
        String header = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
        String payload = "{\"userId\":" + userId + ",\"exp\":" + (System.currentTimeMillis() / 1000 + Integer.parseInt(System.getProperty("JWT_TIME_LIMIT", "3600"))) + "}";

        String encodedHeader = base64UrlEncode(header);
        String encodedPayload = base64UrlEncode(payload);

        String data = encodedHeader + "." + encodedPayload;

        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signature = signer.sign();

        return data + "." + base64UrlEncode(signature);
    }

    public Integer verifyToken(String token) {
        try {
            System.out.println("Public key: " + publicKey.getAlgorithm() + ", Format: " + publicKey.getFormat());

            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                System.err.println("Invalid token structure.");
                return null;
            }
            
            String data = parts[0] + "." + parts[1];
            byte[] signatureBytes = base64UrlDecode(parts[2]);

            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update(data.getBytes(StandardCharsets.UTF_8));

            boolean valid = verifier.verify(signatureBytes);
            System.out.println("Signature verification result: " + valid);
            if (!valid) {
                return null;
            }


            String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
            if (!payloadJson.contains("\"userId\":") || !payloadJson.contains("\"exp\":")) {
                System.err.println("User ID or exp missing from payload.");
                return null;
            }

            long exp = Long.parseLong(payloadJson.replaceAll(".*\"exp\":(\\d+).*", "$1"));
            if (exp < System.currentTimeMillis() / 1000) {
                System.err.println("Token expired.");
                return null;
            }

            String idStr = payloadJson.replaceAll(".*\"userId\":(\\d+).*", "$1");
            return Integer.parseInt(idStr);

        } catch (Exception e) {
            System.err.println("Token verification failed:");
            e.printStackTrace();
            return null;
        }
    }

    private String base64UrlEncode(String str) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private byte[] base64UrlDecode(String str) {
        return Base64.getUrlDecoder().decode(str);
    }
}
