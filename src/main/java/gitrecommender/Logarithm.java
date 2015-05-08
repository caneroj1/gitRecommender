// package gitrecommender.decisionTree;

//This class is used to determine logarithms that are not base 10, which is not supported in Java.

public class Logarithm {
	// Uses mathematical statement logb x = logk x / logk b. Log base b of x =
	// log base k of x / log base k of b, where log k is a function that returns
	// the base-k logarithm of a number. In this case we use k = 10 to determine
	// the logBase of x given the base. Takes 2 doubles as parameters, returns
	// the logbase of x as a double.

	public static double logBase(double x, double base) {
		return Math.log(x) / Math.log(base);
	}

	// This gives the log base 2 of a double, returns a double.
	public static double log2(double x) {
		return logBase(x, 2);
	}
}
