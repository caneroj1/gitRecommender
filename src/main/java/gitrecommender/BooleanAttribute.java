// package gitrecommender.decisionTree;

public class BooleanAttribute extends Attribute {
	public BooleanAttribute(int i) {
		len = 2;
		options = new String[len];

		options[0] = "true";
		options[1] = "false";
		index = i;
		choice = options[i];
		type = "BooleanAttribute";
	}
}