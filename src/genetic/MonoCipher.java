package genetic;




import java.io.File;
import java.util.*;

/**
 * Created by tomereiges on 5/2/15.
 */
public class MonoCipher extends Genetic<MonoCipher> {
    static Random rnd = new Random(System.currentTimeMillis());
    static final Frequencies f = new Frequencies("freq.txt");
    static MonoCryptor cryptor = new MonoCryptor();
    static String story = null;
    float fitness;
    String key;
    String target;
    static String cipherText;
    Text text;

    static float totalTime=0;

    public static class Comperator extends Genetic.Comperator<MonoCipher> {
        @Override
        public int compare(MonoCipher a, MonoCipher b) {
            return a.getFitness() < b.getFitness() ? -1 : a.getFitness() == b.getFitness() ? 0 : 1;
        }
    }


    static public String implode(List l) {
        String ret = "";
        for (Object o : l) {
            ret += o.toString();
        }
        return ret;
    }

    static public List<Character> explode(String s) {
        List<Character> ret = new ArrayList<>();
        for (char c : s.toCharArray()) {
            ret.add(c);
        }
        return ret;
    }



    @Override
    public void calculateFitness() {
        //f = new Frequencies("freq.txt");
        //Text.initCache(this.cipherText);
        String decryptedText = cryptor.decrypt(this.key, this.cipherText);
        if (this.key.equals("zebrascdfghijklmnopqtuvwxy")) {
            this.fitness = 0;
            return;
        }
        float fitness = 0;

        if (this.text == null)
            this.text = new Text(decryptedText, this.f);
        fitness = text.getScore();
        this.fitness = fitness;

        
    }


    private String mergeKeys(List<Character> a, List<Character> b, Text text_a, Text text_b) {
        if (false)
            return (implode(a));
        Map<Character, Float> monoStats_a = null;
        Map<Character, Float> monoStats_b = null;

        List<Character> letters = new LinkedList<>();
        List<Character> mate = new LinkedList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            letters.add(c);
        }

