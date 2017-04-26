package be.unamur.transitionsystem.test.mutation.equivalence.exact;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.State;

public class NFAPair {

	ArrayList<State> x = new ArrayList<State>();
	ArrayList<State> y = new ArrayList<State>();
	private Logger logger = LoggerFactory.getLogger(NFAPair.class);
	
	public NFAPair(ArrayList<State> x, ArrayList<State> y) {
		this.x =x;
		this.y = y;
	}
	
	public void printNFAPair() {
		System.out.println("X:");
		for (State s : x)
		 System.out.print(s.getName());
		System.out.println("Y:");
		for (State s:  y) 
			 System.out.print(s.getName());
		
	}
	
	public boolean equals(NFAPair p) {
		
		
		if (this.x.equals(p.x) && this.y.equals(p.y)) {
			//logger.debug("equals: " +x.toString() + " " + p.x.toString());
			//logger.debug("equals: " +y.toString() + " " + p.y.toString());
			return true;
		}
		else
			return false;
		
	}
	
	public String toString() {
		String res=new String();
		res += "<<";
		for (int i=0;i<x.size();i++) {
			if (i==x.size()-1)
				res += x.get(i).getName();
			else
			res += x.get(i).getName()+",";
		}
		res += ">,<";
		for (int i=0;i<y.size();i++) {
			if (i==y.size()-1)
				res += y.get(i).getName();
			else
			res += y.get(i).getName()+",";
		}
		res += ">>";
		return res;
		
	}
	
	
}
