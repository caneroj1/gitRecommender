package gitrecommender;

/**
 * This is another attribute that the recommender uses, called KeywordRange. The
 * options that it represents are percentage ranges for the overall keyword
 * score according to the calculations done by the nearest neighbor.
 */
public class KeywordRange extends Attribute {

	/**
	 * @param i
	 *            is the index that is assigned to this keyword range
	 */
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
