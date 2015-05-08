package gitrecommender;

public class StarRange extends Attribute {

	/**
	 * @param i
	 */
	public StarRange(int i) {
		len = 4;
		index = i;
		options = new String[len];
		options[0] = "zerofifty";
		options[1] = "fiftyonehundred";
		options[2] = "onehundredfivehundred";
		options[3] = "onethousandormore";

		choice = options[i];
		type = "StarRange";
	}

}
