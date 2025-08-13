# Segment Trees: Comprehensive Notes

## Introduction
A **Segment Tree** is a type of **binary tree** data structure that stores information about intervals or segments. Each node in a Segment Tree represents a specific range or interval of an array and holds the result of a query (e.g., sum, maximum, minimum, product) performed on that interval.

## Problem Solved / Use Cases
Segment Trees are primarily used to efficiently solve **Range Query problems** and **Point Update problems** on an array. The core challenge they address is improving the time complexity of these operations from O(N) (for a naive array traversal) to **O(log N)**.

Typical questions where Segment Trees are applicable include:
*   Finding the **sum** of all elements between two given indices (a range).
*   Finding the **maximum** item in a given range.
*   Finding the **minimum** item in a given range.
*   Finding the **average** of elements in a given range.
*   Finding the **product** of all numbers in a given range.

They are a very important topic for interviews and competitive programming.

## Structure of a Segment Tree
*   A Segment Tree is a **binary tree**, meaning every node has a maximum of two children.
*   It is specifically a **full binary tree**, where every node, except the leaf nodes, has two children.
*   Each node in the tree contains two main pieces of information:
    *   **Interval Information:** The start and end indices of the range it represents (e.g., `startInterval`, `endInterval`).
    *   **Operation Result (Data):** The result of the query (e.g., sum) for that specific interval (e.g., `data`).
*   The **root node** of the Segment Tree represents the **entire array** (e.g., indices 0 to N-1).
*   The array is recursively divided into two halves at each step until the **leaf nodes** are reached. A leaf node represents a single element from the original array.

## Operations and Time Complexities

### 1. Tree Construction
*   **Process:** The tree is built recursively by dividing the array into halves.
    *   The base condition for recursion is when the `start` and `end` indices are equal, indicating a leaf node. The `data` for a leaf node is the value of the array element at that index.
    *   For internal nodes, the `data` is calculated by combining the results from its left and right children (e.g., `left.data + right.data` for sum queries).
*   **Time Complexity for Construction:** **O(N)**, where N is the size of the original array. This is because every element and every segment is processed once to build the tree.

### 2. Range Query
*   **Purpose:** To find the result of a query (e.g., sum, max) over a specified `queryStartInterval` and `queryEndInterval`.
*   **Process:** The query operation traverses the tree recursively, starting from the root node. There are three main cases for a node's interval relative to the query interval:
    1.  **Node Interval is Completely Outside Query Interval:** If the node's interval `(node.startInterval, node.endInterval)` does not overlap with the `(queryStartInterval, queryEndInterval)` at all (e.g., `node.startInterval > queryEndInterval` or `node.endInterval < queryStartInterval`), then it returns a default value (e.g., `0` for sum, `Integer.MIN_VALUE` for max) because it contributes nothing to the query.
    2.  **Node Interval is Completely Inside Query Interval:** If the node's interval is fully contained within the query interval (e.g., `queryStartInterval >= node.startInterval && queryEndInterval <= node.endInterval`), then the `node.data` is returned directly, as its entire range is needed for the query.
    3.  **Overlapping (Partial Overlap):** If the node's interval partially overlaps with the query interval, the query is recursively called on **both** the left and right children of the node. The results from the children are then combined (e.g., added together for sum queries) to get the answer for the current node's range.
*   **Time Complexity for Query:** **O(log N)**. This is because, at each level of the tree, the query path typically goes down one or two branches, similar to how a binary search works. The height of a full binary tree with N leaf nodes is log N.

### 3. Point Update
*   **Purpose:** To change the value of an element at a specific `index` in the original array and propagate this change up the tree.
*   **Process:** The update operation also traverses the tree recursively, starting from the root.
    *   It checks if the `index` to be updated lies within the current node's interval. If it does not, the function simply returns without making changes.
    *   If the `index` is within the current node's interval and the current node is a leaf node (i.e., `node.startInterval == node.endInterval`), the `node.data` is updated directly with the `new value`.
    *   If the `index` is within the current node's interval but it's not a leaf node, the update is recursively called on either the left or right child, depending on which child's interval contains the `index`.
    *   After the recursive call returns (meaning a child's data might have been updated), the current node's `data` is recalculated by combining the updated `data` from its children (e.g., `node.left.data + node.right.data`). This process ensures that all affected parent nodes reflect the change.
*   **Time Complexity for Update:** **O(log N)**. This is because the update path follows a single branch down to the leaf node and then updates values back up to the root, both taking logarithmic time relative to the number of elements.

## Space Complexity
*   **Disadvantage:** Segment Trees require **extra space** to store the tree structure.
*   **Memory Usage:** The total number of nodes in a full binary tree with N leaf nodes is approximately **2N - 1** internal nodes and N leaf nodes, resulting in about **2N - 1** total nodes. In practice, it's often approximated as **O(N)** space, as the tree's size is linear with the number of elements in the array. This can be a disadvantage when memory is a critical constraint.

## Advantages and Disadvantages

### Advantages
*   **Efficient Range Queries:** Performs range queries (sum, max, min, etc.) in **O(log N)** time.
*   **Efficient Point Updates:** Updates individual array elements in **O(log N)** time.
*   **Versatile:** Can be adapted for various range operations beyond just sum, like maximum, minimum, product, or average.

### Disadvantages
*   **Extra Space:** Requires **O(N)** additional memory to store the tree, which can be significant for very large arrays.
*   **Construction Time:** Initial construction takes **O(N)** time, though this is typically a one-time cost.
*   **Complexity:** The implementation and understanding can be more complex compared to simpler data structures.

## Some Problems 
1.  [Leetcode Fruits into Basket III - 3479](https://leetcode.com/problems/fruits-into-baskets-iii/description/?envType=daily-question&envId=2025-08-06)