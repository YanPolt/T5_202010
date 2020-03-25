package model.data_structures;

public class ListaEncadenada <K extends Comparable<K>,V> implements ISymbolTable<K,V> 
{

	private int n;
	
	private Node first;
	
	private class Node
	{ // linked-list node
		private K key;
		private V val;
		private Node next;
		
		public Node(K key, V val, Node next)
		{
			this.key = key;
			this.val = val;
			this.next = next;
		}
	}
	
	public ListaEncadenada(){		
	}

	public int size()
	{
		return n;
	}
	
	public boolean isEmpty()
	{
		return size()==0;
	}
	
	 public boolean contains(K key) {
	        return get(key) != null;
	    } 

	
	public V get(K k)
	{
		for(Node x=first; x!=null; x=x.next)
		{
			if(k.equals(x.key))
			{
				return x.val;
			}
		}
		return null;
	}
	
	public void put(K k,V v)
	{
		
		for(Node x=first; x!=null; x=x.next)
		{
			if(k.equals(x.key))
			{
				x.val=v;
				return;
			}
		}
		first= new Node(k,v,first);
		n++;
	}
	
	  public V delete(K key) {
	        first = delete(first, key);
	        return delete(first,key).val;
	    }

	    // delete key in linked list beginning at Node x
	    private Node delete(Node x, K key) {
	    	Node retorno= new Node(null,null,null);
	        if (x == null) return null;
	        if (key.equals(x.key)) {
	            n--;
	            retorno=x;
	            x= x.next;
	            
	        }
	        x.next = delete(x.next, key);
	        return retorno;
	    }
	    
	    public Iterable<K> keys()  {
	        Cola<K> queue = new Cola<K>();
	        for (Node x = first; x != null; x = x.next)
	            queue.enqueue(x.key);
	        return queue;
	    }


}
