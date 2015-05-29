package genetic;



import genetic.Pair;
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
    private Map<State, Integer> totalAmounts;
    private Map<Pair<State, String>, Float> frequencies;
    public MonoCipher a, b;
    private String myKey;
    public static int cacheCounter = 0;
    public static int cacheHit = 0;

    public static void initCache(String plainText) {

    }

    public Text(String text, Frequencies frequencies) {
        this.text = text;
        this.idealFrequencies = frequencies;
        this.calcTotalAmounts();
    }


    public Text(String text, Frequencies frequencies, String key, MonoCipher a, MonoCipher b) {
        this.text = text;
        this.idealFrequencies = frequencies;
        this.myKey = key;
        this.a = a;
        this.b = b;
        this.calcTotalAmounts();
        this.doParentsOptimizations();
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
            //factors.put(State.MONO, 1F);
            factors.put(State.BI_WITH_SPACE, 0.15F);
            factors.put(State.BI_WITHOUT_SPACE, 0.25F);
            factors.put(State.TRI_WITHOUT_SPACE, 0.50F);
            factors.put(State.TRI_WITH_SPACE, 0.10F);
            //factors.put(State.BI_IN_BEGINNING, 0.05F);
            //factors.put(State.BI_IN_END, 0.05F);
            float ret = 0;
            for (State state : State.values()) {
                if (factors.containsKey(state)) {
                    //float startTime = System.nanoTime();
                    float diff = factors.get(state)* calcFrequenciesDiff(state);
                    //System.out.println(state + " " + diff/factors.get(state));
                    //System.out.println(state + " " + (System.nanoTime()-startTime)/1000);
                    ret += diff;
                }
            }
            //System.out.println();

            return ret;
        } catch(Exception e) {
           // System.out.println("asdfasdfsdf");
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
        if (this.a != null) {
            this.totalAmounts = this.a.text.totalAmounts;
            return;
        }
        this.totalAmounts = new HashMap<>();

        this.totalAmounts.put(State.BI_IN_BEGINNING, (this.text.length() - this.text.replaceAll("(^|\\x20)([a-z]{2})","$1").length())/2);
        this.totalAmounts.put(State.BI_IN_END, (this.text.length() - this.text.replaceAll("([a-z]{2})([^a-z])","$2").length())/2);
        //this.totalAmounts.put(State.BI_IN_BEGINNING, count(2, true)/2);
        //this.totalAmounts.put(State.BI_IN_END, count(2, true)/2);

        this.totalAmounts.put(State.BI_WITH_SPACE, (this.text.length() - this.text.replaceAll("[a-z\\x20]{2}","").length())/2);
        this.totalAmounts.put(State.BI_WITHOUT_SPACE, (this.text.length() - this.text.replaceAll("[a-z]{2}","").length())/2);

        this.totalAmounts.put(State.TRI_WITH_SPACE, (this.text.length() - this.text.replaceAll("[a-z]{3}","").length())/3);
        this.totalAmounts.put(State.TRI_WITHOUT_SPACE, (this.text.length() - this.text.replaceAll("[a-z\\x20]{3}","").length())/3);

    }

    public float calcFrequenciesDiff(State which) {
        float ret = 0;
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
                        freq = (100F * occ / this.totalAmounts.get(which)) / 2;
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
                        freq = (100F * occ / this.totalAmounts.get(which)) / 2;
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
                        freq = this.frequencies.get(new Pair(which, p.getKey()));
                        //System.out.println(freq-(100F*occ/this.totalAmounts.get(which))/2);

                    }
                    else {
                        int occ = (this.text.length() - this.text.replace(p.getKey(), "").length())/2;
                        freq = (100F * occ / this.totalAmounts.get(which)) / 2;
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
                        freq = (100F * occ / this.totalAmounts.get(which)) / 2;
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
                        freq = (100F * occ / this.totalAmounts.get(which)) / 3;
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
                        freq = (100F * occ / this.totalAmounts.get(which)) / 3;
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
