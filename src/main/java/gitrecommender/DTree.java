package gitrecommender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.Vector;

/**
 * This is our decision tree class that defines the algorithm required to
 * implement our recursive decision tree learning algorithm. The tree is built
 * upon a set of examples that acts as our training data.
 * 
 * @param <T>
 */
public class DTree<T> {

	protected Node<T> parent;

	/**
	 * The constructors are able to be passed either a DTree or a Node in order
	 * to instantiate the DTree.
	 * 
	 * @param dTree
	 */
	public DTree(DTree<T> dTree) {
		this.parent = dTree.getParent();
	}

	/**
	 * The constructors are able to be passed either a DTree or a Node in order
	 * to instantiate the DTree.
	 * 
	 * @param element
	 */
	public DTree(Node<T> element) {
		this.parent = element;
	}

	/**
	 * @return the parent
	 */
	public Node<T> getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	/**
	 * @return
	 */
	public ArrayList<Node<T>> getChildren() {
		return parent.getChildren();
	}

	/**
	 * @param index
	 * @return
	 */
	public Node<T> getChild(int index) {
		return parent.getChild(index);
	}

	/**
	 * decisionTree Learning Method : This is our recursive algorithm that
	 * develops and iterates upon a DTree. It checks to see if the parameters
	 * are empty, and then it calls the pluralityValue method to calculate an
	 * answer for our decision tree, or returns a positive or negative Boolean
	 * leaf if all examples given are positive or negative, respectively.
	 * Otherwise, we begin our algorithm by finding the maximum information gain
	 * of the attributes that were given in our example training set. The
	 * recursive step then begins by making our tree based on the max importance
	 * of the attributes and starting with that at the root. Then for each
	 * choice for that attribute, generate a new array of examples from the
	 * original examples that have that choice, and then call the algorithm in
	 * the recursive loop. Then add a branch to the tree with that choice and
	 * return the tree.
	 * 
	 * @param examples
	 * @param attributes
	 * @param parentExamples
	 * @return
	 */
	public DTree decisionTreeLearning(ArrayList<Example> examples,
			ArrayList<Attribute> attributes, ArrayList<Example> parentExamples) {
		boolean check = true;

		for (Example ex : examples) {
			if (!ex.isShowRepo()) {
				check = false;
				break;
			}
		}

		if (examples.isEmpty()) {
			return getPlurality(parentExamples);
		} else if (check) {
			Node<Attribute> tmpNode = null;

			if (check) {
				tmpNode = new Node<Attribute>(new BooleanAttribute(0));
			} else {
				tmpNode = new Node<Attribute>(new BooleanAttribute(1));
			}

			DTree<Attribute> tmpTree = new DTree<Attribute>(tmpNode);

			return tmpTree;
		} else if (attributes.size() == 0) {
			return getPlurality(examples);
		} else {
			double maxInfo = -1;
			double lastMax = -1;
			Attribute high = null;
			for (Attribute attri : attributes) {
				maxInfo = Math.max(
						informationGain(
								examples.toArray(new Example[examples.size()]),
								attri.getType()), maxInfo);
				System.out.println("maxInfo: " + maxInfo);
				if (lastMax != maxInfo) {
					lastMax = maxInfo;
					high = attri;
				}
			}

			/* Recursive Part of Method */
			System.out.println("High: " + high);
			Node<Attribute> tmpNode = new Node<Attribute>(high);
			DTree<Attribute> tmp = new DTree<Attribute>(tmpNode);
			for (int i = 0; i < high.getLen(); i++) {
				ArrayList<Example> newExs = new ArrayList<Example>();
				for (int j = 0; j < examples.size(); j++) {
					if (examples.get(j).getChoice(high)
							.equals(high.getOptions()[high.getIndex()])) {
						newExs.add(examples.get(j));
					}
				}
				System.out.println("Size of new examples: " + newExs.size());
				attributes.remove(high);
				DTree<Attribute> subtree = new DTree<Attribute>(
						decisionTreeLearning(newExs, attributes, examples));
				tmp.parent.addChild(subtree.getParent());
			}

			return tmp;

		}

	}

	/**
	 * This method merely returns the majority value of previous. They would
	 * have said the majority class if there were only two classes. Plurality is
	 * just the generalization of majority to more than 2 classes. It just means
	 * take the most frequent class in that leaf and return that as the
	 * prediction.
	 * 
	 * @param examples
	 * @return
	 */
	public static DTree<Attribute> getPlurality(ArrayList<Example> examples) {
		int trueCount = 0;
		int falseCount = 0;
		for (int i = 0; i < examples.size(); i++) {
			if (examples.get(i).isShowRepo()) {
				trueCount++;
			} else {
				falseCount++;
			}
		}

		Node<Attribute> tmpNode = null;

		if (trueCount > falseCount) {
			tmpNode = new Node<Attribute>(new BooleanAttribute(0));
		} else if (falseCount > trueCount) {
			tmpNode = new Node<Attribute>(new BooleanAttribute(1));
		} else {
			Random rng = new Random();
			if (rng.nextBoolean() == true) {
				tmpNode = new Node<Attribute>(new BooleanAttribute(0));
			} else {
				tmpNode = new Node<Attribute>(new BooleanAttribute(1));
			}
		}

		DTree<Attribute> tmpTree = new DTree<Attribute>(tmpNode);

		return tmpTree;

	}

