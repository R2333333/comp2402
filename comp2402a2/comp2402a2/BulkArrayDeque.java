package comp2402a2;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class BulkArrayDeque<T> extends ArrayDeque<T> {
	
	public BulkArrayDeque(Class<T> clazz) {
		super(clazz);
	}
	
	/**
	 * Remove all the elements with indices that belong to the range [i, j)
	 * @param i the starting index
	 * @param j the stopping index
	 */
   @Override
	 public void removeRange(int i, int j) {

	   if (i < 0 || i >= this.size() || j <= 0 || j >= this.size()) {
		   throw new IndexOutOfBoundsException();
	   }

	   if (i > j) {
		   // i must be less than j, so throw an exception here
		 return;
		   //  throw new IllegalArgumentException();
	   }


	   int numToRemove = j - i;
	   int numToRemain = this.size() - numToRemove;

	   //if(numToRemain>size()/2) {
		   // Need to create new array and copy remaining elements over
		   T[] newArray = f.newArray(numToRemain);

		   for (int k = 0; k < i; k++) {
			   newArray[k] = a[(this.j + k) % a.length];
		   }

		   for (int k = i; k < numToRemain; k++) {
			   int shiftedIndex = k + numToRemove;
			   newArray[k] = a[(this.j + shiftedIndex) % a.length];
		   }

		   a = newArray;
		   this.j = 0;
		   n = a.length;
	  // }else {
	   	//	for(int k)
	   //}


		return;
	}
	
	/**
	 * testing method
	 */
	public static void doIt(BufferedReader r, PrintWriter w){
		//
		// testing code for your BulkArrayDeque 
		// 
	}
	
	
	/**
	 * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
	 * and System.out or from filenames specified on the command line, then call doIt.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader r;
			PrintWriter w;
			if (args.length == 0) {
				r = new BufferedReader(new InputStreamReader(System.in));
				w = new PrintWriter(System.out);
			} else if (args.length == 1) {
				r = new BufferedReader(new FileReader(args[0]));
				w = new PrintWriter(System.out);				
			} else {
				r = new BufferedReader(new FileReader(args[0]));
				w = new PrintWriter(new FileWriter(args[1]));
			}
			long start = System.nanoTime();
			doIt(r, w);
			w.flush();
			long stop = System.nanoTime();
			System.err.println("Execution time: " + 1e-9 * (stop-start));
		} catch (IOException e) {
			System.err.println(e);
			System.exit(-1);
		}
	}
}
