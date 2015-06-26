package genetic;



//import genetic.Pair;
import javafx.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import genetic.Frequencies.State;

/**
 * Created by tomereiges on 5/20/15.
 */

public class Text {

    private Frequencies idealFrequencies;
    private String text;
    private static Map<State, Integer> totalAmounts = null;
    private Map<Pair<State, String>, Float> frequencies;
    public MonoCipher a, b;
    private String myKey;
    public static int cacheCounter = 0;
    public static int cacheHit = 0;
    public static Map<Pair<State, String>, Float> cache;
    public static String cache_key = null;

    public static void initCache(String plainText) {

    }

    public Text(String text, Frequencies frequencies) {
        this.text = text;
        this.idealFrequencies = frequencies;
        this.calcTotalAmounts();
        doPreprocessingOptimization();
    }


    public Text(String text, Frequencies frequencies, String key, MonoCipher a, MonoCipher b) {
        this.text = text;
        this.idealFrequencies = frequencies;
        this.myKey = key;
        this.a = a;
        this.b = b;
        this.calcTotalAmounts();
        this.doParentsOptimizations();
        doPreprocessingOptimization();
    }

    private List<Pair<String, Float>> getListByState(State state) {
        switch(state) {
            case BI_IN_BEGINNING: return this.idealFrequencies.BI_IN_BEGINNING;
            case BI_IN_END: return this.idealFrequencies.BI_IN_END;
            case BI_WITH_SPACE: return this.idealFrequencies.BI_WITH_SPACE;
            case BI_WITHOUT_SPACE: return this.idealFrequencies.BI_WITHOUT_SPACE;
            case TRI_WITH_SPACE: return this.idealFrequencies.TRI_WITH_SPACE;
            case TRI_WITHOUT_SPACE: return this.idealFrequencies.TRI_WITHOUT_SPACE;
            default: return null;
        }
    }

    private void doParentsOptimizations() {

        if (this.a == null || this.b == null || this.myKey == null)
            return;

        //if (this.frequencies == null)
        this.frequencies = new HashMap<>();
        Map<State, Boolean> enabled = new HashMap<>();

        enabled.put(State.MONO, false);
        enabled.put(State.WORDS, false);
        enabled.put(State.BI_IN_BEGINNING, false);
        enabled.put(State.BI_IN_END, false);
        enabled.put(State.BI_WITH_SPACE, true);
        enabled.put(State.BI_WITHOUT_SPACE, true);
        enabled.put(State.TRI_WITH_SPACE, true);
        enabled.put(State.TRI_WITHOUT_SPACE, true);

        // MONO
        if (false) {
            for (int i = 0; i < a.key.length(); i++) {
                if (myKey.charAt(i) == a.key.charAt(0)) {
                    Pair<State, String> p = new Pair(State.MONO, MonoCryptor.decrypt(a.key, String.valueOf(myKey.charAt(i))));
                    this.frequencies.put(p, a.text.frequencies.get(p));
                } else if (myKey.charAt(i) == b.key.charAt(0)) {
                    Pair<State, String> p = new Pair(State.MONO, MonoCryptor.decrypt(b.key, String.valueOf(myKey.charAt(i))));
                    this.frequencies.put(p, b.text.frequencies.get(p));
                }
            }
        }

        for (State state : State.values()) {
            if (enabled.get(state)) {
                for (Pair<String, Float> p : this.getListByState(state)) {
                    Pair<State, String> pair = new Pair(state, p.getKey());
                    if (MonoCryptor.encrypt(a.key, p.getKey()).equals(MonoCryptor.encrypt(this.myKey, p.getKey()))) {
                        this.frequencies.put(pair, a.text.frequencies.get(pair));
                    }
                    else if (MonoCryptor.encrypt(b.key, p.getKey()).equals(MonoCryptor.encrypt(this.myKey, p.getKey()))) {
                        this.frequencies.put(pair, b.text.frequencies.get(pair));

                    }
                }
            }
        }

    }


