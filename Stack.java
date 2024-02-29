package Project.mazesolverproject;

public class Stack<T> {
    private Node<T> top;
    
    public Stack() {
        top = null;
    }
    
    public boolean isEmpty() {
        if (top==null)
            return true;
        return false;
    }
    
    public void push(T n) {
        Node<T> newNode = new Node<>(n);
        newNode.next = top;
        top = newNode;
    }
    
    public T pop() {
        Node<T> t = top;
        if(!isEmpty())
            top = top.next;
        return t.data;
    }

    // Added class -- Converts Stack to String
    @Override
    public String toString() {
        String str = "";
        Node<T> temp = top;
        while (temp != null) {
            if (temp == top) str += "{" + temp.data + " (Top), ";
            else if (temp.next == null) str += temp.data + "}";
            else str += temp.data + ", ";
            temp = temp.next;
        }
        return str;
    }

    private static class Node<T>{
        Node<T> next = null;
        T data;

        Node(T data){
            this.data = data;
        }
    }
}