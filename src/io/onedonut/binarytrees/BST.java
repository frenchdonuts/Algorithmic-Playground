package io.onedonut.binarytrees;

import java.util.NoSuchElementException;

/**
 * Created by pamelactan on 3/23/17.
 */
public class BST {
    BST left;
    BST right;
    BST parent;
    int key;

    public BST(int key, BST left, BST right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    public BST max(BST root) {
        if (root == null)
            throw new NoSuchElementException("An empty tree has no max.");

        while (root.right != null)
            root = root.right;

        return root;
    }

    public void delete(BST nodeToDelete) {
        if (nodeToDelete == null)
            return;

        if (nodeToDelete.left == null && nodeToDelete.right == null) {
            // nodeToDelete has 0 children
            replaceNodeToDeleteWith(null, nodeToDelete, nodeToDelete.parent);

        } else if (nodeToDelete.left != null && nodeToDelete.right == null) {
            // nodeToDelete has 1 child ---------
            replaceNodeToDeleteWith(nodeToDelete.left, nodeToDelete, nodeToDelete.parent);

        } else if (nodeToDelete.left == null && nodeToDelete.right != null) {
            replaceNodeToDeleteWith(nodeToDelete.right, nodeToDelete, nodeToDelete.parent);
            // ----------------------------------

        } else {
            // nodeToDelete has 2 children
            BST predecessor = max(nodeToDelete.left);

            BST tempLeft = nodeToDelete.left;
            BST tempRight = nodeToDelete.right;

            nodeToDelete.left = predecessor.left;
            nodeToDelete.right = predecessor.right;

            predecessor.left = tempLeft;
            predecessor.right = tempRight;

            // Now nodeToDelete either has 0 or 1 children
            delete(nodeToDelete);
        }

    }

    private void replaceNodeToDeleteWith(BST newChild, BST nodeToDelete, BST parent) {
        if (parent.left == nodeToDelete)
            parent.left = newChild;
        else if (parent.right == nodeToDelete)
            parent.right = newChild;
    }

    public BST predecessor(BST root) {
        if (root == null)
            throw new NoSuchElementException("An empty tree cannot have a predecessor.");

        if (root.left != null)
            return max(left);

        BST currentNode = root;
        while (currentNode.parent != null) {
            currentNode = currentNode.parent;
            if (currentNode.key < root.key)
                return currentNode;
        }

        return null;
    }
}
