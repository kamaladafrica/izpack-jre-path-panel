# JREPathPanel for IzPack
Same as [JDKPathPanel](https://github.com/izpack/izpack/blob/master/izpack-panel/src/main/java/com/izforge/izpack/panels/jdkpath/JDKPathPanel.java) but for JRE.
The only difference between JREPathPanel and JDKPathPanel is that the JREPathPanel accept a JDK as choice too.

You have only to keep in mind that all variables must be called with JRE prefix instead of JDK.

Es. 
***JDK**PathPanel.minVersion* become ***JRE**PathPanel.minVersion* and ***jdk**Path* become ***jre**Path*

## Usage

This is a simple [Custom Panel](https://izpack.atlassian.net/wiki/spaces/IZPACK/pages/491687/Custom+Panels), so you have to add it to your project.
The usage is the same as JDKPathPanel, so you can show the [JDKPathPanel official documentation](https://izpack.atlassian.net/wiki/spaces/IZPACK/pages/491644/JDKPathPanel).

### Maven configuration

In pom.xml

```xml
<plugin>
	<groupId>org.codehaus.izpack</groupId>
	<artifactId>izpack-maven-plugin</artifactId>
	<version>${izpack.plugin.version}</version>
	<extensions>true</extensions>
	<executions>
		<execution>
			<phase>package</phase>
			<goals>
				<goal>izpack</goal>
			</goals>
		</execution>
	</executions>
	<dependencies>
		<dependency>
			<groupId>org.codehaus.izpack</groupId>
			<artifactId>izpack-panel</artifactId>
			<version>${izpack.plugin.version}</version>
		</dependency>
		<dependency>
			<groupId>it.kamaladafrica.izpack.panel</groupId>
			<artifactId>jre-path-panel</artifactId>
			<version>1.0</version>
		</dependency>
	</dependencies>
</plugin>
...
<repositories>
	<repository>
		<id>jcenter</id>
		<name>jcenter</name>
		<url>http://jcenter.bintray.com</url>
 	</repository>
</repositories>
```

In install.xml
```xml
<panels>
  <panel classname="it.kamaladafrica.izpack.panel.jrepath.JREPathPanel"/>
</panels>
```
