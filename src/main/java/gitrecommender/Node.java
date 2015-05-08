package gitrecommender;

import java.util.ArrayList;

/**
 * A node class that uses Java generics to describe a node in our DTree<T>. It
 * includes an element and an arraylist of Nodes<T> that are the associated
 * nodes children.
 * 
 * @param <T>
 */
public class Node<T> {

	protected T element;
	protected ArrayList<Node<T>> children;

	/**
	 * Constructor Passes in an element of generic type T, and assigns it to the
	 * element that resides within the node. It also assigns the children to the
	 * nodes ArrayList.
	 * 
	 * @param element
	 */
	public Node(T element) {
		this.element = element;
		children = new ArrayList<Node<T>>();
	}

	/**
	 * @return the element
	 */
	public T getElement() {
		return element;
	}

	/**
	 * @param element
	 *            the element to set
	 */
	public void setElement(T element) {
		this.element = element;
	}

	/**
	 * @return the children
	 */
	public ArrayList<Node<T>> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(ArrayList<Node<T>> children) {
		this.children = children;
	}

	/**
	 * adds a child to Node that calls this method
	 * 
	 * @param child
	 */
	public void addChild(Node child) {
		children.add(child);
	}

	/**
	 * @param index
	 * @return
	 */
	public Node<T> getChild(int index) {
		return children.get(index);
	}

	/**
	 * deletes a child of the Node that calls this method; removes it from the
	 * ArrayList that holds the children.
	 * 
	 * @param index
	 */
	public void deleteChild(int index) {
		children.remove(index);
	}

	/**
	 * Clears all the children from a node.
	 */
	public void clearChildren() {
		children.clear();
	}

	/**
	 * Counts all the children of a given node and returns that integer.
	 * 
	 * @return
	 */
	public int getChildCount() {
		return children.size();
	}

	/*
	 * This merely is the toString method for a Node.
	 */
	public String toString() {
		return "Element: " + element;
	}

}