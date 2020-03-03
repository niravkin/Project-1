import java.util.*;
public class Part1Rec {
	private static class Node {
		public Node left = null;
		public Node right = null;
		public int val;
		
		public Node(int value) {
			val = value;
		}
	}
	
	public static class BSTRec {
		public int traversalCount = 0;
		public Node root;
		
		public int traversalCount() {
			return traversalCount;
		}
		
		private int rootHeight(Node n) {
			if (n == null) return 0;
			return Math.max(rootHeight(n.left), rootHeight(n.right)) + 1;
		}
		
		public int height() {
			return rootHeight(root);
		}
		
		public int root() {
			if (root != null) return root.val;
			return -1;
		}
		
		private void setNull(int value, Node n) {
			//only used to set leaves to null
			//every leaf must have a parent, except if the root is a leaf
			if (n == root && n.val == value) {
				root = null;
			}
			while(n != null) {
				if (n.right != null) {
					if (n.right.val == value) {
						n.right = null;
						return;
					}
				}
				if (n.left != null) {
					if (n.left.val == value) {
						n.left = null;
						return;
					}
				}
				
				if (value < n.val) {
					n = n.left;
				} else {
					n = n.right;
				}
			}
		}
		
		private void insertRecHelper(int value, Node n) {
			if (value > n.val) {
				//insert to right
				if (n.right == null) {
					n.right = new Node(value);
				} else {
					//recursive call
					traversalCount++;
					insertRecHelper(value, n.right);
				}
			} else {
				//insert to left
				if (n.left == null) {
					n.left = new Node(value);
				} else {
					//recursive call
					traversalCount++;		
					insertRecHelper(value, n.left);
				}
				
			}
		}
		
		public void insertRec(int value) {
			if (root == null) {
				root = new Node(value);
			} else {
				insertRecHelper(value, root);
			}
		}
		
		private void deleteRecHelper(int value, Node n) {
			if (n == null) {
				//the value is not in the tree
				return;
			}
			if (n.val == value) {
				//ready to delete
				if (n.left == null && n.right == null) {
					setNull(n.val, root);
					return;
				} else if (n.right != null && n.left == null) {
					//change left and right pointers to point to child's left and right pointers
					//change value to child's value
					Node temp = n.right;
					n.right = temp.right;
					n.left = temp.left;
					n.val = temp.val;
				} else if (n.right == null && n.left != null) {
					//change left and right pointers to point to child's left and right pointers
					//change value to child's value
					Node temp = n.left;
					n.right = temp.right;
					n.left = temp.left;
					n.val = temp.val;
				} else {
					Node successor = findNextRec(value);
					int successorVal = successor.val;
					//System.out.println("Successor: " + successor.val);
					//copy successor to n, delete successor
					deleteRecHelper(successorVal, n);
					n.val = successorVal;
				}
			} else {
				//recursive search
				if (value < n.val) {
					deleteRecHelper(value, n.left);
				} else {
					deleteRecHelper(value, n.right);
				}
			}
		}
		
		public void deleteRec(int value) {
			deleteRecHelper(value, root);
		}
		
		private void findNextRecHelper(int value, Node n, Stack<Node> s) {
			if (n == null) return;
			if (s.isEmpty() || s.peek().val != value) {
				if(n.right != null) findNextRecHelper(value, n.right, s);
				s.push(n);
				if (n.left != null) findNextRecHelper(value, n.left, s);
			}
		}
		
		public Node findNextRec(int value) {
			Stack<Node> s = new Stack<Node>();
			findNextRecHelper(value, root, s);
			while (!s.isEmpty()) {
				if (s.pop().val == value && !s.isEmpty()) {
					return s.pop();
				}
			}
			return null;
		}
		
		private void findPrevRecHelper(int value, Node n, Stack<Node> s) {
			if (n == null) return;
			if (s.isEmpty() || s.peek().val != value) {
				if(n.left != null) findPrevRecHelper(value, n.left, s);
				s.push(n);
				if (n.right != null) findPrevRecHelper(value, n.right, s);
			}
		}
		
		public Node findPrevRec(int value) {
			Stack<Node> s = new Stack<Node>();
			findPrevRecHelper(value, root, s);
			while(!s.isEmpty()) {
				if (s.pop().val == value && !s.isEmpty()) {
					return s.pop();
				}
			}
			return null;
		}
		
		private Node findMinRecHelper(Node r) {
			if (r==null) {
				return null;
			}
			if (r.left == null) {
				return r;
			} else {
				return findMinRecHelper(r.left);
			}
		}
		
		public Node findMinRec() {
			return findMinRecHelper(root);
		}
		
		private Node findMaxRecHelper(Node r) {
			if (r==null) {
				return null;
			}
			if (r.right == null) {
				return r;
			} else {
				return findMaxRecHelper(r.right);
			}
		}
		
		public Node findMaxRec() {
			return findMaxRecHelper(root);
		}
		
		public void print() {
			//prints in level order, prints null if there exists a parent, but no child
			Queue<Node> nodes = new LinkedList<Node>();
			nodes.add(root);
			while(!nodes.isEmpty()) {
				Node n = nodes.poll();
				if (n == null) {
					System.out.print("null ");
					continue;
				}
				System.out.print(n.val + " ");
				nodes.add(n.left);
				nodes.add(n.right);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		int[] nodes = {10, 13, 14, 8, 9, 11, 7, 12};
		/*
		 * 						 10
		 * 				   8		   13
		 * 				 7	  9     11	  14
		 * 						      12
		 */
		BSTRec b = new BSTRec();
		for (int i = 0; i < nodes.length; i++) {
			b.insertRec(nodes[i]);
		}
		b.print();
		Node min = b.findMinRec();
		if (min != null) System.out.println("Minimum: " + min.val); else System.out.println("Minimum: NULL");

		Node max = b.findMaxRec();
		if (max != null) System.out.println("Maximum: " + max.val); else System.out.println("Maximum: NULL");
		
		for (int i = 0; i < nodes.length; i++) {
			Node next = b.findNextRec(nodes[i]);
			if (next != null) System.out.println("Find Next (" + nodes[i] + "): " + next.val); else System.out.println("Find Next (" + nodes[i] + "): NULL");
		}

		for (int i = 0; i < nodes.length; i++) {
			Node prev = b.findPrevRec(nodes[i]);
			if (prev != null) System.out.println("Find Prev: (" + nodes[i] + "): " + prev.val); else System.out.println("Find Prev (" + nodes[i] + "): NULL");
		}
		
		System.out.println("Deleting 10");
		b.deleteRec(10);
		b.print();
	} 
	

}
