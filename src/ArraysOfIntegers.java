import java.util.*;
public class ArraysOfIntegers {
	public static ArrayList<Integer> getRandomArray(int n) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		ArrayList<Integer> r = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			int randInt = (int)(Math.random() * (n + 1));
			while(map.containsKey(randInt)) {
				randInt = (int)(Math.random() * (n + 1));
			}
			r.add(randInt);
			map.put(randInt, 1);
		}
		return r;
	}
	public static int[] getSortedArray(int n) {
		int[] r = new int[n];
		for (int i = 0; i < n; i++) {
			r[i] = n-i;
		}
		return r;
	}
	public static void main(String[] args) {
		ArrayList<Integer> a = getRandomArray(10);
		System.out.println(a.size());
		for (Integer i: a) {
			System.out.print(i + " ");
		}		
		System.out.println();
		int[] b = getSortedArray(10);
		for (Integer i: b) {
			System.out.print(i + " ");
		}
	}
}


