mvn process-classes;
mvn compile war:war;
cp target/gitrecommender-6.8.war /Users/joecanero/Downloads/apache-tomcat-7.0.61/webapps;
/Users/joecanero/Downloads/apache-tomcat-7.0.61/bin/catalina.sh stop;
/Users/joecanero/Downloads/apache-tomcat-7.0.61/bin/catalina.sh start;
