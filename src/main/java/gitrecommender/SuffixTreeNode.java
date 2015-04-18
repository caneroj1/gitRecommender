package gitrecommender;

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
	    for(int i = 0; i < 256; i++) {
	      System.out.println("Char # " + i + " " + treeNodes[i]);
	    }
	  }
	}
