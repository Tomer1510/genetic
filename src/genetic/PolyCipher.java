package genetic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class PolyCipher extends Genetic<PolyCipher>{

    public static int key_length = 0;

    static Random rnd = new Random(System.currentTimeMillis());
    static final Frequencies f = new Frequencies("freq.txt");
    static PolyCryptor cryptor = new PolyCryptor();
    static String story = null;
    private float fitness;
    private String key;
    static String target;
    static String cipherText;
    private Text text;

    static ArrayList<String> cipherArray = new ArrayList<String>(); // stores all the i'th letters

    static ArrayList<Map<Character,Float>> cipherMonoFreq = new ArrayList<>();

    private ArrayList<Integer> find_dup (String subs){
        // Stores all the distances of substring subs in an arrayList
        String s[] = PolyCipher.cipherText.split(subs);
        if (s.length<=2)
            return null;
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i=0;i<s.length-1;i++)
            ret.add(subs.length()+s[i].length());
        return ret;
    }

    private ArrayList<Integer> find_div (int num){
        // Stores all the numbers who divide num in an arrayList
        ArrayList<Integer> ret = new ArrayList<Integer>();
        // We assume, that the minimal key length is 3.
        for (int i=3;i<=(int)Math.sqrt(num);i++)
            if (num%i==0)
                ret.add(i);
        return ret;
    }

    public int kasiski_Evaluation(){
        // Evaluates the encryption key's length
        if (PolyCipher.key_length != 0)
            return PolyCipher.key_length;

        Map<Integer,Integer> max = new HashMap<Integer,Integer>();

        String s = null;

        // Going through all substrings in the length of 3, and find if there are more than one occurrence of it.
        for (char a = 'a'; a<='z'; a++)
            for (char b = 'a'; b<='z'; b++)
                for (char c = 'a'; c<='z'; c++){
                    s = Character.toString(a)+Character.toString(b)+Character.toString(c);
                    if (find_dup(s)!= null){
                        // If there are multiple occurrences
                        for (int i:find_dup(s)){
                            for (int index:find_div(i)){
                                // Pushing all the divisors of all the distances
                                if (max.containsKey(index))
                                    max.put(index, max.get(index)+1);
                                else
                                    max.put(index, 1);
                            }
                        }
                    }
                }

        // extracting the key length
        int tKey = 0, tValue = 0;


        // Finding the divisor who appear the most. That will be the key length.
        for (Map.Entry<Integer, Integer> i : max.entrySet()){
            if (tValue < i.getValue()){
                tKey= i.getKey();
                tValue = i.getValue();
            }
        }

        PolyCipher.key_length = tKey;

        this.init_cipherArray();

        return PolyCipher.key_length;
    }

    private void init_cipherArray(){
        // Initialize CipherArray
        for (int i=0;i<PolyCipher.key_length;i++)
            PolyCipher.cipherArray.add("");
        for (int i=0;i<PolyCipher.cipherText.length();i++)
            PolyCipher.cipherArray.set(i%PolyCipher.key_length, PolyCipher.cipherArray.get(i%PolyCipher.key_length)+PolyCipher.cipherText.charAt(i));
        // Initialize cipherMonoFreq
        for (int i=0;i<PolyCipher.key_length;i++){
            this.text = new Text(PolyCipher.cipherArray.get(i));
            PolyCipher.cipherMonoFreq.add(text.getMonoStats());
        }

    }

    private float get_letter_fitness(int index, char c){
        float ret = 0;
        for (Map.Entry<Character, Float> m : PolyCipher.cipherMonoFreq.get(index).entrySet()){
            ret += Math.abs(f.getMonoFrequency(this.cryptor.decrypt(String.valueOf(c), m.getKey().toString()).charAt(0))-m.getValue());
        }
        ret /= 26;
        return ret;

    }

    static float totalTime=0;

    public static class Comperator extends Genetic.Comperator<PolyCipher> {
        @Override
        public int compare(PolyCipher a, PolyCipher b) {
            return a.getFitness() < b.getFitness() ? -1 : a.getFitness() == b.getFitness() ? 0 : 1;
        }
    }

    PolyCipher(){
        this.readText(20000);
        this.kasiski_Evaluation();
        this.generateValue();
        this.calculateFitness();
    }

    private void readText(int cutoff) {
        if (PolyCipher.story != null) {
            //  this.target = PolyCipher.story.substring(0, cutoff);
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
            PolyCipher.target = text.substring(0, cutoff);
            PolyCipher.story=text;
            PolyCipher.cipherText = PolyCryptor.encrypt("lemon", this.target);
            // Frequencies f = new Frequencies("freq.txt");
        } catch (Exception e){

        }
    }


    @Override
    public void calculateFitness() {
        this.fitness = 0;
        if (this.key.equals("lemon"))
            return;
        for (int i=0;i<PolyCipher.key_length;i++)
            this.fitness += this.get_letter_fitness(i, this.key.charAt(i));

        this.fitness /= PolyCipher.key_length;
    }

    @Override
    public PolyCipher mate(PolyCipher b) {
        String newKey = "";
        for (int i=0;i<PolyCipher.key_length;i++)
            newKey += (this.get_letter_fitness(i, this.key.charAt(i)) > this.get_letter_fitness(i, b.key.charAt(i))) ? this.key.charAt(i) : b.key.charAt(i);

        return new PolyCipher(newKey);
    }

    PolyCipher(String k){
        this.key = k;
        this.readText(20000);
        this.kasiski_Evaluation();
        this.calculateFitness();
    }

    @Override
    public void mutate() {
        // Changing a random character in the key.
        int pos = rnd.nextInt(PolyCipher.key_length);
        this.key = this.key.substring(0, pos) + (char)(97+rnd.nextInt(26)) + this.key.substring(pos+1);
    }

    @Override
    public double getFitness() {
        return this.fitness;
    }

    @Override
    public void generateValue() {
        // Generate a random key
        this.key = "";
        for (int i=0;i<PolyCipher.key_length;i++)
            key+= (char)(rnd.nextInt(26)+97);
        this.fitness = 0;

    }

    @Override
    public double getDistance(PolyCipher a, PolyCipher b) {
        return Math.abs(a.fitness - b.fitness);
    }

    public String toString() {
        this.calculateFitness();

        String plainText = cryptor.decrypt(this.key, PolyCipher.cipherText.substring(0,300));
        int matches=0;
        for (int i = 0;i < this.key.length();i++) {
            if (this.key.charAt(i) == "lemon".charAt(i)) {
                matches += 1;
            }
            else {
                //plainText=plainText.replaceAll("("+String.valueOf(this.key.charAt(i))+")", "\033[1m $1\033[0m");
            }

        }
        return plainText + "\n"
                + "KEY: "+ this.key + "\n"
                + matches + " out of "+("lemon".length()) + "\n"
                + this.fitness +"\n";
    }





}