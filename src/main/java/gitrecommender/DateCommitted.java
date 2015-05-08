package gitrecommender;

/**
 * Defines the attribute of when a repository was last committed to on Github.
 */
public class DateCommitted extends Attribute {

	/**
	 * Constructor Assigns the proper index, options, and type that specify this
	 * object as a DateCommitted Attribute.
	 * 
	 * @param i
	 */
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