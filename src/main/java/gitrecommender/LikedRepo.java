// package gitrecommender.decisionTree;

public class LikedRepo extends Attribute {
	private boolean likedRepo = false;

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
