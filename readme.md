gitRecommender
==============
gitRecommender Final project for Artificial Intelligence. It is a recommender system that will suggest github repositories you might be interested in.

-------------

ReadMe
-----------
>This document describes the classes that enable our decision tree to rebuild itself based on examples, and to query the tree using an example to output whether we show a repo or not.
>
> * Note: "repo" is short for "repository"

`DTree.java` is the core of our project. It gives us the ability to make trees of generic type. It consists of a parent node. The node class, a generic typed class, contains the generic element and a list of its children of the same type.
The main methods for this class are `decisionTreeLearning` and `queryTree`. `decisionTreeLearning` uses the algorithm for building a decision tree, described in the textbook, using helper methods such as `informationGain`, `entropy`, `remainder`, `getPlurality`, and `goalEntropy`. We can then query the DTree using our `queryTree` method, which follows the tree and returns "true" or "false" for whether we show an example based on previous examples.

To encapsulate the data necessary to test Attributes using Examples, we created Example classes and Attribute classes. The example class has all four attributes that we care about for making decisions, as well as whether it is a positive or negative example. The Attribute class is a parent class for our five types of Attributes that we have: the `DateCommitted`, which tests the most recent commit date to a repo; `KeywordRange`, which takes the keyword score given from the user's keywords; `LikedRepo`, which becomes true if the user follows the link for the repos suggested; `StarRange`, which gives us a range of the number of follows a repo has; and the `BooleanAttribute`, which returns true or false for making a decision using the Decision Tree.
The five children of Attributes have arrays of all of their decisions. When constructing an Attribute, we also instantiate a specific choice for each Attribute, which allows our examples to describe all the details associated with it that we get from input.

Finally, we have a `Logarithm.java` class, because Java doesn't have native support for logarithms that aren't base 10, which allows our entropy function to work.