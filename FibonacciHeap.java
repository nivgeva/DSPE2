import java.util.ArrayList;
import java.util.List;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public HeapNode minNode;
    public HeapNode first = null;
    public HeapNode last = null;
    public int size;
    public int totalMarked;
    public static int numOfLinks;
    public static int numOfCuts;

    public FibonacciHeap() {} //Empty constructor
    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     *
     */
    public boolean isEmpty(){
        return this.size == 0;
    }

    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     *
     * Returns the newly created node.
     */
    public HeapNode insert(int key){
        HeapNode newNode = new HeapNode(key);
        if(this.isEmpty()){
        	this.minNode = newNode;
        	this.first = newNode;
        	this.last = newNode;
        }
        
        else{
        	newNode.setLeftBro(this.last);
        	this.last.setRightBro(newNode);
        	newNode.setRightBro(this.first);
        	this.first.setLeftBro(newNode);
        	this.updateMin();
        }
        this.size += 1;
        return newNode;
    }

    public void updateMin(){
        HeapNode currMinNode = this.minNode;
        HeapNode node = this.first;
        if (this.first != this.last) {
            if(this.first.getKey() < currMinNode.getKey()){
                this.minNode = this.first;
            }
            node = node.getRightBro();
            while(node != this.first){
                if(node.getKey() < currMinNode.getKey()){
                    this.minNode = node;
                    node = node.getRightBro();
                }
            }
        }
    }

    /**
     * public void deleteMin()
     *
     * Deletes the node containing the minimum key.
     *
     */
    public void deleteMin(){
    	if(!isEmpty()) {
    		replaceRootWithChildrens(minNode);
    	
    		return; // should be replaced by student code
    	}
    }
    
    private void replaceRootWithChildrens(HeapNode root) {
    	HeapNode prevRoot = root.getLeftBro();
    	HeapNode NextRoot = root.getRightBro();
    	HeapNode mostLeftChild = root.childrens.get(0);
    	HeapNode mostRightChild = root.childrens.get(root.childrens.size()-1);
    	prevRoot.setRightBro(mostLeftChild);
    	mostLeftChild.setLeftBro(prevRoot);
    	NextRoot.setLeftBro(mostRightChild);
    	mostRightChild.setRightBro(NextRoot);
    	for(HeapNode child : root.childrens) {
    		child.setParent(null);
    		child.mark = false;
    	}
    }

    private void consolidate() {
    	HeapNode[] degArray = new HeapNode[size];
    	for(int i = 0; i < size; i++) {
    		degArray[i] = null;
    	}
    	
    }
    
    /**
     * public HeapNode findMin()
     *
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     *
     */
    public HeapNode findMin(){
        return this.minNode;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    public void meld (FibonacciHeap heap2){
        HeapNode tmpLast = heapNodeDeepCopy(last);
        this.last.setRightBro(tmpLast);
        this.last = heap2.last;
        heap2.first.setLeftBro(tmpLast);
        heap2.last.setRightBro(first);
        this.updateMin();
        this.size += heap2.size;
    }

    public HeapNode heapNodeDeepCopy(HeapNode node){
        HeapNode newNode = new HeapNode(node.getKey());
        newNode.setLeftBro(node.leftBro);
        newNode.setRightBro(node.rightBro);
        newNode.setParent(node.getParent());
        newNode.setChildrens(node.getChildrens());
        return newNode;
    }

    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *
     */
    public int size()
    {
        return this.size;
    }

    /**
     * public int[] countersRep()
     *
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * (Note: The size of of the array depends on the maximum order of a tree.)
     *
     */
    public int[] countersRep()
    {
        int[] arr = new int[(int) Math.ceil(Math.log(this.size)/Math.log(2))];
        HeapNode node = this.first;
        int k = first.getRank();
        arr[k] += 1;
        node = node.getRightBro();
        while(node != this.first){
            k = node.getRank();
            arr[k] += 1;
            node = node.getRightBro();
        }
        return arr;
    }

    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     *
     */
    public void delete(HeapNode x)
    {
        x.setKey((int) Double.NEGATIVE_INFINITY);
        this.updateMin();
        this.deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta)
    {
        return; // should be replaced by student code
    }

    /**
     * public int nonMarked()
     *
     * This function returns the current number of non-marked items in the heap
     */
    public int nonMarked(){
        return this.size - this.totalMarked;
    }

    /**
     * public int potential()
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     *
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     */
    public int potential(){
        return this.size + 2*this.totalMarked;
    }

    /**
     * public static int totalLinks()
     *
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     */
    
    
    public static int totalLinks(){
        return numOfLinks;
    }

    /**
     * public static int totalCuts()
     *
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts(){
        return numOfCuts;
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     *
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     *
     * ###CRITICAL### : you are NOT allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k){
        int[] arr = new int[100];
        return arr;
    }
    

    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     *
     */
    public class HeapNode{
        public int key;
        public HeapNode parent = null;
        public HeapNode rightBro= null;
        public HeapNode leftBro= null;
        public List<HeapNode> childrens= new ArrayList<HeapNode>();
        public boolean mark;
        public HeapNode(int key){
            this.key = key;
            this.leftBro = this;
            this.rightBro = this;
        }
        public int getKey(){
            return this.key;
        }
        public HeapNode getParent(){
            return this.parent;
        }
        public HeapNode getLeftBro(){
            return this.leftBro;
        }
        public HeapNode getRightBro(){
            return this.rightBro;
        }
        public List<HeapNode> getChildrens(){
            return this.childrens;
        }
        public HeapNode getChildrenK(int k){
            return this.childrens.get(k);
        }
        public int getRank() {return this.childrens.size();}
        public boolean getMark(){return this.mark;}
        public void setParent(HeapNode parentNode) {this.parent = parentNode;}
        public void setLeftBro(HeapNode newNode) {this.leftBro = newNode;}
        public void setRightBro(HeapNode newNode) {this.rightBro = newNode;}
        public void setChildrens(List<HeapNode> newChildrenList) {this.childrens = newChildrenList;}
        public void setChildrenK(HeapNode newChild, int k) {this.childrens.set(k, newChild);}
        public void setKey(int k){this.key = k;}
        public void setMark(boolean sign){this.mark = sign;}
    }

}
    