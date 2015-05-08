// package gitrecommender.decisionTree;

public class Example {

	/* keywordrange, datecommitted, starrange, userlike */
	private KeywordRange keywordRange;
	private DateCommitted dateCommitted;
	private StarRange starRange;
	private LikedRepo likedRepo;
	private boolean showRepo = false;

	/**
	 *
	 * @param keywordRange
	 * @param dateCommitted
	 * @param starRange
	 * @param likedRepo
	 * @param showRepo
	 */
	public Example(KeywordRange keywordRange, DateCommitted dateCommitted,
			StarRange starRange, LikedRepo likedRepo, boolean showRepo) {
		this.keywordRange = keywordRange;
		this.dateCommitted = dateCommitted;
		this.starRange = starRange;
		this.likedRepo = likedRepo;
		this.showRepo = showRepo;
	}

	public String getChoice(Attribute attri) {
		String type = attri.getType();
		if (type.equals("DateCommitted")) {
			return this.dateCommitted.getChoice();
		} else if (type.equals("KeywordRange")) {
			return this.keywordRange.getChoice();
		} else if (type.equals("LikedRepo")) {
			return this.likedRepo.getChoice();
		} else {
			return this.starRange.getChoice();
		}
	}

	/**
	 *
	 * @return keywordRange
	 */
	public KeywordRange getKeywordRange() {
		return keywordRange;
	}

	/**
	 *
	 * @return dateCommitted
	 */
	public DateCommitted getDateCommitted() {
		return dateCommitted;
	}

	/**
	 *
	 * @return starRange
	 */
	public StarRange getStarRange() {
		return starRange;
	}

	/**
	 *
	 * @return starRange
	 */
	public LikedRepo getLikedRepo() {
		return likedRepo;
	}

	/**
	 * @param keywordRange
	 *            the keywordRange to set
	 */
	public void setKeywordRange(KeywordRange keywordRange) {
		this.keywordRange = keywordRange;
	}

	/**
	 * @param starRange
	 *            the starRange to set
	 */
	public void setStarRange(StarRange starRange) {
		this.starRange = starRange;
	}

	/**
	 * @param likedRepo
	 *            the likedRepo to set
	 */
	public void setLikedRepo(LikedRepo likedRepo) {
		this.likedRepo = likedRepo;
	}

	/**
	 *
	 * @param dateCommitted
	 */
	public void setDateCommitted(DateCommitted dateCommitted) {
		this.dateCommitted = dateCommitted;
	}

	/**
	 * @return the showRepo
	 */
	public boolean isShowRepo() {
		return showRepo;
	}

	/**
	 * @param showRepo
	 *            the showRepo to set
	 */
	public void setShowRepo(boolean showRepo) {
		this.showRepo = showRepo;
	}

	public String toString() {
		String str = "keywordRange : " + keywordRange.toString();
		str += "\ndateCommitted: " + dateCommitted.toString();
		str += "\nstarRange: " + starRange.toString();
		str += "\nlikedRepo: " + likedRepo.toString();
		str += "\nshow repo: " + showRepo;
		return str;
	}

}
