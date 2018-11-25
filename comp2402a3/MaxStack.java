package comp2402a3;



import java.util.*;


/**
 * An implementation of a Stack that also can efficiently return  
 * the maximum element in the current Stack.  Every operation -
 * push, pop, peek, and max - must run in constant time
 *
 * @param <T> the class of objects stored in the queue
 */
public class MaxStack<T extends Comparable<T>> extends SLListStack<T> {

	// Add any structures you need here...
	//TreeMap<T,Integer> max = new TreeMap<>();
	SLListStack<T> mm = new SLListStack();
	int count = 0;
	List<Integer> list = new LinkedList<>();
	// ...override whatever methods you need here...
	@Override
	public T push(T x) {
		//if(this.size()==0)
		//	max = x;
		Node u = new Node();
		u.x = x;
		u.next = head;
		head = u;
		if (n == 0)
			tail = u;
		n++;
		if(mm.size()==0 || x.compareTo(mm.peek())>0) {
			mm.push(x);
			list.add(0,1);
		}
		else list.set(0,list.get(0)+1);
		//if(x.compareTo(max)>0)
		//	max = x;
		/*if(mm.containsKey(x))
			mm.put(x,mm.get(x)+1);
		else mm.put(x,1);
		*/
		return x;
	}
	@Override
	public T pop() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		//if(!this.contains(x))
		if(list.get(0)==1) {
			mm.pop();
			list.remove(0);
		}else {
			list.set(0,list.get(0)-1);
		}

		//if(mm.get(x)==1) mm.remove(x);
		//else mm.put(x,mm.get(x)-1);
		return x;
	}

	public T max(){
		if(n==0) return null;
		else {
		//	mm.addAll(this);
		//	System.out.println(list);
			return mm.peek();
		}

	}

	public static void main(String[] args){
		
		MaxStack<Character> stack = new MaxStack<Character>();
		
		String datasequence = "ABXRTSXY";
		for (int i = 0; i < datasequence.length(); i++){
			stack.push(datasequence.charAt(i));
			System.out.println(stack + ", max = " + stack.max());
		}
		while(stack.size() > 0) {
			stack.pop();
			System.out.println(stack + ", max = " + stack.max());
		}
		
		
	}

}
