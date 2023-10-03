package com.kpmg.cacm.api.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RsaCypher {

    @Value("classpath:public-key")
    private Resource publicKeyFile;

    private PublicKey publicKey()
            throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        final byte[] keyBytes = Files.readAllBytes(this.publicKeyFile.getFile().toPath());
        final KeySpec spec = new X509EncodedKeySpec(keyBytes);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private void writeToFile(final File output, final byte[] toWrite) throws IOException {
        final FileOutputStream fos = new FileOutputStream(output);
        fos.write(toWrite);
        fos.flush();
        fos.close();
    }

    public final void decrypt(final byte[] input, final File output)
            throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.publicKey());
        this.writeToFile(output, cipher.doFinal(input));
    }

    public final String decrypt(final String encryptedText) {
        String decryptedText = "";
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, this.publicKey());
            decryptedText = new String(cipher.doFinal(Base64.decodeBase64(encryptedText)), StandardCharsets.UTF_8);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException exception) {
            RsaCypher.log.error("Error occurred while decrypting the text", exception);
        }
        return decryptedText;
    }
}
