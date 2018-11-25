package comp2402a6;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class GeometricTree extends BinaryTree<GeometricTreeNode> {

	public GeometricTree() {
		super (new GeometricTreeNode());
	}
	
	public void inorderDraw() {
		assignLevels();
		// TODO: use your code here instead
		int count = 0;
		GeometricTreeNode u = r, prev = nil, next;
		r.position.y = 0;

		while (u != nil) {

			if (prev == u.parent) {
				if (u.left != nil) next = u.left;
				else if (u.right != nil){
					next = u.right;
					u.position.x = count++;
				}
				else {
					next = u.parent;
					u.position.x = count++;
				}
			} else if (prev == u.left) {
				if (u.right != nil) next = u.right;
				else next = u.parent;
				u.position.x = count;
				++count;
			} else {
				next = u.parent;
			}

			prev = u;
			u = next;
		}
	}

	/**
	 * Draw each node so that it's x-coordinate is as small
	 * as possible without intersecting any other node at the same level 
	 * the same as its parent's
	 */
	public void leftistDraw() {
		assignLevels();
		Queue<GeometricTreeNode> q = new LinkedList<>();
		q.add(r);
		r.position.x = 0;
		int count = 0;
		int currLevel = 0;
		while (!q.isEmpty()) {
			GeometricTreeNode u = q.remove();
			if (u.left != nil) q.add(u.left);
			if (u.right != nil) q.add(u.right);
			if(u.position.y > currLevel){
				++currLevel;
				count = 0;
			}
			u.position.x = count;
			++count;
		}
	}

	public void balancedDraw() {
		assignLevels();
		// TODO: use your code here instead
		GeometricTreeNode u = r, prev = nil, next;
		HashMap<GeometricTreeNode, Integer> s = branchSize();

		GeometricTreeNode lesserNode;
		GeometricTreeNode greaterNode;

		int x = 0;
		int y = 0;

		while(u != nil) {

			if(u.left == nil) {
				greaterNode = u.right;
				lesserNode = u.left;
			}
			else if(u.right == nil) {
				greaterNode = u.left;
				lesserNode = nil;
			}
			else if(s.get(u.right) < s.get(u.left)) {
				lesserNode = u.right;
				greaterNode = u.left;
			} else {
				lesserNode = u.left;
				greaterNode = u.right;
			}


			if(prev == u.parent) {
				u.position.x = x;
				u.position.y = y;

				if (lesserNode != nil) {
					y += 1;
					next = lesserNode;
				}
				else if (greaterNode != nil) {
					x += 1;
					next = greaterNode;
				}
				else {
					next = u.parent;
				}
			}

			else if(prev == lesserNode) {
				--y;

				if(u.right != nil) {
					next = greaterNode;
					++x;
				}
				else
					next = u.parent;
			}

			else
				next = u.parent;

			prev = u;
			u = next;
		}

	}

		
	protected void assignLevels() {
		assignLevels(r, 0);
	}
	
	protected void assignLevels(GeometricTreeNode u, int i) {
		int l = i;

		GeometricTreeNode ut = u, prev = nil, next;
		while (ut != nil) {
			if (prev == ut.parent) {
				if (ut.left != nil) next = ut.left;
				else if (ut.right != nil) next = ut.right;
				else next = ut.parent;
				ut.position.y = l++;
			} else if (prev == ut.left) {
				if (ut.right != nil) next = ut.right;
				else next = ut.parent;
				--l;
			} else {
				next = ut.parent;
				--l;
			}
			prev = ut;
			ut = next;
		}
	}

	public HashMap<GeometricTreeNode,Integer> branchSize(){
		GeometricTreeNode u = r, prev = nil, next;
		HashMap<GeometricTreeNode, Integer> s = new HashMap<>();

		while (u != nil) {
			if (prev == u.parent) {
				if (u.left != nil) next = u.left;
				else if (u.right != nil) next = u.right;
				else {
					next = u.parent;
					s.put(u, 1);
				}
			} else if (prev == u.left) {
				if (u.right != nil) next = u.right;
				else{
					next = u.parent;
					s.put(u, s.get(u.left)+1);
				}
			} else {
				next = u.parent;
				s.put(u, s.get(u.right)+1);

				if(u.left != nil)
					s.put(u, s.get(u)+s.get(u.left));
			}
			prev = u;
			u = next;
		}
		return s;
	}


	
	public static void main(String[] args) {
		GeometricTree t = new GeometricTree();
		galtonWatsonTree(t, 100);
		System.out.println(t);
		t.inorderDraw();
		System.out.println(t);
	}
}

