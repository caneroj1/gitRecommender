// package gitrecommender.decisionTree;

public class KeywordRange extends Attribute {

	public KeywordRange(int i) {
		len = 4;
		options = new String[len];

		options[0] = "zerothirty";
		options[1] = "thirtyfifty";
		options[2] = "fiftyeighty";
		options[3] = "eightyonehundred";

		index = i;
		choice = options[i];
		type = "KeywordRange";
	}
}
