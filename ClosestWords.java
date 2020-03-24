/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ClosestWords {
    ArrayList<String> closestWords = null;
    int closestDistance = -1;
    ArrayList<int[]> M = new ArrayList<>(10);

    /*
    int left(int col, int row) {
	return M.get(row).get(col-1);
    }
    int diag(int col, int row) {
	return M.get(row-1).get(col-1);
    }
    int up(int col, int row) {
	return M.get(row-1).get(col);
	}*/
    int dynaDist(String w1, String w2, int startC, int startR) {
	// w1 === cols, w2 === rows
	int currC, currR;
	int l,u,d, res;
	for(currR = startR; currR < w2.length()+1; currR++) {
	    int[] m = M.get(currR);
	    int[] upDiagR = M.get(currR-1);
	    for(currC = startC; currC < w1.length()+1; currC++) {
		l = m[currC-1] + 1; // left
		u = upDiagR[currC] + 1; // up
		d = upDiagR[currC-1] + diagVal(w1,w2,currC,currR); // diag + diagVal
		res = d;
		if(l < res) {
		    res = l;
		}
		if(u < res) {
		    res = u;
		}
		m[currC] = res;
	    }
	}
	return M.get(w2.length())[w1.length()];
    }

    int diagVal(String w1, String w2, int currC, int currR) {
	if(w1.charAt(currC-1) == w2.charAt(currR-1)) {
	    return 0;
	}
	else {
	    return 1;
	}
	       
    }
    boolean first = true;
    String oldWord = "";
    int Distance(String w1, String w2) {
	int[] start = new int[]{1,1};
	if(first) {
	    initM(w1.length()+1, w2.length()+1);
	    first = false;
	}
	else {
	    start = maybeExtendM(w1, w2,oldWord);
	}
	int res2 = dynaDist(w1,w2, start[0], start[1]);
	oldWord = w2;
	return res2;
    }
  
    void initM(int cols, int rows) {
	M.clear();
	M.ensureCapacity(rows);
	int[] rowBaseCase = new int[cols];
	for(int i = 0; i < cols; i++) {
	    rowBaseCase[i] = i;
	}
	M.add(rowBaseCase);
	for(int i = 1; i < rows; i++) {
	    int[] r = new int[cols];
	    r[0] = i;
	    M.add(r);
	}
	return;
    }
    
    // remRows stands for "remaining rows"
    void cutM(int remRows) {
	// Example: Let remRows be 3
	// We keep rows 0, 1, 2
	M.subList(remRows,M.size()).clear();
    }
    
    int[] maybeExtendM(String w1, String newW, String oldW) {
	int same = nrOfSame(newW,oldW);
	if(same < oldW.length()) {
	    /* if(same == 0) {
		// OK we need to regenerate whole M
		initM(w1.length()+1,newW.length()+1);
		return new int[]{1,1};
	    }
	    else {*/
		// Shorten down the word until we can extend by the base-case
		cutM(same+1); // +1 to take into account basecase-row
		oldW = oldW.substring(0,same);
		// }
	}
	// Extend base-case
	for(int i = oldW.length()+1; i <= newW.length(); i++) {
	    int[] newRow = new int[w1.length()+1];
	    newRow[0] = i;
	    M.add(newRow);
	}
	return new int[]{1, oldW.length()+1};
    }
    /*
    void printM(int cols, int rows) {
	for(int  i = 0; i < rows; i++) {
	    for(int j = 0; j < cols; j++) {
		System.out.print(M.get(i).get(j));
		System.out.print(", ");
	    }
	    System.out.println();
	}
	}*/
    int nrOfSame(String oldW, String newW) {
	int i = 0;
	int shortest = Math.min(oldW.length(),newW.length());
	while(i < shortest && oldW.charAt(i) == newW.charAt(i)) {
	    i++;
	}
	return i;
    }

    public ClosestWords(String w, ArrayList<String> wordList) {
	for (String s : wordList) {
	    int dist = Distance(w, s);
	    // System.out.println("d(" + w + "," + s + ")=" + dist);
	    if (dist < closestDistance || closestDistance == -1) {
		closestDistance = dist;
		closestWords = new ArrayList<String>();
		closestWords.add(s);
	    }
	    else if (dist == closestDistance)
		closestWords.add(s);
	}
    }

    int getMinDistance() {
	return closestDistance;
    }
    List<String> getClosestWords() {
	return closestWords;
    }
}
