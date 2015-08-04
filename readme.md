gitRecommender
==============
gitRecommender Final project for Artificial Intelligence. It is a recommender system that will suggest github repositories you might be interested in.

>**Our compiled project can be accessed at: ~~http://45.56.104.253:8080/gitrecommender-11~~**

-------------

ReadMe
-----------
>This document describes the classes that enable our decision tree to rebuild itself based on examples, and to query the tree using an example to output whether we show a repo or not.
>
> Notes: 
>   * "repo" is short for "repository"
>   * Within the `src` folder you can find any relevant source code.

`Driver.java` is our driver class with the main method. Our driver class writes out the webpage via Apache Tomcat as HTTP Requests occur. Driver interacts with `WebRequest.java` to simpilify the process of serving html.

The `Recommender.java` class is a wrapper for the processing we do on each repository. Our k-nearest-neighbors implementation calculates the desirability of each repository using the methods contained in here. The methods are basically all static, and do not require an instantiation of this class.

The `Repository.java` class simplifys the interactions with the server's postgresql database.

`SuffixTreeNode.java` provides nodes for the suffix tree. Each node essentially stores more suffix tree  nodes in an array that is indexed by the ascii decimal value of a character.

`DTree.java` is the core of our project. It gives us the ability to make trees of generic type. It consists of a parent node. The `Node.java` class, a generic typed class, contains the generic element and a list of its children of the same type.
The main methods for this class are `decisionTreeLearning` and `queryTree`. `decisionTreeLearning` uses the algorithm for building a decision tree, described in the textbook, using helper methods such as `informationGain`, `entropy`, `remainder`, `getPlurality`, and `goalEntropy`. We can then query the DTree using our `queryTree` method, which follows the tree and returns "true" or "false" for whether we show an example based on previous examples.

To encapsulate the data necessary to test Attributes using Examples, we created `Example.java` classes and `Attribute.java` classes. The example class has all four attributes that we care about for making decisions, as well as whether it is a positive or negative example. The Attribute class is a parent class for our five types of Attributes that we have: the `DateCommitted`, which tests the most recent commit date to a repo; `KeywordRange`, which takes the keyword score given from the user's keywords; `LikedRepo`, which becomes true if the user follows the link for the repos suggested; `StarRange`, which gives us a range of the number of follows a repo has; and the `BooleanAttribute`, which returns true or false for making a decision using the Decision Tree.
The five children of Attributes have arrays of all of their decisions. When constructing an Attribute, we also instantiate a specific choice for each Attribute, which allows our examples to describe all the details associated with it that we get from input.

We have a `Logarithm.java` class, because Java doesn't have native support for logarithms that aren't base 10, which allows our entropy function to work.

`TreeDriver.java` starts the learning and deciding processes on our DTree.

`pom.xml` is for the Apache Maven build tool.

`WebContent` is for any static web content (css, js, libraries), and some info for our Apache Tomcat Webserver.

`ruby_scripts` are a collection of small programs to grab information about repos to use in our algorithms and postgresql database. Within this folder you can find `datasets`, in this folder lies all of our training datasets for our decision tree.

`server_scripts` are a collection of small programs that help us compile with Apache Maven, and test using a local instance of Apache Tomcat.

`.gitignore` tells git to ignore any irrelevant files.
