// package gitrecommender.decisionTree;

public class DateCommitted extends Attribute {
	public DateCommitted(int i) {
		len = 4;
		index = i;
		options = new String[len];
		options[0] = "weekorless";
		options[1] = "monthorless";
		options[2] = "yearorless";
		options[3] = "morethanyear";

		choice = options[i];
		type = "DateCommitted";
	}
}
