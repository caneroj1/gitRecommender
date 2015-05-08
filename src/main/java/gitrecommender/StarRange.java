package gitrecommender;

/**
 * This is another attribute that the recommender uses, called StarRange. The
 * options that it represents are count ranges for the overall Stars that belong
 * to a repo.
 */
public class StarRange extends Attribute {

	/**
	 * Constructor Assigns the proper index that specifies this object as a
	 * StarRange. The values for the options that a StarRange could possess are
	 * strings that are stored in an array that differentiate between boundaries
	 * star counts. It also assigns it the appropriate type.
	 * 
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
