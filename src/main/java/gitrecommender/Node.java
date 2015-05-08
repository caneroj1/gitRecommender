// package gitrecommender.decisionTree;

import java.util.ArrayList;

public class Node<T> {

	protected T element;
	protected ArrayList<Node<T>> children;

	/**
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

	public void addChild(Node child) {
		children.add(child);
	}

	public Node<T> getChild(int index) {
		return children.get(index);
	}

	public void deleteChild(int index) {
		children.remove(index);
	}

	public void clearChildren() {
		children.clear();
	}

	public int getChildCount() {
		return children.size();
	}

	public String toString() {
		return "Element: " + element;
	}
}
