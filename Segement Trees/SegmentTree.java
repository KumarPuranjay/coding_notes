class SegmentTree {

    public static void main(String[] args) {
        // Sample array to demonstrate segment tree operations
        int[] arr = { 2, 8, 6, 7, -2, -8, 4, 9 };
        SegmentTree segTree = new SegmentTree(arr);
        
        // Example usage (uncomment to test):
        System.out.println("Sum from index 1 to 3: " + segTree.query(1, 3));
        segTree.update(2, 10);
        System.out.println("Sum after updating index 2 to 10: " + segTree.query(1, 3));
    }

    /**
     * Node class represents each node in the segment tree
     * Each node stores:
     * - data: the sum of elements in its range
     * - startInterval & endInterval: the range this node covers
     * - left & right: child nodes
     */
    private class Node {
        int data;           // Sum of elements in this node's range
        int startInterval;  // Start index of the range this node covers
        int endInterval;    // End index of the range this node covers
        Node left;          // Left child node
        Node right;         // Right child node

        // Constructor for internal nodes (without data initially)
        public Node(int startInterval, int endInterval) {
            this.startInterval = startInterval;
            this.endInterval = endInterval;
        }

        // Constructor for leaf nodes or nodes with calculated data
        public Node(int data, int startInterval, int endInterval) {
            this.data = data;
            this.startInterval = startInterval;
            this.endInterval = endInterval;
        }
    }

    Node root; // Root of the segment tree

    /**
     * Constructor: builds the segment tree from the given array
     * @param arr - input array to build segment tree from
     */
    public SegmentTree(int[] arr) {
        // Build the tree starting from index 0 to arr.length-1
        this.root = constructTree(arr, 0, arr.length - 1);
    }

    /**
     * Recursively constructs the segment tree
     * @param arr - source array
     * @param start - start index of current segment
     * @param end - end index of current segment
     * @return root node of the constructed subtree
     */
    private Node constructTree(int[] arr, int start, int end) {
        // Base case: leaf node (single element)
        if (start == end) {
            return new Node(arr[start], start, end);
        }

        // Calculate midpoint to divide the range
        int mid = start + (end - start) / 2;

        // Recursively build left subtree (covers start to mid)
        Node left = constructTree(arr, start, mid);
        // Recursively build right subtree (covers mid+1 to end)
        Node right = constructTree(arr, mid + 1, end);

        // Create current node with sum of both children
        Node currNode = new Node(left.data + right.data, start, end);
        currNode.left = left;
        currNode.right = right;

        return currNode;
    }

    /**
     * Public method to query sum in a given range
     * @param queryStartInterval - start index of query range (inclusive)
     * @param queryEndInterval - end index of query range (inclusive)
     * @return sum of elements in the specified range
     */
    public int query(int queryStartInterval, int queryEndInterval) {
        return this.query(root, queryStartInterval, queryEndInterval);
    }

    /**
     * Private recursive method to perform range sum query
     * @param node - current node being examined
     * @param queryStartInterval - start of query range
     * @param queryEndInterval - end of query range
     * @return sum of elements in the query range within this node's range
     */
    private int query(Node node, int queryStartInterval, int queryEndInterval) {
        // Case 1: No overlap - node's range is completely outside query range
        if (node.startInterval > queryEndInterval || node.endInterval < queryStartInterval) {
            return 0; // No contribution to sum
        } 
        // Case 2: Complete overlap - query range completely contains node's range
        else if (queryStartInterval <= node.startInterval && queryEndInterval >= node.endInterval) {
            return node.data; // Return entire sum stored in this node
        } 
        // Case 3: Partial overlap - need to check both children
        else {
            return query(node.left, queryStartInterval, queryEndInterval)
                    + query(node.right, queryStartInterval, queryEndInterval);
        }
    }

    /**
     * Public method to update value at a specific index
     * @param index - index to update
     * @param val - new value to set at the index
     */
    public void update(int index, int val) {
        update(root, index, val);
    }

    /**
     * Private recursive method to update a value and propagate changes up the tree
     * @param node - current node being examined
     * @param index - index to update
     * @param val - new value
     */
    private void update(Node node, int index, int val) {
        // If index is outside current node's range, no update needed
        if (index < node.startInterval || index > node.endInterval) {
            return;
        }

        // Base case: reached the leaf node containing the index
        if (node.startInterval == node.endInterval) {
            // Update the leaf node's value
            if (node.startInterval == index)
                node.data = val;
            return;
        }

        // Determine which child contains the index
        int mid = node.startInterval + (node.endInterval - node.startInterval) / 2;
        if (index <= mid) {
            // Index is in left subtree
            update(node.left, index, val);
        } else {
            // Index is in right subtree
            update(node.right, index, val);
        }

        // After updating child, recalculate current node's sum
        // This propagates the change up the tree
        node.data = node.left.data + node.right.data;
    }
}
