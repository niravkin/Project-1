import java.util.*;
public class ConstructingTrees {
	public static void main(String[] args) {
		//Part A:
		Part1Rec.BSTRec bstRec = new Part1Rec.BSTRec();
		Part2.AVLIter avlIter = new Part2.AVLIter();
		
		ArrayList<Integer> randomNumbers = ArraysOfIntegers.getRandomArray(1000);
		
		for (Integer i: randomNumbers) {
			//System.out.print(i + " ");
			avlIter.insertIter(i);
		}
		
		System.out.println(randomNumbers.get(0));
		System.out.println("AVL Tree Root: " + avlIter.root());
		System.out.println("AVL Tree Height: " + avlIter.height());
		
		for (Integer i: randomNumbers) {
			bstRec.insertRec(i);
		}
		System.out.println("BSTRec Root: " + bstRec.root());
		System.out.println("BSTRec Height: " + bstRec.height());
			
		//Part C:
		Part1Iter.BSTIter bstIter = new Part1Iter.BSTIter();
		for (Integer i: randomNumbers) {
			bstIter.insertIter(i);
		}
		System.out.println("BSTIter Root: " + bstIter.root());
		System.out.println("BSTIter Height: " + bstIter.height());		
	}
}
