mvn process-classes;
mvn compile war:war;
cp target/gitrecommender-2.52.war /Users/joecanero/Downloads/apache-tomcat-7.0.61/webapps;
/Users/joecanero/Downloads/apache-tomcat-7.0.61/bin/catalina.sh stop;
/Users/joecanero/Downloads/apache-tomcat-7.0.61/bin/catalina.sh start;
