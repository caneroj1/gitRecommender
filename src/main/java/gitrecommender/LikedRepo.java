package gitrecommender;

/**
 * One of the attributes that we are defining is whether the repository is liked
 * by the user. The user being the user that being used for the training data.
 * This class assumes that the repository is not liked by the user originally.
 */
public class LikedRepo extends Attribute {
	private boolean likedRepo = false;

	/**
	 * Constructor
	 * 
	 * @param i
	 *            is the index that will be assigned to this LikedRepo object.
	 *            This attribute essentially as two options values: true or
	 *            false. It also assigns the type of attribute as "LikedRepo"
	 */
	public LikedRepo(int i) {
		len = 2;
		options = new String[len];
		options[0] = "False";
		options[1] = "True";

		index = i;
		if (i == 0)
			likedRepo = false;
		else
			likedRepo = true;
		type = "LikedRepo";
		choice = options[i];
	}

	/*
	 * Assigns the value index for this object based on whether likeRepo is true
	 * or false. It assigns 1 for true, or 0 for false. it then returns the
	 * index.
	 */
	@Override
	public int getIndex() {
		if (likedRepo) {
			index = 1;
		} else {
			index = 0;
		}
		return index;
	}

}