    private static void preprocessing(String text) {
        Text.cache = new HashMap<>();
        //System.out.println(text.substring(0,100));
        for (char c1 = 'a';c1 <= 'z';c1++) {
            for (char c2 = 'a';c2 <= 'z';c2++) {
                String str = String.valueOf(c1)+String.valueOf(c2);
                int occ = (text.length() - text.replace(str, "").length())/2;
                Text.cache.put(new Pair(State.BI_WITH_SPACE, str), 0.5F*100F*occ/Text.totalAmounts.get(State.BI_WITH_SPACE));
                Text.cache.put(new Pair(State.BI_WITHOUT_SPACE, str), 0.5F*100F*occ/Text.totalAmounts.get(State.BI_WITHOUT_SPACE));
            }
        }


        for (char c1 = 'a';c1 <= 'z';c1++) {
            for (char c2 = 'a'; c2 <= 'z'; c2++) {
                for (char c3 = 'a'; c3 <= 'z'; c3++) {
                    String str = String.valueOf(c1) + String.valueOf(c2) + String.valueOf(c3);
                    int occ = (text.length() - text.replace(str, "").length()) / 3;
                    Text.cache.put(new Pair(State.TRI_WITH_SPACE, str), (1/3F)*100F * occ / Text.totalAmounts.get(State.TRI_WITH_SPACE));
                    Text.cache.put(new Pair(State.TRI_WITHOUT_SPACE, str), (1/3F)*100F * occ / Text.totalAmounts.get(State.TRI_WITHOUT_SPACE));
                }


            }
        }
       // System.out.println("key is: "+key +"\t"+text.length()+"\t"+Text.cache.size());
    }


    private void doPreprocessingOptimization() {

        if(this.myKey==null)
            return;


        if (Text.cache == null)
            preprocessing(MonoCryptor.encrypt(this.myKey, this.text));

        Map<State, Boolean> enabled = new HashMap<>();
        enabled.put(State.MONO, false);
        enabled.put(State.WORDS, false);
        enabled.put(State.BI_IN_BEGINNING, false);
        enabled.put(State.BI_IN_END, false);
        enabled.put(State.BI_WITH_SPACE, true);
        enabled.put(State.BI_WITHOUT_SPACE, true);
        enabled.put(State.TRI_WITH_SPACE, true);
        enabled.put(State.TRI_WITHOUT_SPACE, true);


        for (State state : State.values()) {
            if (enabled.get(state)) {
                for (Pair<String, Float> p : this.getListByState(state)) {
                    Pair<State, String> pair = new Pair(state, MonoCryptor.encrypt(this.myKey, p.getKey()));
                    if (Text.cache.get(pair) != null) {

                        this.frequencies.put(new Pair(state, p.getKey()), Text.cache.get(pair));
                    }
                    else if(!p.getKey().contains(" ")) {
                        System.out.println(p.getKey());
                    }
                    else {
                     //   System.out.println(pair.toString());
                      //  System.out.println(Text.cache.get(pair));
                      //  System.out.println(Text.cache.size());
                    }

                }
            }
        }
    }


    public Text(String text) {
        this.text = text;
    }

    public Map<Character, Float> getMonoStats() {
        Map<Character, Float> ret = new HashMap<>();
        for (char c = 'a';c <= 'z';c++) {
            int occurrences = text.length() - text.replace(String.valueOf(c), "").length();
            float frequency = 100*(float)occurrences/text.replaceAll("[^a-z\\x20]", "").length();
            ret.put(c, frequency);
        }
        //line.length() - line.replace(".", "").length();
        //System.out.println(ret);
        return ret;
    }

    public float getScore() {
        try {
            if (this.frequencies == null)
                this.frequencies = new HashMap<>();
            Map<State, Float> factors = new HashMap<State, Float>();
            //factors.put(State.MONO, 0.05F);
            factors.put(State.BI_WITH_SPACE, 0.05F);
            factors.put(State.BI_WITHOUT_SPACE, 0.10F);
            factors.put(State.TRI_WITHOUT_SPACE, 0.55F);
            factors.put(State.TRI_WITH_SPACE, 0.30F);
            //factors.put(State.BI_IN_BEGINNING, 0.05F);
            //factors.put(State.BI_IN_END, 0.05F);
            float ret = 0;
            for (State state : State.values()) {
                if (factors.containsKey(state)) {
                    float diff = factors.get(state)* calcFrequenciesDiff(state);
                    //System.out.println(state + "\t" + ((float)diff/factors.get(state)));
                    ret += diff;
                }
            }

            return ret;
        } catch(Exception e) {
            throw e;
        }
    }

