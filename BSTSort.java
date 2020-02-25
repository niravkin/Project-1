import java.util.*;

public class BSTSort {
	private static class Node {
		public Node left = null;
		public Node right = null;
		public int val;
		
		public Node(int value) {
			val = value;
		}
	}
	
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
	
	public static class BSTIter {
		public Node root;
		
		public void insertIter(int value) {
			if (root == null) {
				//empty tree, set root
				root = new Node(value);
			} else {
				//start at root, keep track of a parent to insert it at the right spot
				Node curr = root;
				Node parent = null;
				while (curr != null) {
					if (value < curr.val) {
						parent = curr;
						curr = curr.left;
					} else {
						parent = curr;
						curr = curr.right;
					}
				}
				//curr is now null, it is either the left or right child of parent
				if (value < parent.val) {
					//left child
					parent.left = new Node(value);
				} else {
					//right child
					parent.right = new Node(value);
				}
			}
		}
		
		private void inorderHelper(Node n, ArrayList<Integer> a) {
			if (n.left != null) {
				inorderHelper(n.left, a);
			}
			a.add(n.val);
			if (n.right != null) {
				inorderHelper(n.right, a);
			}
		}
		
		public ArrayList<Integer> inorder() {
			ArrayList<Integer> ret = new ArrayList<Integer>();
			inorderHelper(root, ret);
			return ret;
			
		}
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> a = getRandomArray(25);
		BSTIter b = new BSTIter();
		System.out.println(a.size());
		for (Integer i: a) {
			System.out.print(i + " ");
			b.insertIter(i);
		}		
		System.out.println();
		a = b.inorder();
		for (Integer i: a) {
			System.out.print(i + " ");
		}		
	}
}
