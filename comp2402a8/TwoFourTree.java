package comp2402a8;


import java.util.List;
import java.util.Stack;

/**
 * The TwoFourTree class is an implementation of the 2-4 tree from notes/textbook.
 * The tree will store Strings as values. 
 * It extends the (modified) sorted set interface (for strings).
 * It implements the LevelOrderTraversal interface. 
 */

public class TwoFourTree extends StringSSet implements LevelOrderTraversal{
	/* your class MUST have a zero argument constructor. All testing will
	   use this constructor to create a 2-3 tree.
    */
	private Node r = NIL, posFind, processorNode;
	String processor;
	private int size = 0;
	private List<Node> levels = new Stack<>();




	public TwoFourTree(){
		// add whatever code you want.

		//r = NIL;
		//size = 0;
	}

	@Override
	public int size() {

		return size;
	}

	@Override
	public String find(String x) {
		Node curr = r, next = NIL, prev = NIL;
		String sucessor = null;

		while (curr != NIL){
//System.out.println("find");
			for(int i = 0; i < curr.data.length; ++i){

				if (curr.data[i] == EMPTY) {
					next = curr.children[i];
					break;
				}
				if (curr.data[i].compareTo(x) == 0) {
				    posFind = curr;
                    return curr.data[i];
                }
				if (x.compareTo(curr.data[i]) < 0) {
				    next = curr.children[i];
					sucessor = curr.data[i];
					break;
                }else if (i == 2) {next = curr.children[3]; processor = curr.data[i];}
                }
            prev = curr;
			curr = next;
		}
		posFind = prev;

		for(int i = 0; i < prev.data.length; ++i) {

			if (prev.data[i] == EMPTY){
				return sucessor;
			}

			if (x.compareTo(prev.data[i]) < 0) return prev.data[i];
		}

		return null;
	}

	@Override
	public boolean add(String x) {
		Node curr;//, prev = NIL;
//		System.out.println("add");
		if (size == 0) {
			return addEmpty(x);
		}
		String f = find(x);

		if(f != EMPTY) {
			if (f.compareTo(x) == 0) return false;
		}
////		System.out.println("?");
		curr = posFind;

		if(dataNum(curr) < 3) {  ++size; return addLess3(curr, x);}
		else {
			split(curr, x);
			++size;
		}

		return true;
	}
	private boolean notLeaf(Node node){
		for (int i = 0; i < node.children.length; ++i){
			if (node.children[i] != NIL) {
//             System.out.println("!!!!");
                return true;
            }
		}
		return false;
	}

	@Override
	public boolean remove(String x) {
		String f = find(x);
		if (f == null || f.compareTo(x) != 0) {
            System.out.printf("removeNotFound f= %s\n", f);
		    return false;
        }
		Node curr = posFind;
//        for (String i : posFind.data)
//            System.out.printf("!!! %s ",i);
		if (!notLeaf(curr)){
//            System.out.print("notLeaf\n");
			if (curr == r)
				removeMoreThan1(curr, x);
			if (dataNum(curr) > 1)
                removeMoreThan1(curr, x);
		}
		System.out.println(processor);
		return true;
	}

	private void removeMoreThan1(Node node, String string){
		System.out.printf("removeMoreThan1\n");


	    for (int i = 0; i < 3; ++i){
			if (node.data[i] != null){
				if (node.data[i].compareTo(string) == 0) {
				    node.data[i] = null;
                    for (int j = i; j < 2; ++j) {
                        node.data[j] = node.data[j + 1];
                        node.data[j + 1] = null;
                    }
				}

			}
		}
//	    for (String i : node.data)
//	        System.out.printf("!!! %s ",i);
	}

	@Override
	public void clear() {
		this.r = NIL;
	}

