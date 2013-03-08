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

#Deployment instruction
Before using Kadaster Bag Client these few steps have to be completed :

 1.  Java keystore file that contains certificate and private key obtained from Kadaster has to be created
 2.  Truststore file for communication with Kadaster has to be created or selected from project
 3.  Environment variable has to be created
 4.  User specific property files have to be configured

After all steps are completed generated api.war file located at api/target/api.war can be deployed at desired tomcat server.

##Creation of keystore file
Creation of java keystore file is done in two steps.
In first step certificate and private key obtained from Kadaster are used to generate PKCS12 file:

    openssl pkcs12 -export -in your_certificate.crt -inkey your_key.key -out temp.p12
    
And in second step this temporary PKCS12 file is used to create java kestore file :

    keytool -importkeystore -deststorepass custom_password -destkeypass custom_password -destkeystore custom_name.keystore -deststoretype JKS -srckeystore temp.p12 -srcstoretype PKCS12 -srcstorepass entered_password
    
After creation of java keystore file is created PKCS12 can be deleted, and new java keystore file places into desired location. This keystore will be referenced in geocoding.properties file.

##Creation of truststore file
Because root CA for Kadaster Bag's certificate is Nederland, and that certificate is not in java trusted certificates, it has to be downloaded and placed into custom truststore file.
Url for download of that certificate can be obtained from Mozilla trusted certificates (http://www.mozilla.org/projects/security/certs/included/#Staat%20der%20Nederlanden%20/%20Logius).
Certificate should be downloaded, and keytool can be used to create truststore from obtained certificate :

    keytool -import -file path_to_the_file -alias nederland_ca -keystore myTrustStore
    
After creation of java truststore file is created downloaded certificate can be deleted, and new java truststore file places into desired location. This truststore will be referenced in geocoding.properties file.

##Creation of environment variable
Name for environment variable should be GEO_CODING_HOME, and her value should be absolute path to the folder that contains user specific properties.
In MacOS or Linux it should be something like :

    /home/username/resources/kadaster-resources
    
In Windows it should be something like :

    D:\resources\kadaster-resources
    
This environment variable is used in src/main/resources/META-INF/applicationContext.xml, so if name should be changed - new name should appear in this config file.

##Configuration of user specific properties
Properties that should be placed here can be copied from src/test/resources folder. They are :
db.properties
geocoding.properties
If custom names for this files are used, again modification has to be made in src/main/resources/META-INF/applicationContext.xml.
For db.properties file user specific database type and authorization have to be overwritten.
One thing that should be done after this step is creation of selected database (without any tables in it, just database that can be accessed with selected authorization parameters).
For geocoding.properties file lines that are commented out in test scope should be uncommented, and proper values should be assigned to them.
