import java.util.*;
public class ComparingImplementations {
	public static void main(String[] args) {
		Part2.AVLIter avlIter = new Part2.AVLIter();
		
		ArrayList<Integer> randomNumbers = ArraysOfIntegers.getRandomArray(10000);
		
		for (Integer i: randomNumbers) {
			avlIter.insertIter(i);
		}
		
		Part1Iter.BSTIter bstIter = new Part1Iter.BSTIter();
		for (Integer i: randomNumbers) {
			bstIter.insertIter(i);
		}
		
		System.out.println("BSTIter Random Array Traversal Count: " + bstIter.traversalCount());
		System.out.println("AVLIter Random Array Traversal Count: " + avlIter.traversalCount());		
		
		avlIter = new Part2.AVLIter();
		
		int[] sortedNumbers = ArraysOfIntegers.getSortedArray(10000);
		
		for (Integer i: sortedNumbers) {
			avlIter.insertIter(i);
		}
				
		bstIter = new Part1Iter.BSTIter();
		for (Integer i: sortedNumbers) {
			bstIter.insertIter(i);
		}
		
		System.out.println("BSTIter Sorted Array Traversal Count: " + bstIter.traversalCount());
		System.out.println("AVLIter Sorted Array Traversal Count: " + avlIter.traversalCount());
	}
}