	@Override
	public String levelOrder() {
		String data = new String();
		Node curr = r, prev = NIL, next;
		prev.parent = NIL;
		boolean first = true;
		if (size == 0)
			return null;
		if (r == NIL)
			return null;

		levels.add(curr);

		while (!levels.isEmpty()) {
			curr = levels.remove(0);
			if (curr == NIL) return data;
			if(prev != NIL) {
				if (curr.data[0].compareTo(prev.data[0]) < 0)
					data += "|";
				else if (curr.parent != NIL) data += ":";
//				System.out.println("trav1");
			}

			for (int i = 0; i < 3; ++i) {
				if (curr.data[i] != EMPTY) {
					data += curr.data[i];
					data += ",";
//					System.out.println(curr.data[i]);
				}
			}
			prev = curr;
            if (data.length() > 0)
			    data = data.substring(0,data.length()-1);
			for (Node node: prev.children) {
				if (node != NIL)
					levels.add(node);
			}
//			System.out.println("trav2");
		}
		return data;
	}

	private boolean addEmpty(String s){
		r = new Node();
		r.data[0] = s;
		++size;
		return true;
	}

	private int dataNum(Node n){
		int num = 0;

		for(int i = 0; i < n.data.length; ++i) {
			if (n.data[i] != EMPTY) ++num;
		}

		return num;
	}

	private boolean addLess3(Node prev, String x){

		//String tmp[] = new String[3];
		int count = dataNum(prev);
		if (count == 0) { prev.data[0] = x; return true;}

		if(count == 1){

			if(x.compareTo(prev.data[0]) < 0) {
				prev.data[1] = prev.data[0];
				prev.data[0] = x;
			}

			else prev.data[1] = x;
			return true;
		}

		if (count == 2){
			if (x.compareTo(prev.data[0]) < 0){
				prev.data[2] = prev.data[1];
				prev.data[1] = prev.data[0];
				prev.data[0] = x;
			} else if (x.compareTo(prev.data[1]) < 0){
				prev.data[2] = prev.data[1];
				prev.data[1] = x;
			} else prev.data[2] = x;
		}

		if (count == 3){
			Node n = new Node();
			n.data[0] = prev.data[2];
			n.parent = prev.parent;
			prev.data[2] = EMPTY;
			int pos = posInP(prev.parent, prev);
			for (int i = 3; i > pos + 1; --i){
				prev.parent.children[i] = prev.children[i-1];
			}
			prev.parent.children[pos+1] = n;
		}

		return false;
	}

	private int posInP(Node p, Node n){
		for(int i = 0; i < 4; ++i){
			if (p.children[i] == n)
				return i;
		}
		return 0;
	}



	private String goUp(Node n, String x){
		String s;
		if (dataNum(n) < 3) return x;
		if (x.compareTo(n.data[2]) < 0 && x.compareTo(n.data[1]) > 0)
			return x;

		else if (x.compareTo(n.data[2]) > 0) {
			s = n.data[2];
			n.data[2] = x;
		} else {
			s = n.data[1];
			if (x.compareTo(n.data[0]) < 0) {
				n.data[1] = n.data[0];
				n.data[0] = x;
			} else n.data[1] = x;
		}
		return s;
	}

