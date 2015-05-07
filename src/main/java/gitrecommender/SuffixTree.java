package gitrecommender;

// An implementation of a suffix tree in order to facilitate fast lookups of words from repository readmes.
public class SuffixTree {
	SuffixTreeNode rootNode;

	public SuffixTree() {
		rootNode = new SuffixTreeNode();
	}

	public void addWord(String word) {
		SuffixTreeNode traversalNode = rootNode;
		int position;

		for (char character : word.toUpperCase().toCharArray()) {
			position = (int) character;
			if (!traversalNode.isCharSet(position)) {
				traversalNode.setCharAt(position);
			}

			traversalNode = traversalNode.getNextNode(position);
		}
	}

	public boolean findWord(String word) {
		boolean wordResult = true;
		SuffixTreeNode traversalNode = rootNode;
		int position;

		for (char character : word.toUpperCase().toCharArray()) {
			position = (int) character;

			if (traversalNode.isCharSet(position)) {
				traversalNode = traversalNode.getNextNode(position);
			} else {
				return false;
			}
		}

		return wordResult;
	}

	public void stringify() {
		rootNode.stringify();
	}
}
