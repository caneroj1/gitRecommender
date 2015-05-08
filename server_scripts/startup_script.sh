mvn compile;
mvn process-classes;
mvn war:war;
cp target/gitrecommender-11.war /Users/joecanero/Downloads/apache-tomcat-7.0.61/webapps;
/Users/joecanero/Downloads/apache-tomcat-7.0.61/bin/catalina.sh stop;
/Users/joecanero/Downloads/apache-tomcat-7.0.61/bin/catalina.sh start;
