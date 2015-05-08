package gitrecommender;

import java.util.Arrays;

/**
 * The parent class of the attributes of KeywordRange, DateCommited, StarRange
 * and LikedRepo. It provides information that is applicable to all *
 * attributes, and can be extended to future attributes.
 */
public class Attribute {

	/**
	 * The parent class of the attributes of KeywordRange, DateCommited,
	 * StarRange and LikedRepo
	 */
	protected int index;
	protected String[] options;
	protected int len;
	protected String type;
	protected String choice;

	/**
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return
	 */
	public int getLen() {
		return len;
	}

	/**
	 * @return
	 */
	public String[] getOptions() {
		return options;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getChoice() {
		return choice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Attribute [index=" + index + ", options="
				+ Arrays.toString(options) + ", len=" + len + ", type=" + type
				+ ", choice=" + choice + "]";
	}

}