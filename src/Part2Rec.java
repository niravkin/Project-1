import java.util.*;

public class Part2Rec {
	private static class Node {
		public Node left = null;
		public Node right = null;
		public int val;
		public int height = 1;
		
		public Node(int value) {
			val = value;
		}
	}
	
	public static class AVLRec {
		public Node root;
		
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
		
		private Node getParent(Node n) {
			Node curr = root;
			while(curr != null) {
				if (curr.val == n.val) {
					return null;
				} 
				if (curr.left != null && curr.left.val == n.val) return curr;
				if (curr.right != null && curr.right.val == n.val) return curr;
				if (n.val < curr.val) curr = curr.left;
				if (n.val > curr.val) curr = curr.right;
			}
			return null;
		}
		
		private void balance(Node c) {
				int balanceFactor = bf(c);
				if (Math.abs(balanceFactor) > 1) {
					//node is not balanced
					Node parent = getParent(c);
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
				}
				//even if the node did not need rebalancing, its height might need to be updated
				updateHeight(c);
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
		
		
		private void insertRecHelper(int value, Node n) {
			if (value > n.val) {
				//insert to right
				if (n.right == null) {
					n.right = new Node(value);
				} else {
					//recursive call
					insertRecHelper(value, n.right);
				}
			} else {
				//insert to left
				if (n.left == null) {
					n.left = new Node(value);
				} else {
					//recursive call		
					insertRecHelper(value, n.left);
				}
			}
			updateHeight(n);
			balance(n);
			if (Math.abs(bf(n)) > 1) {
				System.out.println("Node " + n.val + " has balance factor " + bf(n) + " and needs rebalancing.");
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
			updateHeight(n);
			balance(n);
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
		
		
		//print function to visualize tree from: https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
		private void printTree(List<Node> levelNodes, int level) {

			   List<Node> nodes = new ArrayList<Node>();

			   //indentation for first node in given level
			   printIndentForLevel(level);

			   for (Node treeNode : levelNodes) {

			       //print node data
			       System.out.print(treeNode == null?" ":treeNode.val);

			       //spacing between nodes
			       printSpacingBetweenNodes(level);

			       //if its not a leaf node
			       if(level>1){
			           nodes.add(treeNode == null? null:treeNode.left);
			           nodes.add(treeNode == null? null:treeNode.right);
			       }
			   }
			   System.out.println();

			   if(level>1){        
			       printTree(nodes, level-1);
			   }
			}

		private void printIndentForLevel(int level){
		   for (int i = (int) (Math.pow(2,level-1)); i >0; i--) {
		       System.out.print(" ");
		   }
		}

		private void printSpacingBetweenNodes(int level){
		   //spacing between nodes
		   for (int i = (int) ((Math.pow(2,level-1))*2)-1; i >0; i--) {
		       System.out.print(" ");
		   }
		}

		public void print() {
		   List<Node> list = new ArrayList<Node>();
		   list.add(root);
		   printTree(list, root.height);
		}
	}
	
	public static void main(String[] args) {
		int[] nodes1 = {20, 4, 26, 3, 9, 21, 30, 2, 7, 11};
		int[] nodes2 = {5, 2, 8, 1, 3, 7, 10, 4, 6, 9, 11, 12};
		AVLRec avl1 = new AVLRec();
		AVLRec avl2 = new AVLRec();
		AVLRec avl3 = new AVLRec();

		for (Integer i: nodes1) {
			avl1.insertRec(i);
			avl2.insertRec(i);
		}
		for (Integer i: nodes2) {
			avl3.insertRec(i);
		}
		
		System.out.println("Tree 1: ");
		avl1.print();
		System.out.println("Inserting 15:");
		avl1.insertRec(15);
		avl1.print();
		
		System.out.println("Tree 2: ");
		avl2.print();
		System.out.println("Inserting 8:");
		avl2.insertRec(8);
		avl2.print();
		
		System.out.println("Tree 3: ");
		avl3.print();
		System.out.println("Deleting 1:");
		avl3.deleteRec(1);
		avl3.print();
		
		
	}
}
