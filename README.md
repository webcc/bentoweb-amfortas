# Amfortas: User testing framework to analyse Test Suites (http://bentoweb.org/tools.html)

The user testing framework to analyse the developed Test Suites has two major components: Parsifal and Amfortas. Both components are highly tailored to the selected back-end supporting tools like CVS, Apache Cocoon, Apache Tomcat, Apache HTTP Server, and especially, TCDL, the Test Case Description Language. Parsifal is an innovative usability tool that aims to support accessibility and usability experts, who may not have a technical background, to access the TCDL metadata of the test case itself and the corresponding test files. Furthermore, the tool allows the expert to design testing scenarios with questionnaires, which are then automatically filtered and parsed by Amfortas — the online back-end —  for online testing. BenToWeb partners are seeking for partnerships that will allow the generalisation and further development of these applications to the Web usability domain for online testing.

## Key Technologies
+ Java
+ XML
+ [Apache Cocoon](https://cocoon.apache.org/)
+ [Apache Tomcat](https://tomcat.apache.org/)
+ [Apache HTTP Server](http://httpd.apache.org/)
+ [TCDL](https://github.com/webcc/bentoweb-tcdl)

## Config and Run
+ Build webapp (Eclipse recommended)
+ Deploy to container (Tomcat 7 recommended)
+ Open WebContent\WEB-INF\cocoon.xconf
+ Look for  jdbc name="amfortas" and edit DB info
+ Look for smtp-host, edit if required
+ Open your browser and point it to http://localhost:8080/Amfortas/amfortas/home.html



 
