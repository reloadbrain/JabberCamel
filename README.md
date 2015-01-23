# JabberCamel
A Java Spring application which spits out XMPP chat onto a website and does some other cool stuff.


## Technology
Spring Framework
Apache Camel

## How to set up

In ``` src/main/resources/META-INF/templates ``` you will find template.properties. Fill this out with your DB / XMPP details, and copy it to your ``` src/main/resources/META-INF ``` folder. Reference this in ``` src/main/webapp/WEB-INF/spring/appServlet/camel-context.xml ``` and ``` pom.xml ``` and it _should_ work.

Create your database prior to running, but the tables should create automatically thanks to the wizardry of JPA.

Once it's running, go to localhost:8080/jabbercamel. That's it.