package genetic;

/**
 * Created by tomereiges on 5/28/15.
 */
public class PolyCryptor extends Cryptor {

    private static class Key {
        private String key;

        public Key(String key) {
            this.key = key;
        }

        public String toString(){
            return this.key;
        }

        public char get(int i) {
            return this.key.charAt(i%(this.key.length()));
        }

    }

    public static int mod(int a, int b) {
        return (((a % b) + b) % b);
    }

    public static String decrypt(String keyword, String text) {
        text = text.toLowerCase();
        keyword = keyword.toLowerCase();
        String ret = "";
        Key key = new Key(keyword);
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else {
                ret += (char) ('a' + mod(text.charAt(i) - key.get(i), 26));
            }
        }
        return ret;
    }

    public static String encrypt(String keyword, String text) {
        text = text.toLowerCase();
        String ret = "";
        Key key = new Key(keyword);
        for (int i = 0;i < text.length();i++) {
            if (text.charAt(i) > 'z' || text.charAt(i) < 'a')
                ret += text.charAt(i);
            else
                ret += (char)('a' + (text.charAt(i) + key.get(i) - 2*'a')%26);
        }
        return ret;
    }



}
