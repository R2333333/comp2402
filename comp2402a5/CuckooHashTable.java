package comp2402a5;


/**
 * This class implements the cuckoo hash 
 * 
 * See: Rasmus Pagh, Flemming Friche Rodler, Cuckoo Hashing, Algorithms - ESA 2001, 
 * Lecture Notes in Computer Science 2161, Springer 2001, ISBN 3-540-42493-8
 *
 * @param <T>
 */
public abstract class CuckooHashTable<T> extends OpenAddressHashTable<T> {
	
	/* add any attributes you may need here */
	int z[];
	int zPosition;
	boolean dNoChanged = true, shrinked = false;

	MultiplicativeHashFunction h1;
	MultiplicativeHashFunction h2;

	
	/**
	 * Create a new empty hash table
	 * @param clazz is the class of the data to be stored in the hash table
	 * @param zz is an array of integer values to be used for the has functions
	 */
	public CuckooHashTable(Class<T> clazz, int[] zz) {

		super(clazz);
		f = new Factory<>(clazz);
		z = zz;
		t = f.newArray(16);
		d = 4;
		h1 = new MultiplicativeHashFunction(zz[0],w,d);
		h2 = new MultiplicativeHashFunction(zz[1],w,d);
		zPosition = 2;
	}
	
	/* define all abstract methods inherited from parent class here */
	@Override
	public boolean add(T x){
		int count = 0;
		T tmp, tmp2;

		if(find(x) != null) return false;
		if((n+1)*2 > t.length) resize();
		if(t[h1.hash(x)] == null) {
			t[h1.hash(x)] = x;
		}
		else {
			tmp = t[h1.hash(x)];
			t[h1.hash(x)] = x;
			tmp2 = x;

			while (true){
				if(tmp == null) {
					break;
				}
				if(t[h2.hash(tmp)] == null){
					t[h2.hash(tmp)] = tmp;
					break;
				}
				if(!t[h2.hash(tmp)].equals(tmp2)) {
					tmp2 = t[h2.hash(tmp)];
					t[h2.hash(tmp)] = tmp;
					tmp = tmp2;
				}else {
					tmp2 = t[h1.hash(tmp)];
					t[h1.hash(tmp)] = tmp;
					tmp = tmp2;
				}
				++count;
				if(count >= t.length){
					resize();
					count = 0;
				}
			}
		}
		++n;
		return true;
	}

	@Override
	protected void resize(){
		T[] n1;
		if((n*8 < t.length && d >= 5) || shrinked) {
			n1 = f.newArray(t.length/2);
			if(dNoChanged) {
				--d;
				dNoChanged = false;
			}
			shrinked = true;
		}else if((n+1)*2 > t.length){
			n1 = f.newArray( 2*t.length);
			if(dNoChanged) {
				++d;
				dNoChanged = false;
			}
		}else n1 = f.newArray(t.length);

		if(zPosition+1 > z.length-1)
			zPosition = 0;

		h1 = new MultiplicativeHashFunction(z[zPosition],w,d);
		h2 = new MultiplicativeHashFunction(z[zPosition+1],w,d);
		zPosition += 2;
		/*if(n1.length == t.length/2){
			T[] copy = f.newArray(t.length);
			for(int i =0; i<t.length; ++i){
				if(t[i] != null)
					copy[i] = t[i];
			}

			this.clear();
			for(T tt: copy){
				if(tt != null)
					this.add(tt);
			}
			return;
		}*/
		for(T tt: t) {
			int count = 0;
			T tmp, tmp2;
			if(tt == null)
				continue;

			if (n1[h1.hash(tt)] == null) {
				n1[h1.hash(tt)] = tt;
				dNoChanged = true;
				shrinked = false;
			} else {
				tmp = n1[h1.hash(tt)];
				n1[h1.hash(tt)] = tt;
				tmp2 = tt;

				while (true) {
					if (tmp == null) {
						break;
					}
					if (n1[h2.hash(tmp)] == null) {
						n1[h2.hash(tmp)] = tmp;
						break;
					}
					if (!n1[h2.hash(tmp)].equals(tmp2)) {
						tmp2 = n1[h2.hash(tmp)];
						n1[h2.hash(tmp)] = tmp;
						tmp = tmp2;
					}else {
					tmp2 = n1[h1.hash(tmp)];
					n1[h1.hash(tmp)] = tmp;
					tmp = tmp2;
					}
					++count;
					if (count >= n1.length) {
						resize();
						break;
					}
				}
				dNoChanged = true;
				shrinked = false;
			}

		}
		t = n1;
	}

	@Override
	public T find(Object x){
		if(h1.hash(x) < t.length) {
			if (t[h1.hash(x)] != null) {
				if (t[h1.hash(x)].equals(x))
					return t[h1.hash(x)];
			}
		}else if(h2.hash(x) < t.length){
			if (t[h2.hash(x)] != null) {
				if (t[h2.hash(x)].equals(x))
					return t[h2.hash(x)];
			}
		}

		return null;
	}

	@Override
	public T remove(T x){
		if(find(x) != null) {
			T findT;
			if (t[h1.hash(x)] != null) {
				if (t[h1.hash(x)].equals(x)) {
					findT = t[h1.hash(x)];
					t[h1.hash(x)] = null;
					--n;
					if((n*8)<t.length && d >= 5)
						resize();
					return findT;
				}
			} else if (t[h2.hash(x)] != null) {
				if (t[h2.hash(x)].equals(x)) {
					findT = t[h2.hash(x)];
					t[h2.hash(x)] = null;
					--n;
					if((n*8)<t.length && d >= 5)
						resize();
					return findT;
				}
			}
		}

		return null;
	}
	
}