	/**
	 * The formula for calculating the entropy of a random variable.
	 * 
	 * @param q
	 * @return
	 */
	public static double entropy(double q) {
		return -1 * (q * Logarithm.log2(q) + (1 - q) * Logarithm.log2(1 - q));
	}

	/**
	 * Calculating the remainder that will be used in the information gain
	 * calculation.
	 * 
	 * @param e
	 * @param toCheck
	 * @return
	 */
	public static double remainder(Example[] e, String toCheck) {
		double sum = 0;
		double one = 0;
		double posOne = 0;
		double two = 0;
		double posTwo = 0;
		double three = 0;
		double posThree = 0;
		double four = 0;
		double posFour = 0;

		if (toCheck != null) {
			if (toCheck.equals("KeywordRange")) {
				for (Example ex : e) {
					int num = ex.getKeywordRange().getIndex();
					if (num == 1) {
						one += 1;
						if (ex.isShowRepo()) {
							posOne += 1;
						}
					} else if (num == 2) {
						two += 1;
						if (ex.isShowRepo()) {
							posTwo += 1;
						}
					} else if (num == 3) {
						three += 1;
						if (ex.isShowRepo()) {
							posThree += 1;
						}
					} else if (num == 4) {
						four += 1;
						if (ex.isShowRepo()) {
							posFour += 1;
						}
					}
				}

				if (one == 0) {
					sum += 0;
				} else {
					sum += (one / e.length) * entropy((posOne / one));
				}

				if (two == 0) {
					sum += 0;
				} else {
					sum += (two / e.length) * entropy((posTwo / two));
				}

				if (three == 0) {
					sum += 0;
				} else {
					sum += (three / e.length) * entropy((posThree / three));
				}

				if (four == 0) {
					sum += 0;
				} else {
					sum += (four / e.length) * entropy((posFour / four));
				}

				System.out.println("Sum: " + sum);
			} else if (toCheck.equals("DateCommitted")) {
				for (Example ex : e) {
					int num = ex.getDateCommitted().getIndex();
					if (num == 1) {
						one += 1;
						if (ex.isShowRepo()) {
							posOne += 1;
						}
					} else if (num == 2) {
						two += 1;
						if (ex.isShowRepo()) {
							posTwo += 1;
						}
					} else if (num == 3) {
						three += 1;
						if (ex.isShowRepo()) {
							posThree += 1;
						}
					} else if (num == 4) {
						four += 1;
						if (ex.isShowRepo()) {
							posFour += 1;
						}
					}
				}
				if (one == 0) {
					sum += 0;
				} else {
					sum += (one / e.length) * entropy((posOne / one));
				}

				if (two == 0) {
					sum += 0;
				} else {
					sum += (two / e.length) * entropy((posTwo / two));
				}

				if (three == 0) {
					sum += 0;
				} else {
					sum += (three / e.length) * entropy((posThree / three));
				}

				if (four == 0) {
					sum += 0;
				} else {
					sum += (four / e.length) * entropy((posFour / four));
				}
			} else if (toCheck.equals("StarRange")) {
				for (Example ex : e) {
					int num = ex.getStarRange().getIndex();
					if (num == 1) {
						one += 1;
						if (ex.isShowRepo()) {
							posOne += 1;
						}
					} else if (num == 2) {
						two += 1;
						if (ex.isShowRepo()) {
							posTwo += 1;
						}
					} else if (num == 3) {
						three += 1;
						if (ex.isShowRepo()) {
							posThree += 1;
						}
					} else if (num == 4) {
						four += 1;
						if (ex.isShowRepo()) {
							posFour += 1;
						}
					}
				}
				if (one == 0) {
					sum += 0;
				} else {
					sum += (one / e.length) * entropy((posOne / one));
				}

				if (two == 0) {
					sum += 0;
				} else {
					sum += (two / e.length) * entropy((posTwo / two));
				}

				if (three == 0) {
					sum += 0;
				} else {
					sum += (three / e.length) * entropy((posThree / three));
				}

				if (four == 0) {
					sum += 0;
				} else {
					sum += (four / e.length) * entropy((posFour / four));
				}
			} else if (toCheck.equals("LikedRepo")) {
				for (Example ex : e) {
					int num = ex.getLikedRepo().getIndex();
					if (num == 1) {
						one += 1;
						if (ex.isShowRepo()) {
							posOne += 1;
						}
					} else if (num == 0) {
						two += 1;
						if (ex.isShowRepo()) {
							posTwo += 1;
						}
					}
				}
				if (one == 0) {
					sum += 0;
				} else {
					sum += (one / e.length) * entropy((posOne / one));
				}

				if (two == 0) {
					sum += 0;
				} else {
					sum += (two / e.length) * entropy((posTwo / two));
				}
			}
		}

		return sum;
	}

