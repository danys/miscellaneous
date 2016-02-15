template<class K, class V>
class HashTable
{
 public:
 	HashTable(int numberOfBuckets){
 		nBuckets = numberOfBuckets;
 		buckets = new Bucket[nBuckets];
 	}
 	~HashTable(){
 		delete [] buckets;
 	}
 	
 	bool put(K key, V value){
 		int index = computeIndex(key);
 		if (index<=nBuckets-1)
 		{
 			buckets[index].addToBucket(key,value);
 			return true;
 		}
 		else return false;
 	}

 	V* get(K key){
 		int index = computeIndex(key);
 		return buckets[index].search(key);
 	}

 private:
 	class Bucket
 	{
 	 public:
 	 	Bucket(){node = 0;}
 	 	~Bucket(){if (node!=0) delete node;}

 	 	class Node
 	 	{
 	 	 public:
 	 		Node(K k, V v) {key = k; value = v;next=0;}
 	 		~Node() {if (next!=0) delete next;}
 	 		K key;
 	 		V value;
 	 		Node* next;
 	 	};

 	 	Node* node;

 	 	void addToBucket(K key, V value){
 	 		if (node==0) node = new Node(key,value);
 	 		else
 	 		{
 	 			Node* cur = node;
 	 			while(cur->next!=0)	cur = cur->next;
 	 			cur->next = new Node(key,value);
 	 		}
 	 	}

 	 	V* search(K key){
 	 		Node* cur = node;
 	 		while(cur!=0){
 	 			if (cur.key==key) return cur;
 	 			cur = cur->next;
 	 		}
 	 		return 0;
 	 	}

 	};

  private:
  	int nBuckets;
  	Bucket* buckets;

  	int computeIndex(K key){
  		//TODO
  	}
};
