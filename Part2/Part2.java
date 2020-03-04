import java.util.*;

public class Part2 {
	private static class Node {
		public Node left = null;
		public Node right = null;
		public int val;
		public int height = 1;
		
		public Node(int value) {
			val = value;
		}
	}
	
	public static class AVLIter {
		//counts how many time we traverse down a level to insert
		public int traversalCount = 0;
		public Node root;
		
		public int traversalCount() {
			return traversalCount;
		}
		
		public int height() {
			return root.height;
		}
		
		public int root() {
			return root.val;
		}
		
		private int bf(Node n) {
			//returns the balance factor of a node
			if (n == null) return 0;
			//null nodes have a height of zero
			int left = 0;
			int right = 0;
			if (n.left != null) {
				left = n.left.height;
			}
			if (n.right != null) {
				right = n.right.height;
			}
			//System.out.println("Returning " + left + " - " + right);
			return left - right;
		}
		
		private void updateHeight(Node n) {
			//used to update height of a node during rotations
			//null nodes have a height of zero
			int left = n.left != null ? n.left.height : 0;
			int right = n.right != null ? n.right.height : 0;
			//your height is the maximum of your left and right children's height, plus one
			n.height = Math.max(left, right) + 1;
		}
		
		private void rightRotate(Node n, Node parent) {
			//right rotate Node n
			//System.out.println("right rotating " + n.val);
			boolean nisroot = false;
			if (parent == null) nisroot = true;
			
			//the heights of n and its left child may change b/c of the rotation
			Node leftNode = n.left;
			n.left = leftNode.right;
			updateHeight(n);
			leftNode.right = n;
			updateHeight(leftNode);
			n = leftNode;
			
			if (nisroot) {
				root = n;
			} else {
				//parent is used to update reference in tree
				if (n.val > parent.val) {
					parent.right = n;
				} else {
					parent.left = n;
				}
			}
		}
		
		public void leftRotate(Node n, Node parent) {
			//left rotate Node n
			//System.out.println("left rotating " + n.val);
			boolean nisroot = false;
			if (parent == null) nisroot = true;
			
			
			//the heights of n and its right child may change b/c of the rotation
			Node rightNode = n.right;
			n.right = rightNode.left;
			updateHeight(n);
			rightNode.left = n;
			updateHeight(rightNode);
			n = rightNode;
			
			if (nisroot) {
				root = n;
			} else {
				//parent is used to update reference in tree
				if (n.val > parent.val) {
					parent.right = n;
				} else {
					parent.left = n;
				}
			}
		}
		
		private void balance(Stack<Node> stack) {
			//a stack of nodes is used to traverse back up the tree, updating heights and balancing each node
			//assume stack contains the parents of each node as you pop off the stack
			while(!stack.isEmpty()) {
				Node c = stack.pop();
				int balanceFactor = bf(c);
				if (Math.abs(balanceFactor) > 1) {
					//node is not balanced
					Node parent = stack.isEmpty() ? null : stack.pop();
					//determine which case we are in:
					if (balanceFactor > 0) {
						//left heavy, so we are guaranteed to have a left child
						if ((bf(c.left)) >= 0) {
							//System.out.println("LEFT LEFT");
							//left left, right rotate on c
							rightRotate(c, parent);
						} else {
							//System.out.println("LEFT RIGHT");
							//left right, left rotate left child, right rotate c
							leftRotate(c.left, c);
							rightRotate(c, parent);
						}
					} else {
						//right heavy, so we are guaranteed to have a right child
						if (bf(c.right) <= 0) {
							//System.out.println("RIGHT RIGHT");
							//right right, left rotate on c
							leftRotate(c, parent);
						} else {
							//System.out.println("RIGHT LEFT");
							//right left, right rotate right child, left rotate c
							rightRotate(c.right, c);
							leftRotate(c, parent);
						}
					}
					//put parent back on the stack in case it needs to be balanced
					if (parent != null) stack.push(parent);
				}
				//even if the node did not need rebalancing, its height might need to be updated
				updateHeight(c);
			}
		}
		
		public void insertIter(int value) {
			//use a stack to keep track of the path from the root node to the node inserted
			//each node in the stack may have its height/balance factor changed because of the insert
			Stack<Node> stack = new Stack<Node>();
			if (root == null) {
				//empty tree, set root
				root = new Node(value);
				root.height = 1;
			} else {
				//start at root, keep track of a parent to insert it at the right spot
				Node curr = root;
				Node parent = null;
				while (curr != null) {
					stack.push(curr); //traversed to a new node, push onto stack
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
				balance(stack);
			}
		}
		
		public void deleteIter(int value) {
			//use a stack to keep track of the path from the root node to the node inserted
			//each node in the stack may have its height/balance factor changed because of the delete
			Stack<Node> stack = new Stack<Node>();
			//iterative search for the value while keeping track of the parent node
			Node parent = null;
			Node curr = root;
			while (curr != null) {
				stack.push(curr); //traversed to a new node, push onto stack
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
				//change values to child's values
				Node temp = curr.left;
				curr.right = temp.right;
				curr.left = temp.left;
				curr.val = temp.val;
			} else if (curr.left == null && curr.right != null) {
				//curr has a right child but not a left child
				//change values to child's values
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
					stack.push(curr); //traversed to a new node, push onto stack
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
			balance(stack);
		}
		
		public Node findNextIter(int value) {
			//same as bst find next
			if (root == null) return null;
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
			//same as bst find prev
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
			return findMinIter(root);
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
			return findMaxIter(root);
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
		int[] nodes1 = {20, 4, 26, 3, 9, 21, 30, 2, 7, 11};
		int[] nodes2 = {5, 2, 8, 1, 3, 7, 10, 4, 6, 9, 11, 12};
		AVLIter avl1 = new AVLIter();
		AVLIter avl2 = new AVLIter();
		AVLIter avl3 = new AVLIter();

		for (Integer i: nodes1) {
			avl1.insertIter(i);
			avl2.insertIter(i);
		}
		for (Integer i: nodes2) {
			avl3.insertIter(i);
		}
		
		System.out.println("Tree 1: ");
		avl1.print();
		System.out.println("Inserting 15:");
		avl1.insertIter(15);
		avl1.print();
		
		System.out.println("Tree 2: ");
		avl2.print();
		System.out.println("Inserting 8:");
		avl2.insertIter(8);
		avl2.print();
		
		System.out.println("Tree 3: ");
		avl3.print();
		System.out.println("Deleting 1:");
		avl3.deleteIter(1);
		avl3.print();
		
		//Timings:
		AVLIter avl4 = new AVLIter();
		Part1Iter.BSTIter bstIter = new Part1Iter.BSTIter();
		ArrayList<Integer> randomNumbers = ArraysOfIntegers.getRandomArray(10000);
		
		long startTime = System.currentTimeMillis();
		//AVL:
		//10000 inserts
		for (Integer i: randomNumbers) {
			avl4.insertIter(i);
		}
		
		//10000 deletes
		for (Integer i: randomNumbers) {
			avl4.deleteIter(i);
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println("Time for AVL 10k inserts and 10k deletes: " + (int)((endTime - startTime)) + " milliseconds.");
		
		startTime = System.currentTimeMillis();
		//AVL:
		//10000 inserts
		for (Integer i: randomNumbers) {
			bstIter.insertIter(i);
		}
		
		//10000 deletes:
		for (Integer i: randomNumbers) {
			bstIter.deleteIter(i);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time for BST 10k inserts and 10k deletes: " + (int)((endTime - startTime)) + " milliseconds.");

	}
}



