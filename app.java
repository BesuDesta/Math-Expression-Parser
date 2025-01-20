import java.util.*;

/**
 *Besu Desta
 *9/26/2024
 *CMSC 256-001
 *This project takes in a math expression in the form of a string and evaluates it using a binary tree
 */

public class app {
    // Inner BinaryNode class
    public static class BinaryNode<E> {
        private E data;
        private BinaryNode<E> leftChild;
        private BinaryNode<E> rightChild;

        public BinaryNode(E data) {
            this(data, null, null);
        }

        public BinaryNode(E data, BinaryNode<E> leftChild, BinaryNode<E> rightChild) {
            this.data = data;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public E getData() { return data; }
        public BinaryNode<E> getLeft() { return leftChild; }
        public BinaryNode<E> getRight() { return rightChild; }

        public void setData(E data) { this.data = data; }
        public void setLeftChild(BinaryNode<E> left) { this.leftChild = left; }
        public void setRightChild(BinaryNode<E> right) { this.rightChild = right; }
    }

    public static BinaryNode<String> buildParseTree(String expression) {
        String[] tokens = expression.split("\\s+");
        Stack<BinaryNode<String>> nodeStack = new Stack<>();
        BinaryNode<String> currentNode = new BinaryNode<>(null);
        BinaryNode<String> root = currentNode;

        for (String token : tokens) {
            switch (token) {
                case "(":
                    // Add new node as left child and descend
                    currentNode.setLeftChild(new BinaryNode<>(null));
                    nodeStack.push(currentNode);
                    currentNode = currentNode.getLeft();
                    break;

                case "+": case "-": case "/": case "*":
                    // Set current node's data to operator
                    currentNode.setData(token);
                    // Create and descend to right child
                    currentNode.setRightChild(new BinaryNode<>(null));
                    nodeStack.push(currentNode);
                    currentNode = currentNode.getRight();
                    break;

                case ")":
                    // Return to parent
                    if (!nodeStack.isEmpty()) {
                        currentNode = nodeStack.pop();
                    }
                    break;

                default:
                    // Must be a number - set node's data and return to parent
                    currentNode.setData(token);
                    if (!nodeStack.isEmpty()) {
                        currentNode = nodeStack.pop();
                    }
                    break;
            }
        }

        return root;
    }

    public static double evaluate(BinaryNode<String> tree) {
        if (tree == null) {
            return 0;
        }

        // Leaf node (operand)
        if (tree.getLeft() == null && tree.getRight() == null) {
            return Double.parseDouble(tree.getData().toString());
        }

        // Evaluate left and right subtrees
        double leftVal = evaluate(tree.getLeft());
        double rightVal = evaluate(tree.getRight());

        // Apply operator
        switch (tree.getData().toString()) {
            case "+": return leftVal + rightVal;
            case "-": return leftVal - rightVal;
            case "*": return leftVal * rightVal;
            case "/": return leftVal / rightVal;
            default: return 0;
        }
    }

    public static String getInfixExpression(BinaryNode<String> tree) {
        if (tree == null) {
            return "";
        }

        // Leaf node (operand)
        if (tree.getLeft() == null && tree.getRight() == null) {
            return tree.getData().toString();
        }

        // Build expression recursively with parentheses
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        sb.append(getInfixExpression(tree.getLeft()));
        sb.append(" ").append(tree.getData().toString()).append(" ");
        sb.append(getInfixExpression(tree.getRight()));
        sb.append(" )");

        return sb.toString();
    }
}