	private void split(Node n, String x){
////		System.out.println(n.data[0]);
////		System.out.println(x);


		Node node1 = new Node(), node2 = new Node();

		int pos= posInP(n.parent, n), pDataNum = dataNum(n.parent);
//		System.out.printf("???  %d  ??? %s %s %s\n", dataNum(n.parent), n.parent.data[0],n.parent.data[1],n.parent.data[2]);

		String stringUp = goUp(n,x);
////		System.out.println(stringUp);
//		if(dataNum(n) < 3) return;
//
		if (pDataNum < 3) {
			if(n.parent == NIL) {
				r = new Node();
				n.parent = r;
			}
			addLess3(n.parent,stringUp);
////			System.out.println(stringUp);
			node1.data[0] = n.data[0];
			node1.data[1] = n.data[1];
			node2.data[0] = n.data[2];
			node1.parent = n.parent;
			node2.parent = n.parent;

			for (int i = 0; i < 2; ++i) {
				node1.children[i] = n.children[i];
				if (n.children[i] != NIL)
					n.children[i].parent = node1;
				node2.children[i] = n.children[i+2];
				if (n.children[i+2] != NIL)
					n.children[i+2].parent = node2;
			}

			n.parent.children[pos]   = node1;

			for (int i = 3; i >pos; --i)
				n.parent.children[i] = n.parent.children[i-1];

			n.parent.children[pos+1] = node2;


		} else {
////			System.out.println("???");
			Node curr = n, nodes[] = new Node[5];
			for (int i = 0; i < 5; ++i)
				nodes[i] = NIL;

//
			while (curr != NIL){
				//node1 = new Node();
				pos = posInP(curr.parent, curr);
//				System.out.println(pos);

				stringUp = goUp(curr, stringUp);
////				System.out.println(stringUp);
				if (dataNum(curr) < 3){
////					System.out.println("!!");
					addLess3(curr, stringUp);
					int j = 0;
					for (int i = 0; i < 5; ++i){
						if (nodes[i] != NIL) {
							curr.children[j] = nodes[i];
							curr.children[j].parent = curr;
							++j;
						}
					}
					return;
				}

				if (curr == r) {
					r = new Node();
					r.data[0] = stringUp;
					r.children[0] = new Node();
					r.children[1] = new Node();
					r.children[0].data[0] = curr.data[0];
					r.children[0].data[1] = curr.data[1];
					r.children[1].data[0] = curr.data[2];
					r.children[0].parent = r;
					r.children[1].parent = r;

					for (int i = 0; i < 3; ++i){
						r.children[0].children[i] = nodes[i];
						if (nodes[i] != NIL)
							nodes[i].parent = r.children[0];
					}
					for (int i = 0; i < 2; ++i){
						r.children[1].children[i] = nodes[i+3];
						if (nodes[i+3] != NIL)
							nodes[i+3].parent = r.children[1];
					}

					//break;
//					System.out.println("...");

					return;
				}
				node1 = new Node();
				node1.data[0] = curr.data[2];
//				System.out.println(curr.data[2]);

				curr.data[2] = EMPTY;
				for (int i = 0; i < 3; ++i) {
					curr.children[i] = nodes[i];
					if (nodes[i] != NIL)
						nodes[i].parent = curr;
				}
				curr.children[3] = NIL;

				for (int i = 0; i < 2; ++i) {
					node1.children[i] = nodes[i+3];
					if (nodes[i+3] != NIL)
						nodes[i+3].parent = node1;
				}

//				pos = posInP(curr.parent, curr);

				for (int i = 0; i < pos + 1; ++i)
					nodes[i] = curr.parent.children[i];
				for (int i = pos + 2; i < 5; ++i)
					nodes[i] = curr.parent.children[i-1];
				nodes[pos+1] = node1;
//				node1 = new Node();
//				System.out.println(nodes[3].data[0]);
//				System.out.printf("???  %d  ??? %s %s %s\n", dataNum(curr.parent), curr.parent.data[0],curr.parent.data[1],curr.parent.data[2]);

				curr = curr.parent;


//
			}
////			addLess3(curr,stringUp);
//			System.out.printf("!!!! %s, %s, %s, %s \n", n.data[0], n.parent.children[1].data[0],n.parent.children[2].data[0],n.parent.children[3].data[0]);
//			System.out.printf("???!!!  %d  ??? %s %s %s\n", dataNum(curr.parent), curr.parent.data[0],curr.parent.data[1],curr.parent.data[2]);
		}

		//addLess3(node1,x);
	}