    public void test() {
        float sum = 0;
        for (Pair<String, Float> p : this.idealFrequencies.TRI_WITHOUT_SPACE) {

            sum += p.getValue();
        }
        System.out.println(sum);
    }


    private int count(int size, boolean withSpace) {
        int ret = 0;
        int flag = 0;
        for(int i = 0;i < text.length();i++) {
            if((text.charAt(i) >= 'a' && text.charAt(i) <= 'z') || (withSpace && text.charAt(i) == ' ')) {
                flag++;
                if (flag >= size) {
                    ret++;
                }
            }
            else {
                flag=0;
            }

        }

        return ret;
    }

    public void calcTotalAmounts() {
        if (Text.totalAmounts == null) {
            System.out.println("test");
            Text.totalAmounts = new HashMap<>();

            Text.totalAmounts.put(State.BI_IN_BEGINNING, (this.text.length() - this.text.replaceAll("(^|\\x20)([a-z]{2})", "$1").length()) / 2);
            Text.totalAmounts.put(State.BI_IN_END, (this.text.length() - this.text.replaceAll("([a-z]{2})([^a-z])", "$2").length()) / 2);
            //this.totalAmounts.put(State.BI_IN_BEGINNING, count(2, true)/2);
            //this.totalAmounts.put(State.BI_IN_END, count(2, true)/2);

            Text.totalAmounts.put(State.BI_WITH_SPACE, (this.text.length() - this.text.replaceAll("[a-z\\x20]{2}", "").length()) / 2);
            Text.totalAmounts.put(State.BI_WITHOUT_SPACE, (this.text.length() - this.text.replaceAll("[a-z]{2}", "").length()) / 2);

            Text.totalAmounts.put(State.TRI_WITH_SPACE, (this.text.length() - this.text.replaceAll("[a-z]{3}", "").length()) / 3);
            Text.totalAmounts.put(State.TRI_WITHOUT_SPACE, (this.text.length() - this.text.replaceAll("[a-z\\x20]{3}", "").length()) / 3);
        }

    }

