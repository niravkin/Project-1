import java.util.*;
public class Part1Iter {
	private static class Node {
		public Node left = null;
		public Node right = null;
		public int val;
		
		public Node(int value) {
			val = value;
		}
	}
	
	public static class BSTIter {
		public Node root;
		public int traversalCount = 0;
		
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
						traversalCount++;
					} else {
						parent = curr;
						curr = curr.right;
						traversalCount++;
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
		
		public void deleteIter(int value) {
			//iterative search for the value while keeping track of the parent node
			Node parent = null;
			Node curr = root;
			while (curr != null) {
				if (curr.val == value) {
					break;
				} else if (value < curr.val) {
					parent = curr;
					curr = curr.left;
				} else {
					parent = curr;
					curr = curr.right;
				}
			}
			//if curr is null, the value is not in the tree
			if (curr == null) return;
			if (curr.left == null && curr.right == null) {
				//curr is a leaf node, find which child of its parent it is and set that child to null
				//if the parent is null, the root is a leaf and must be deleted, so set root to null
				if (parent == null) { 
					root = null;
					return;
				}
				if (parent.left != null && parent.left.val == curr.val) {
					parent.left = null; 
				} else {
					parent.right = null;
				} 
			} else if (curr.left != null && curr.right == null) {
				//curr has a left child but not a right child
				//change value to child's value
				Node temp = curr.left;
				curr.right = temp.right;
				curr.left = temp.left;
				curr.val = temp.val;
			} else if (curr.left == null && curr.right != null) {
				//curr has a right child but not a left child
				//change value to child's value
				Node temp = curr.right;
				curr.right = temp.right;
				curr.left = temp.left;
				curr.val = temp.val;
			} else {
				// curr has two children, copy the value of its inorder successor and delete that inorder successor
				// since curr has a right child, the inorder successor must be the minimum value in the right subtree
				Node successor = findMinIter(curr.right);
				int successorVal = successor.val;
				curr.val = successor.val;
				// the node with that value can either have no children or a right child
				// use the rules from above to find and delete that node
				parent = curr;
				curr = curr.right;
				while (curr != null) {
					if (curr.val == successorVal) {
						break;
					} else if (successorVal < curr.val) {
						parent = curr;
						curr = curr.left;
					} else {
						parent = curr;
						curr = curr.right;
					}
				}
				if (curr.right == null) {
					//the inorder successor was a leaf
					if (parent.right != null && parent.right.val == curr.val) {
						parent.right = null;
					} else {
						parent.left = null;
					}
				} else {
					//the inorder successor had a right child
					Node temp = curr.right;
					curr.right = temp.right;
					curr.left = temp.left;
					curr.val = temp.val;
				}
				
			}
		}
		
		public Node findNextIter(int value) {
			if (root == null) return null;
			//keep track of parent, curr, and next nodes
			Node curr = root;
			Node next = null;			
			while (curr != null) {
				if (value == curr.val) {
					if (curr.right != null) {
						return findMinIter(curr.right);
					} else {
						return next;
					}
				} else if (value < curr.val) {
					next = curr;
					curr = curr.left;
				} else if (value > curr.val) {
					curr = curr.right;
				}
			}
			
			return null;
		}
		
		public Node findPrevIter(int value) {
			if (root == null) return null;
			//keep track of parent, curr, and next nodes
			Node curr = root;
			Node prev = null;			
			while (curr != null) {
				if (value == curr.val) {
					if (curr.left != null) {
						return findMaxIter(curr.left);
					} else {
						return prev;
					}
				} else if (value < curr.val) {
					curr = curr.left;
				} else if (value > curr.val) {
					prev = curr;
					curr = curr.right;
				}
			}
			
			return null;
		}
	
		private Node findMinIter(Node n) {
			Node curr = n;
			if (curr == null) return null;
			while (curr.left != null) {
				curr = curr.left;
			}
			return curr;
		}
		
		public Node findMinIter() {
			Node curr = root;
			if (curr == null) return null;
			while (curr.left != null) {
				curr = curr.left;
			}
			return curr;
		}
		
		private Node findMaxIter(Node n) {
			Node curr = n;
			if (curr == null) return null;
			while (curr.right != null) {
				curr = curr.right;
			}
			return curr;
		}
		
		public Node findMaxIter() {
			Node curr = root;
			if (curr == null) return null;
			while (curr.right != null) {
				curr = curr.right;
			}
			return curr;
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
		BSTIter b = new BSTIter();
		for (int i = 0; i < nodes.length; i++) {
			b.insertIter(nodes[i]);
		}
		b.print();
		
		Node min = b.findMinIter();
		if (min != null) System.out.println("Minimum: " + min.val); else System.out.println("Minimum: NULL");
		
		Node max = b.findMaxIter();
		if (max != null) System.out.println("Maximum: " + max.val); else System.out.println("Maximum: NULL");
		
		for (int i = 0; i < nodes.length; i++) {
			Node next = b.findNextIter(nodes[i]);
			if (next != null) System.out.println("Find Next (" + nodes[i] + "): " + next.val); else System.out.println("Find Next (" + nodes[i] + "): NULL");
		}

		for (int i = 0; i < nodes.length; i++) {
			Node prev = b.findPrevIter(nodes[i]);
			if (prev != null) System.out.println("Find Prev: (" + nodes[i] + "): " + prev.val); else System.out.println("Find Prev (" + nodes[i] + "): NULL");
		}
		
		System.out.println("Deleting 10");
		b.deleteIter(10);
		b.print();

	}
	
}
