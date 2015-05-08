// package gitrecommender.decisionTree;
import java.util.Calendar;
import java.util.TimeZone;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class DTree<T> {

	protected Node<T> parent;

	/**
	 * @param dTree
	 */
	public DTree(DTree<T> dTree) {
		this.parent = dTree.getParent();
	}

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

	public ArrayList<Node<T>> getChildren() {
		return parent.getChildren();
	}

	public Node<T> getChild(int index) {
		return parent.getChild(index);
	}

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

			// Dis be where recursion happens
			System.out.println("High: " + high);
			Node<Attribute> tmpNode = new Node<Attribute>(high);
			DTree<Attribute> tmp = new DTree<Attribute>(tmpNode);
			for (int i = 0; i < high.getLen(); i++) {
				ArrayList<Example> newExs = new ArrayList<Example>();
				for (int j = 0; j < examples.size(); j++) {
					if (examples.get(j).getChoice(high)
							.equals(high.getOptions()[high.getIndex()])) {
						// System.out.println(examples.get(j));
						// System.out.println(examples.get(j).getChoice(high));
						// System.out.println(high.getOptions()[high.getIndex()]);
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

	public static double entropy(double q) {
		return -1 * (q * Logarithm.log2(q) + (1 - q) * Logarithm.log2(1 - q));
	}

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
					/*
					 * "zerothirty", "thirtyfifty", "fiftyeighty",
					 * "eightyonehundred"
					 */
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

	public static double goalEntropy(Example[] e) {
		double posNum = 0;

		for (Example ex : e) {
			if (ex.isShowRepo()) {
				posNum++;
			}
		}

		return entropy(posNum / e.length);
	}

	public static double informationGain(Example[] e, String toCheck) {
		return goalEntropy(e) - remainder(e, toCheck);
	}

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

	public String queryTree(int watchers, int keyword, long date) {
		Node<Attribute> tempNode = (Node<Attribute>)this.parent;
		String type;

		while(!tempNode.getElement().getType().equals("BooleanAttribute")) {
			type = tempNode.getElement().getType();

			switch(type) {
				case("BooleanAttribute"):
					break;
				case("DateCommitted"):
					tempNode = switchOnDateNode(date, tempNode.getChildren());
					break;
				case("KeywordRange"):
					tempNode = switchOnKeywordNode(keyword, tempNode.getChildren());
					break;
				case("StarRange"):
					tempNode = switchOnStarNode(watchers, tempNode.getChildren());
					break;
			}
		}

		return tempNode.getElement().getChoice();
	}

	private Node<Attribute> switchOnDateNode(long date, ArrayList<Node<Attribute>> children) {
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

	private Node<Attribute> switchOnKeywordNode(int keywords, ArrayList<Node<Attribute>> children) {
		if(keywords < 30) return (Node<Attribute>) children.get(0);
		else if(keywords < 50) return (Node<Attribute>) children.get(1);
		else if(keywords < 80) return (Node<Attribute>) children.get(2);
		else return (Node<Attribute>) children.get(3);
	}

	private Node<Attribute> switchOnStarNode(int stars, ArrayList<Node<Attribute>> children) {
		if(stars < 50) return (Node<Attribute>) children.get(0);
		else if(stars < 100) return (Node<Attribute>) children.get(1);
		else if(stars < 500) return (Node<Attribute>) children.get(2);
		else return (Node<Attribute>) children.get(3);
	}

	private Node<Attribute> switchOnLikedNode(boolean liked, ArrayList<Node<Attribute>> children) {
		if(liked) return (Node<Attribute>) children.get(1);
		else return (Node<Attribute>) children.get(0);
	}
}
