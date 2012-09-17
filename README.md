#kadaster-bag-client
===================

Java client om de webservice van het kadaster met basisregistraties van adressen en gebouwen te bevragen.

#Deployment instruction
to successfully create the application .war file run maven command
mvn clean install -DskipTests

To be able to run the application on tomcat or jetty server first create a system environment variable GEO_CODING_HOME that will point to location where properties files will be stored.
Necessary properties files are the same as in DEVELOPMENT instruction part with minor difference that some of the files dont have "_tests" part in its name. Name of the files shoud be db.properties, geocoding.properties and log4j.properties.

#Development instruction
To start with development every developer need to create configuration a folder for test properties in api/config/test/{username} 
in the folder need to have three properties files:

db_test.properties

	jdbc.username=root
	jdbc.password=123
	jdbc.driver=com.mysql.jdbc.Driver
	jdbc.url=jdbc:mysql://localhost:3306/kadasterbagclient_test?useUnicode=true&characterEncoding=UTF-8

geocoding_test.properties

	max.validation.period.sec=1000

log4j.properties

	log4j.debug=true

	log4j.rootLogger=INFO,Stdout
	log4j.logger.org=INFO
	log4j.logger.com=INFO
	log4j.logger.net=INFO
	log4j.logger.crawler=INFO

	log4j.appender.Stdout=org.apache.log4j.ConsoleAppender
	log4j.appender.Stdout.layout=org.apache.log4j.PatternLayout
	log4j.appender.Stdout.layout.conversionPattern=%-5p - %-26.26c{1} - %m\n



