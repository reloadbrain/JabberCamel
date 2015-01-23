# JabberCamel
A Java Spring application which spits out XMPP chat onto a website and does some other cool stuff.


## Technology
* Spring Framework (ORM, WebSockets, MVC)
* Apache Camel
* MySQL

## WTF it does
JabberCamel connects to an XMPP conference using Camel, which then converts the raw message to a Java object, persists it to a database, then converts it to JSON and sends it to a WebSocket topic, for consumption by front-end clients.

## How to set up
In ``` src/main/resources/META-INF/templates ``` you will find template.properties. Fill this out with your DB / XMPP details, and copy it to your ``` src/main/resources/META-INF ``` folder. Reference this in ``` src/main/webapp/WEB-INF/spring/appServlet/camel-context.xml ``` and ``` pom.xml ``` and it _should_ work.

Create your database prior to running, but the tables should create automatically thanks to the wizardry of JPA.

Once it's running, go to ``` localhost:8080/jabbercamel ```. That's it.

## TODO
- [ ] Emotion Tracking
- [ ] Popular Links
- [ ] Topic History (Not sure if this is possible)
