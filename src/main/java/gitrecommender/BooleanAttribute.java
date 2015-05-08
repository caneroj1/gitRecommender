package gitrecommender;

/**
 * Defines a Boolean attribute. This attribute can have an option of true or
 * false assigns it the proper index, and the type.
 */
public class BooleanAttribute extends Attribute {

	/**
	 * Constructor generates a new BooleanAttribute based on input.
	 * 
	 * @param i
	 */
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