package fallout76.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class CryptUtil {
    public static String decrypt(String data, String key) {

        String src = new String(Base64.getDecoder().decode(data));

        String iv = src.substring(0, 16);

        byte[] newSecret = Base64.getDecoder().decode(src.substring(16));

        StringBuilder finalKeyBuilder = new StringBuilder(key);
        while (finalKeyBuilder.length() < 32) {
            finalKeyBuilder.append("\0");
        }

        String finalKey = finalKeyBuilder.toString();

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    new SecretKeySpec(finalKey.getBytes(), "AES"),
                    new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8))
            );
            return new String(cipher.doFinal(newSecret));
        } catch (Exception e) {
            log.error("******CryptUtil.decrypt：解密失败，原因：{}", e.getMessage());
            return null;
        }
    }
}
