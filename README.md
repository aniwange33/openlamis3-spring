## Build

### Prerequisites

#### Java

LAMIS is a Java application which is why you need to install a Java JDK.

If you want to build the master branch you will need a Java JDK of minimum version 8.

#### Maven

Install the build tool [Maven](https://maven.apache.org/).

You need to ensure that Maven uses the Java JDK needed for the branch you want to build.

To do so execute

```bash
mvn -version
```

which will tell you what version Maven is using. Refer to the [Maven docs](https://maven.apache.org/configure.html) if you need to configure Maven.

#### Git

Install the version control tool [git](https://git-scm.com/) and clone this repository with

```bash
git clone https://gitlab.com/fhi360/open-lamis.git
```

### Build Command

After you have taken care of the [Prerequisites](#prerequisites)

Execute the following

```bash
cd 
mvn clean install
```

This will generate the LAMIS application in `target/lamis-3.0.0.war` which you will have to deploy into an application server like for example [tomcat](https://tomcat.apache.org/) or [jetty](http://www.eclipse.org/jetty/).

### Deploy

For development purposes you can simply deploy the ` lamis-3.0.0.war`   using any server of your choice (tomcat among others)

```bash
mvn spring-boot:run
```
If all goes well (check the console output) you can access the LAMIS 3.0 application at `localhost:8080/`.


