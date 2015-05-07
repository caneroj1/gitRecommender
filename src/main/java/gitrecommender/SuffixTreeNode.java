package gitrecommender;

// Nodes for the suffix tree.
// Each node essentially stores more suffix tree nodes in an array that is indexed by the ascii decimal value of a character.
public class SuffixTreeNode {
	private SuffixTreeNode[] treeNodes;

	public SuffixTreeNode() {
		treeNodes = new SuffixTreeNode[256];
	}

	public void setCharAt(int position) {
		treeNodes[position] = new SuffixTreeNode();
	}

	public boolean isCharSet(int position) {
		return treeNodes[position] != null;
	}

	public SuffixTreeNode getNextNode(int position) {
		return treeNodes[position];
	}

	public void stringify() {
		for (int i = 0; i < 256; i++) {
			System.out.println("Char # " + i + " " + treeNodes[i]);
		}
	}
}
