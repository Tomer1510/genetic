package genetic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomereiges on 5/28/15.
 */
public class MonoCryptor extends Cryptor {
    private String key;
    public MonoCryptor(String key) {
        this.key = key;
    }
    private static Map<String, String> encryptCache = new HashMap<>();
    private static Map<String, String> decryptCache = new HashMap<>();
    public MonoCryptor() {
        this.key = "zebrascdfghijklmnopqtuvwxy";

    }

    public void setKey(String key) {
        this.key = key;
    }

    public static String decrypt(String key, String text) {
        if (text.length() > 100 && MonoCryptor.decryptCache.containsKey(key)) {
            return MonoCryptor.decryptCache.get(key);
        }
        text = text.toLowerCase();
        key = key.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += key.charAt(text.charAt(i) - 'a');
        }
        if (text.length() > 100) {
            MonoCryptor.decryptCache.put(key, ret);
        }
        return ret;
    }

    public static String encrypt(String key, String text) {
        if (text.length() > 100 && MonoCryptor.encryptCache.containsKey(key)) {
            return MonoCryptor.encryptCache.get(key);
        }
        text = text.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += (char)('a' + key.indexOf(text.charAt(i)));
        }
        if (text.length() > 100) {
            MonoCryptor.encryptCache.put(key, ret);
        }
        return ret;
    }

    public String decrypt(String text) {
        if (text.length() > 100 && MonoCryptor.decryptCache.containsKey(this.key)) {
            return MonoCryptor.decryptCache.get(this.key);
        }
        text = text.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += this.key.charAt(text.charAt(i) - 'a');
        }
        if (text.length() > 100) {
            MonoCryptor.decryptCache.put(this.key, ret);
        }
        return ret;
    }


    public String encrypt(String text) {
        if (text.length() > 100 && MonoCryptor.encryptCache.containsKey(this.key)) {
            return MonoCryptor.encryptCache.get(this.key);
        }
        text = text.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += (char)('a' + this.key.indexOf(text.charAt(i)));
        }
        if (text.length() > 100) {
            MonoCryptor.encryptCache.put(this.key, ret);
        }
        return ret;
    }


}
