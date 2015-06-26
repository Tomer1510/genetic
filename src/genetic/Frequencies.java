package genetic;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair;

/**
 * Created by tomereiges on 5/10/15.
 */
public class Frequencies {

    public enum State {
        WORDS, MONO, BI_WITH_SPACE, BI_IN_BEGINNING, BI_IN_END, BI_WITHOUT_SPACE, TRI_WITH_SPACE, TRI_WITHOUT_SPACE
    }

    List<String> raw;
    List<Pair<Integer, Float>> WORD_LENGTHS;
    List<Pair<Character, Float>> MONO;
    List<Pair<String, Float>> BI_WITH_SPACE;
    List<Pair<String, Float>> BI_IN_BEGINNING;
    List<Pair<String, Float>> BI_IN_END;
    List<Pair<String, Float>> BI_WITHOUT_SPACE;
    List<Pair<String, Float>> TRI_WITH_SPACE;
    List<Pair<String, Float>> TRI_WITHOUT_SPACE;

    public Frequencies() {
        this.raw = new LinkedList<String>();
        this.WORD_LENGTHS = new LinkedList<>();
        this.MONO = new LinkedList<>();
        this.BI_WITH_SPACE = new LinkedList<>();
        this.BI_IN_BEGINNING = new LinkedList<>();
        this.BI_IN_END = new LinkedList<>();
        this.BI_WITHOUT_SPACE = new LinkedList<>();
        this.TRI_WITH_SPACE = new LinkedList<>();
        this.TRI_WITHOUT_SPACE = new LinkedList<>();
    }


    public Frequencies(Frequencies frequencies) {
        this.raw = new LinkedList<String>();
        this.WORD_LENGTHS = new LinkedList<>();
        this.MONO = new LinkedList<>();
        this.BI_WITH_SPACE = new LinkedList<>();
        this.BI_IN_BEGINNING = new LinkedList<>();
        this.BI_IN_END = new LinkedList<>();
        this.BI_WITHOUT_SPACE = new LinkedList<>();
        this.TRI_WITH_SPACE = new LinkedList<>();
        this.TRI_WITHOUT_SPACE = new LinkedList<>();

        Collections.copy(frequencies.raw, this.raw);
        Collections.copy(frequencies.WORD_LENGTHS, this.WORD_LENGTHS);
        Collections.copy(frequencies.MONO, this.MONO);
        Collections.copy(frequencies.BI_WITH_SPACE, this.BI_WITH_SPACE);
        Collections.copy(frequencies.BI_IN_BEGINNING, this.BI_IN_BEGINNING);
        Collections.copy(frequencies.BI_IN_END, this.BI_IN_END);
        Collections.copy(frequencies.BI_WITHOUT_SPACE, this.BI_WITHOUT_SPACE);
        Collections.copy(frequencies.TRI_WITH_SPACE, this.TRI_WITH_SPACE);
        Collections.copy(frequencies.TRI_WITHOUT_SPACE, this.TRI_WITHOUT_SPACE);

    }

    public float getMonoFrequency(char c) {
        for(Pair<Character, Float> p : this.MONO) {
            if(p.getKey() == c)
                return p.getValue();
        }
        return 0;
    }

    public Frequencies(String filename) {
        try {
            this.raw = new LinkedList<String>();
            this.WORD_LENGTHS = new LinkedList<>();
            this.MONO = new LinkedList<>();
            this.BI_WITH_SPACE = new LinkedList<>();
            this.BI_IN_BEGINNING = new LinkedList<>();
            this.BI_IN_END = new LinkedList<>();
            this.BI_WITHOUT_SPACE = new LinkedList<>();
            this.TRI_WITH_SPACE = new LinkedList<>();
            this.TRI_WITHOUT_SPACE = new LinkedList<>();

            State state = State.WORDS;
            File file = new File(filename);
            Scanner reader = new Scanner(file);
            while(reader.hasNext()) {
                String line = reader.nextLine();
                this.raw.add(line);

                if (line.contains("sample")) {
                    if (line.startsWith("Word length"))
                        state = State.WORDS;
                    else if (line.startsWith("Monogram frequencies"))
                        state = State.MONO;
                    else if (line.startsWith("Most common bigrams including space"))
                        state = State.BI_WITH_SPACE;
                    else if (line.startsWith("Most common bigrams in the beginning of words"))
                        state = State.BI_IN_BEGINNING;
                    else if (line.startsWith("Most common bigrams in the end of words"))
                        state = State.BI_IN_END;
                    else if (line.startsWith("Most common bigrams not including space"))
                        state = State.BI_WITHOUT_SPACE;
                    else if (line.startsWith("Most common trigrams including space"))
                        state = State.TRI_WITH_SPACE;
                    else if (line.startsWith("Most common trigrams not including space"))
                        state = State.TRI_WITHOUT_SPACE;
                    continue;
                }

                int len = 2;
                if (state == State.MONO) {
                    len = 1;
                }
                else if (state.toString().contains("BI")) {
                    len = 2;
                }
                else if (state.toString().contains("TRI")) {
                    len = 3;
                }

                boolean spaces=true;
                if (state.toString().contains("WITH_SPACE"))
                    spaces=true;
                else if (state.toString().contains("WITHOUT_SPACE") || state==State.WORDS || state==State.MONO)
                    spaces=false;

                line = line.toLowerCase();
                Pattern p = Pattern.compile("(?:\\|\\x20)?([a-z0-9"+(spaces?"\\x20":"")+"]{"+len+"})\t*\\p{Space}*-[\\p{Space}0-9]*(\\d+.\\d+)%");
                Matcher m = p.matcher(line);
                while (m.find()) {
                    float freq = Float.parseFloat(m.group(2));
                    switch(state) {
                        case WORDS: this.WORD_LENGTHS.add(new Pair<>(Integer.parseInt(m.group(1).replaceAll("[^\\d]", "")), freq)); break;

                        case MONO: this.MONO.add(new Pair<>(m.group(1).charAt(0), freq)); break;

                        case BI_WITH_SPACE: this.BI_WITH_SPACE.add(new Pair<>(m.group(1), freq)); break;

                        case BI_IN_BEGINNING: this.BI_IN_BEGINNING.add(new Pair<>(m.group(1), freq)); break;

                        case BI_IN_END: this.BI_IN_END.add(new Pair<>(m.group(1), freq)); break;

                        case BI_WITHOUT_SPACE: this.BI_WITHOUT_SPACE.add(new Pair<>(m.group(1), freq)); break;

                        case TRI_WITH_SPACE: this.TRI_WITH_SPACE.add(new Pair<>(m.group(1), freq)); break;

                        case TRI_WITHOUT_SPACE: this.TRI_WITHOUT_SPACE.add(new Pair<>(m.group(1), freq)); break;

                    }

                    //System.out.println(l + ":\t" + freq + "%");
                }

            }
            //System.out.println(this.TRI_WITH_SPACE);

        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!");
        }
    }






}