        for (char c = 'a'; c <= 'z'; c++) {
            char a_c = a.get(c - 'a');
            char b_c = b.get(c - 'a');
            int a_indexOf_b_c = a.indexOf(b_c);
            int b_indexOf_a_c = b.indexOf(a_c);
            int letters_indexOf_a_c = letters.indexOf(a_c);
            int letters_indexOf_b_c = letters.indexOf(b_c);

            if (a_c == 0 && b_c > 0) {
                mate.add(b_c);
                a.set(a_indexOf_b_c, (char) 0);
                letters.remove(letters_indexOf_b_c);
            } else if (a_c > 0 && b_c == 0) {
                mate.add(a_c);
                b.set(b_indexOf_a_c, (char) 0);
                letters.remove(letters_indexOf_a_c);
            } else if (a_c > 0 && b.get(c - 'a') > 0) {
                if (b.get(b_indexOf_a_c) > 0 && (a.get(b_indexOf_a_c) == 0 || rnd.nextBoolean() )) {
                    mate.add(b_c);
                    a.set(a_indexOf_b_c, (char) 0);
                    letters.remove(letters_indexOf_b_c);
                } else if (a.get(b_indexOf_a_c) > 0) {
                    mate.add(a_c);
                    b.set(b_indexOf_a_c, (char) 0);
                    letters.remove(letters_indexOf_a_c);
                } else {
                    char letter = letters.get((new Random()).nextInt(letters.size()));
                    letters.remove(letters.indexOf(letter));
                    mate.add(letter);
                    if (a.indexOf(letter) != -1)
                        a.set(a.indexOf(letter), (char) 0);
                    if (b.indexOf(letter) != -1)
                        b.set(b.indexOf(letter), (char) 0);
                }
            } else {
                char letter = letters.get((new Random()).nextInt(letters.size()));
                letters.remove(letters.indexOf(letter));
                mate.add(letter);
                if (a.indexOf(letter) != -1)
                    a.set(a.indexOf(letter), (char) 0);
                if (b.indexOf(letter) != -1)
                    b.set(b.indexOf(letter), (char) 0);
            }

        }
        return (implode(mate));
    }


    @Override
    public MonoCipher mate(MonoCipher b) {
        if (!this.target.equals(b.target)) {
            System.out.println("Targets don't match!");
            return null;
        }

        float temp = System.currentTimeMillis();

        List<Character> l1 = explode(this.key);
        List<Character> l2 = explode(b.key);
        Text text_a = this.text;
        Text text_b = b.text;
        String key = mergeKeys(l1, l2, text_a, text_b);
        MonoCipher.totalTime += System.currentTimeMillis()-temp;
        return new MonoCipher(this.cipherText, this.target, key, this, b);

    }

    @Override
    public void mutate() {
        //if(true)return;
        int pos1 = rnd.nextInt(this.key.length() - 1);
        int pos2 = pos1 + rnd.nextInt(this.key.length() - pos1);
        char arr[] = this.key.toCharArray();
        char temp = arr[pos1];
        arr[pos1] = arr[pos2];
        arr[pos2] = temp;
        this.key = new String(arr);
        this.text = new Text(MonoCryptor.decrypt(this.key, this.cipherText), MonoCipher.f, this.key, this.text.a, this.text.b);
        this.calculateFitness();
    }

    @Override
    public double getFitness() {
        return this.fitness;
    }

    @Override
    public double getDistance(MonoCipher a, MonoCipher b) {
        try {
            int ret = 0;
            for (int i = 0; i < a.key.length(); i++)
                ret += Math.abs((int) (a.key.charAt(i) - b.key.charAt(i)));
            return ret;
        } catch (Exception e) {
            System.out.println(a.key.length() + "\t 444" + b.key.length());
            System.out.println(a.key + "\t 444" + b.key);
            throw e;
        }
    }

    @Override
    public void generateValue() {
        //this.key="abcdefghijklmnopqrstuvwxyz";
        //if(true)return;
        List<Character> alphabet = new LinkedList<Character>();
        for (char c = 'a';c <= 'z';c++)
            alphabet.add(c);
        Collections.shuffle(alphabet);
        this.key = implode(alphabet);
    }

    private void readText(int cutoff) {
        if (MonoCipher.story != null) {
            this.target = MonoCipher.story.substring(0, cutoff);
            return;
        }
        try {
            File f = new File("text.txt");
            Scanner file = new Scanner(f);
            String text = "";
            while(file.hasNext()) {
                String line = file.nextLine();
                line = line.toLowerCase();
                //line = line.replaceAll("[^A-Za-z\\x20]", "");
                text += line;
            }
            this.target = text.substring(0, cutoff);
            MonoCipher.story=text;
            MonoCipher.cipherText = MonoCryptor.encrypt("zebrascdfghijklmnopqtuvwxy", this.target);
            // Frequencies f = new Frequencies("freq.txt");
        } catch (Exception e){

        }
    }

    public MonoCipher() {
        this.generateValue();
        this.readText(4000);
        this.calculateFitness();
    }

    public MonoCipher(String key) {
        this.key = key;
        this.readText(4000);
        this.calculateFitness();
    }

    public MonoCipher(String cipherText, String target) {
        this.generateValue();
        this.target = target;
        this.calculateFitness();
    }

    public MonoCipher(String cipherText, String target, String key, MonoCipher a, MonoCipher b) {
        //this.cipherText = cipherText;
        this.target = target;
        this.key = key;
        this.text = new Text(MonoCryptor.decrypt(this.key, this.cipherText), MonoCipher.f, this.key, a, b);
        this.calculateFitness();
    }


    private int keyMatches() {
        int ret = 0;
        for (int i = 0;i < this.key.length();i++)
            ret += (this.key.charAt(i) == "zebrascdfghijklmnopqtuvwxy".charAt(i))?1:0;
        return ret;
    }

    public String toString() {
        this.calculateFitness();

        String plainText = cryptor.decrypt(this.key, MonoCipher.cipherText.substring(0,300));
        int matches=0;
        for (int i = 0;i < this.key.length();i++) {
            if (this.key.charAt(i) == "zebrascdfghijklmnopqtuvwxy".charAt(i)) {
                matches += 1;
            }
            else {
                //plainText=plainText.replaceAll("("+String.valueOf(this.key.charAt(i))+")", "\033[1m $1\033[0m");
            }

        }
        return plainText + "\n"
                + this.key + "\tzebrascdfghijklmnopqtuvwxy\n"
                + matches + " out of 26" + "\n"
                + this.fitness +"\n";
    }

}