	/**
	 * @param e
	 * @return
	 */
	public static double goalEntropy(Example[] e) {
		double posNum = 0;

		for (Example ex : e) {
			if (ex.isShowRepo()) {
				posNum++;
			}
		}

		return entropy(posNum / e.length);
	}

	/**
	 * Calculates the entropy of our goal by calculating the entropy of the
	 * given example – the remainder from that particular example
	 * 
	 * @param e
	 * @param toCheck
	 * @return
	 */
	public static double informationGain(Example[] e, String toCheck) {
		return goalEntropy(e) - remainder(e, toCheck);
	}

	/**
	 * @return
	 */
	public ArrayList<T> levelorder() {
		Vector<Node<T>> nodeQueue = new Vector<Node<T>>();
		ArrayList<T> iter = new ArrayList<T>();

		if (this.parent != null) {
			nodeQueue.add(parent);
			while (!nodeQueue.isEmpty()) {
				Node<T> current = nodeQueue.remove(0);
				System.out.println(current);
				iter.add(current.getElement());
				System.out.println(current.getChildren().size());
				for (int i = 0; i < current.getChildren().size(); i++) {
					System.out.println(current.getChild(i));
					nodeQueue.add(current.getChild(i));
				}
			}
		}
		return iter;
	}

	/**
	 * This will query our decision tree and ask what type of node it is to
	 * determine the attribute. They call the switchOnAttribute functions below,
	 * which merely return the value associated with the options and children.
	 * 
	 * @param watchers
	 * @param keyword
	 * @param date
	 * @return
	 */
	public String queryTree(int watchers, int keyword, long date) {
		Node<Attribute> tempNode = (Node<Attribute>) this.parent;
		String type;

		while (!tempNode.getElement().getType().equals("BooleanAttribute")) {
			type = tempNode.getElement().getType();
			if (type.equals("DateCommitted"))
				tempNode = switchOnDateNode(date, tempNode.getChildren());
			else if (type.equals("KeywordRange"))
				tempNode = switchOnKeywordNode(keyword, tempNode.getChildren());
			else if (type.equals("StarRange"))
				tempNode = switchOnStarNode(watchers, tempNode.getChildren());
		}

		return tempNode.getElement().getChoice();
	}

	/**
	 * @param date
	 * @param children
	 * @return
	 */
	private Node<Attribute> switchOnDateNode(long date,
			ArrayList<Node<Attribute>> children) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long lastCommit = cal.getTimeInMillis() / 1000L - date;

		if (lastCommit <= 604800) {
			return (Node<Attribute>) children.get(0);
		} else if (lastCommit <= 2592000) {
			return (Node<Attribute>) children.get(1);
		} else if (lastCommit <= 31536000) {
			return (Node<Attribute>) children.get(2);
		} else {
			return (Node<Attribute>) children.get(3);
		}
	}

	/**
	 * @param keywords
	 * @param children
	 * @return
	 */
	private Node<Attribute> switchOnKeywordNode(int keywords,
			ArrayList<Node<Attribute>> children) {
		if (keywords < 30)
			return (Node<Attribute>) children.get(0);
		else if (keywords < 50)
			return (Node<Attribute>) children.get(1);
		else if (keywords < 80)
			return (Node<Attribute>) children.get(2);
		else
			return (Node<Attribute>) children.get(3);
	}

	/**
	 * @param stars
	 * @param children
	 * @return
	 */
	private Node<Attribute> switchOnStarNode(int stars,
			ArrayList<Node<Attribute>> children) {
		if (stars < 50)
			return (Node<Attribute>) children.get(0);
		else if (stars < 100)
			return (Node<Attribute>) children.get(1);
		else if (stars < 500)
			return (Node<Attribute>) children.get(2);
		else
			return (Node<Attribute>) children.get(3);
	}

	/**
	 * @param liked
	 * @param children
	 * @return
	 */
	private Node<Attribute> switchOnLikedNode(boolean liked,
			ArrayList<Node<Attribute>> children) {
		if (liked)
			return (Node<Attribute>) children.get(1);
		else
			return (Node<Attribute>) children.get(0);
	}
}
