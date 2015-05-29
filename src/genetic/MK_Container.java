package genetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MK_Container {

public ArrayList<Integer> Values = new ArrayList<Integer>();
public ArrayList<ArrayList<Integer> > weights = new ArrayList<ArrayList<Integer> >();
public ArrayList<Integer> threhold = new ArrayList<Integer>();
public int optimal_value;

private ArrayList<Integer> getElements(int num,BufferedReader br) throws IOException{
	// reading the next 'num' elements from the file. storing
	// those in a vector.
	ArrayList<Integer> ret = new ArrayList<Integer>();
	String line;
	for (int i=0;i<num;){
		if((line = br.readLine()) !=null)
			for (String c : line.replace((char)9, ' ').split(" "))
				if(cut(c).length()>0 && i<num)
					ret.add(i++,Integer.parseInt(cut(c)));
	}
	return ret;
}

public static String filename;

private String cut(String a){
	return a.replaceAll("\\D+","");
}

public MK_Container(String s){
	this.filename = s;
}

public MK_Container() throws IOException{
	// reading to the MultiKnap sack from a file
	// in the format specified in the assignment.
	File file = new File(filename);
	FileInputStream f = new FileInputStream(file);
	
	BufferedReader br = new BufferedReader(new InputStreamReader(f));
	
	// getting the number of sacks, number of items
	int sack_num = 0 , item_num=0;
	ArrayList<Integer> buffer = this.getElements(2, br);
	sack_num = buffer.get(0);
	item_num = buffer.get(1);
	// getting the values
	this.Values = this.getElements(item_num, br);
	// getting the thresholds
	this.threhold = this.getElements(sack_num, br);
	// getting the weights to each sack
	for (int i=0;i<sack_num;i++)
		this.weights.add(this.getElements(item_num, br));
	// getting the optimal value
	this.optimal_value = this.getElements(1, br).get(0);
	// closing everything
	br.close();
	f.close();	
}


}