	public static void main(String args[]){

		TwoFourTree tree = new TwoFourTree();
//		System.out.println(tree.size());
		for (int i = 0; i < 7; ++i) {
            tree.add("0" + i);
//            System.out.println("0" + i);
        }
//        String x = null;
//		x = "5";

//		System.out.println(tree.find("046"));
//		System.out.println("046".compareTo("05"));

//		tree.add("30");
//		tree.add("40");
//		tree.add("20");
//		tree.add("10");
//		tree.add("15");
//		tree.add("25");
//////		System.out.println(tree.size);
//////
//		tree.add("18");
//		tree.add("19");
//		tree.add("16");
//		tree.add("1");
//		tree.add("07");
//		tree.add("08");
//		tree.add("w");
//		tree.add("t");
//		tree.add("a");
////		System.out.println(tree.size());
		System.out.println(( tree).levelOrder());

        /*for (int i = 0; i < 3; ++i) {

            tree.remove("0" + i);


        }*/
        tree.remove("01");
        tree.remove("02");


        System.out.println(tree.levelOrder());
//        System.out.println(( tree).levelOrder());
//
//////		System.out.println("07".compareTo("1"));
//		System.out.print(tree.r.data[0]);
//		System.out.printf(" %s ",tree.r.data[1]);
//		System.out.printf(" %s ",tree.r.data[2]);
//		System.out.printf(" %s ","|");
//		System.out.printf(" %s ",tree.r.children[0].data[0]);
//		System.out.printf(" %s ",tree.r.children[0].data[1]);
//		System.out.printf(" %s ",tree.r.children[0].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[1].data[0]);
//		System.out.printf(" %s ",tree.r.children[1].data[1]);
//		System.out.printf(" %s ",tree.r.children[1].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[2].data[0]);
//		System.out.printf(" %s ",tree.r.children[2].data[1]);
//		System.out.printf(" %s ",tree.r.children[2].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[3].data[0]);
//		System.out.printf(" %s ",tree.r.children[3].data[1]);
//		System.out.printf(" %s ",tree.r.children[3].data[2]);
//		System.out.printf(" %s ","|");
//		System.out.printf(" %s ",tree.r.children[0].children[0].data[0]);
//		System.out.printf(" %s ",tree.r.children[0].children[0].data[1]);
//		System.out.printf(" %s ",tree.r.children[0].children[0].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[0].children[1].data[0]);
//		System.out.printf(" %s ",tree.r.children[0].children[1].data[1]);
//		System.out.printf(" %s ",tree.r.children[0].children[1].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[0].children[2].data[0]);
//		System.out.printf(" %s ",tree.r.children[0].children[2].data[1]);
//		System.out.printf(" %s ",tree.r.children[0].children[2].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[0].children[3].data[0]);
//		System.out.printf(" %s ",tree.r.children[0].children[3].data[1]);
//		System.out.printf(" %s ",tree.r.children[0].children[3].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[1].children[0].data[0]);
//		System.out.printf(" %s ",tree.r.children[1].children[0].data[1]);
//		System.out.printf(" %s ",tree.r.children[1].children[0].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[1].children[1].data[0]);
//		System.out.printf(" %s ",tree.r.children[1].children[1].data[1]);
//		System.out.printf(" %s ",tree.r.children[1].children[1].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[1].children[2].data[0]);
//		System.out.printf(" %s ",tree.r.children[1].children[2].data[1]);
//		System.out.printf(" %s ",tree.r.children[1].children[2].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[1].children[3].data[0]);
//		System.out.printf(" %s ",tree.r.children[1].children[3].data[1]);
//		System.out.printf(" %s ",tree.r.children[1].children[3].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[2].children[0].data[0]);
//		System.out.printf(" %s ",tree.r.children[2].children[0].data[1]);
//		System.out.printf(" %s ",tree.r.children[2].children[0].data[2]);
//		System.out.printf(" %s ",":");
//		System.out.printf(" %s ",tree.r.children[2].children[1].data[0]);
//		System.out.printf(" %s ",tree.r.children[2].children[1].data[1]);
//		System.out.printf(" %s ",tree.r.children[2].children[1].data[2]);

	}
}