    public float calcFrequenciesDiff(State which) {
        float ret = 0;
        //if (true) return 0.1f;
        switch (which) {
            case MONO:
                Map<Character, Float> stats = this.getMonoStats();
                for (Pair<Character, Float> p : this.idealFrequencies.MONO) {
                    Text.cacheCounter++;
                    float freq;
                    if (this.frequencies.containsKey(new Pair(which, String.valueOf(p.getKey())))) {
                        this.cacheHit++;
                        freq = this.frequencies.get(new Pair(which, String.valueOf(p.getKey())));
                    }
                    else {
                        freq = stats.get(p.getKey());
                    }
                    this.frequencies.put(new Pair(which, String.valueOf(p.getKey())), freq);
                    //ret += factor * Math.abs(p.getValue() - stats.get(Cryptor.encrypt(key, String.valueOf(p.getKey())).charAt(0)));
                    ret += Math.abs(p.getValue() - freq);
                    //ret /= 2;
                }
                return ret/this.idealFrequencies.MONO.size();


            case BI_IN_BEGINNING:
                for (Pair<String, Float> p : this.idealFrequencies.BI_IN_BEGINNING) {
                    Text.cacheCounter++;
                    float freq;
                    if (this.frequencies.containsKey(new Pair(which, p.getKey()))) {
                        this.cacheHit++;
                        freq = this.frequencies.get(new Pair(which, p.getKey()));
                    }
                    else {
                        int occ = (this.text.length() - this.text.replaceAll("(^|\\x20)("+p.getKey()+")", "$1").length())/2;
                        freq = (100F * occ / Text.totalAmounts.get(which)) / 2;
                    }

                    this.frequencies.put(new Pair(which, p.getKey()), freq);
                    ret += Math.abs(p.getValue() - freq);
                }
                return ret/this.idealFrequencies.BI_IN_BEGINNING.size();


            case BI_IN_END:
                for (Pair<String, Float> p : this.idealFrequencies.BI_IN_END) {
                    Text.cacheCounter++;
                    float freq;
                    if (this.frequencies.containsKey(new Pair(which, p.getKey()))) {
                        this.cacheHit++;
                        freq = this.frequencies.get(new Pair(which, p.getKey()));
                    }
                    else {
                        int occ = (this.text.length() - this.text.replaceAll("("+p.getKey()+")([^a-z])", "$2").length())/2;
                        freq = (100F * occ / Text.totalAmounts.get(which)) / 2;
                    }
                    this.frequencies.put(new Pair(which, p.getKey()), freq);
                    ret += Math.abs(p.getValue() - freq);
                }
                return ret/this.idealFrequencies.BI_IN_END.size();


            case BI_WITH_SPACE:
                for (Pair<String, Float> p : this.idealFrequencies.BI_WITH_SPACE) {
                    Text.cacheCounter++;

                    float freq;
                    if (this.frequencies.containsKey(new Pair(which, p.getKey()))) {
                        this.cacheHit++;
                        //System.out.println("adsfsa");
                        freq = this.frequencies.get(new Pair(which, p.getKey()));
                        //System.out.println(freq-(100F*occ/this.totalAmounts.get(which))/2);

                    }
                    else {
                        //System.out.println(this.frequencies.toString());

                        int occ = (this.text.length() - this.text.replace(p.getKey(), "").length())/2;
                        freq = (100F * occ / Text.totalAmounts.get(which)) / 2;
                    }
                    this.frequencies.put(new Pair(which, p.getKey()), freq);
                    ret +=  Math.abs(p.getValue() - freq);
                }
                return ret/this.idealFrequencies.BI_WITH_SPACE.size();


            case BI_WITHOUT_SPACE:
                for (Pair<String, Float> p : this.idealFrequencies.BI_WITHOUT_SPACE) {
                    Text.cacheCounter++;
                    float freq;
                    if (this.frequencies.containsKey(new Pair(which, p.getKey()))) {
                        this.cacheHit++;
                        freq = this.frequencies.get(new Pair(which, p.getKey()));
                    }
                    else {
                        int occ = (this.text.length() - this.text.replace(p.getKey(), "").length())/2;
                        freq = (100F * occ / Text.totalAmounts.get(which)) / 2;
                    }
                    this.frequencies.put(new Pair(which, p.getKey()), freq);
                    ret += Math.abs(p.getValue() - freq);
                }
                return ret/this.idealFrequencies.BI_WITHOUT_SPACE.size();


            case TRI_WITHOUT_SPACE:
                for (Pair<String, Float> p : this.idealFrequencies.TRI_WITHOUT_SPACE) {
                    Text.cacheCounter++;
                    float freq;
                    if (this.frequencies.containsKey(new Pair(which, p.getKey()))) {
                        this.cacheHit++;
                        freq = this.frequencies.get(new Pair(which, p.getKey()));
                    }
                    else {
                        int occ = (this.text.length() - this.text.replace(p.getKey(), "").length())/3;
                        freq = (100F * occ / Text.totalAmounts.get(which)) / 3;
                    }
                    this.frequencies.put(new Pair(which, p.getKey()), freq);
                    ret += Math.abs(p.getValue() - freq);
                }
                return ret/this.idealFrequencies.TRI_WITHOUT_SPACE.size();


            case TRI_WITH_SPACE:
                for (Pair<String, Float> p : this.idealFrequencies.TRI_WITH_SPACE) {
                    Text.cacheCounter++;
                    float freq;
                    if (this.frequencies.containsKey(new Pair(which, p.getKey()))) {
                        this.cacheHit++;
                        freq = this.frequencies.get(new Pair(which, p.getKey()));
                    }
                    else {
                        int occ = (this.text.length() - this.text.replace(p.getKey(), "").length())/3;
                        freq = (100F * occ / Text.totalAmounts.get(which)) / 3;
                    }
                    this.frequencies.put(new Pair(which, p.getKey()), freq);
                    ret += Math.abs(p.getValue() - freq);
                }
                return ret/this.idealFrequencies.TRI_WITH_SPACE.size();
        }

        return 0;
    }


    public String toString() {
        return this.text;
    }



}
