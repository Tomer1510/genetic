package genetic;

/**
 * Created by tomereiges on 5/28/15.
 */
public class MonoCryptor extends Cryptor {
    private String key;
    public MonoCryptor(String key) {
        this.key = key;
    }

    public MonoCryptor() {
        this.key = "zebrascdfghijklmnopqtuvwxy";

    }

    public void setKey(String key) {
        this.key = key;
    }

    public static String decrypt(String key, String text) {
        text = text.toLowerCase();
        key = key.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += key.charAt(text.charAt(i) - 'a');
        }
        return ret;
    }

    public static String encrypt(String key, String text) {
        text = text.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += (char)('a' + key.indexOf(text.charAt(i)));
        }
        return ret;
    }

    public String decrypt(String text) {
        text = text.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += this.key.charAt(text.charAt(i) - 'a');
        }
        return ret;
    }


    public String encrypt(String text) {
        text = text.toLowerCase();
        String ret = "";
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += (char)('a' + this.key.indexOf(text.charAt(i)));
        }
        return ret;
    }


}
