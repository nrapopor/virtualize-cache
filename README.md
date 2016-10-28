# virtualize-cache

## Synopsis

This project implements a simple singleton **Caffeine** cache. 

## Description

The intended use is the caching of transactional info in the Parasoft Virtualize. 

### Usage Details

* This utility is intended to be used from the ExtensionTool Scripts.
* To use please add the jars found in the distribution zip to the ***Menu->Parasoft->Preferences->System Settings***. The virtualize-cache-\<VERSION\>.jar and all the jars in the `lib` directory of the distribution archive.
* Cache the transactional data in the first responder in the transaction.
* Place the key into a **cacheKey** Data Bank column. then the scripts below can be used without modification 

  1. Extract the transactional elements from incoming message into the appropriate **Data Bank**    
 
  2. Add the ExtensionTool to the same responder (Groovy sample)  
  ```groovy
  	import com.parasoft.api.*;
  	import com.parasoft.sa.virtualize.cache.*;
	import org.apache.commons.io.*;
	import org.apache.commons.collections.*;
	import java.io.*;
	import java.util.*;
	import java.util.regex.*;
	
	
	public void saveToCache(Object o, ExtensionToolContext ctx) {
	
		Map<String,String> all = ctx.getGeneratedDataSourceValues();
		Map<String,String> address = new HashMapXML<>(all);	
		String sessionId = ctx.getValue("Generated Data Source", "cacheKey");
	    Application.showMessage("address: "+address.toString());
	    final VirtualizeCache<Map<String, String>> cache = VirtualizeCache.getXMLMapInstance();
	    cache.put(sessionId,address);
	}  
  ```
  * NOTE: Make sure that the extraction of data from the input is done first.

* Extract the transactional data cached in the first responder for any subsequent responders participating in transaction.

  1. Extract the transactional key from incoming message into the appropriate **Data Bank**    
 
  2. Add the ExtensionTool to the same responder 
  ```groovy
  	import com.parasoft.api.*;
  	import com.parasoft.sa.virtualize.cache.*;
	import org.apache.commons.io.*;
	import org.apache.commons.collections.*;
	import java.io.*;
	import java.util.*;
	import java.util.regex.*;
	
	
	public String getCached(Object o, ExtensionToolContext ctx) {
	
	   
	    final VirtualizeCache<Map<String, String>> cache = VirtualizeCache.getXMLMapInstance();
	    String sessionId = ctx.getValue("Generated Data Source", "cacheKey");
	 	Map<String,String> result = cache.getIfPresent(sessionId);
	    Application.showMessage("found in cache: "+result.toString());
	    return result.toString();
	}
  
  ```
  * NOTE: Make sure that the extraction of data from the input is done first.
  * Extract individual elements from the **result** XML into the **XML Data Bank**
  * They are now ready to be used for the response message. 

### Default settings

The default configuration setting for the Cache are: to override add the CacheConfig.properties to the class path **before** the virtualize-cache-\<VERSION\>.jar

```properties
    VirtualizeCache.cache.eviction.msg=Key "{}" for "{}"  was removed ({})
	VirtualizeCache.cache.size=10000 
```
    
 

## How to build

This utility will need to be built using ``maven`` [https://maven.apache.org/](https://maven.apache.org/ "https://maven.apache.org/")    
just install java 8 and maven and run

```bash
   maven clean install
```

### Windows

1. Download and install(unzip) Maven I recommend a short path with no spaces in it like ``c:\tools\``    
    so the final path to maven would be something like ``c:\tools\apache-maven-X.X.X`` where     
    ``X.X.X`` is the maven version
2. Create a ``.m2`` folder under your user id    
   on Windows 7+ this means ``C:\Users\<your login id>``     
   on older versions ``C:\Documents and Settings\<your login id>``    
   - NOTE: This cannot be done graphically in ``explorer`` only on the command line using    
 ```cmd
     md .m2     
 ```

3. Copy the ``settings.xml`` from maven installation folder like ``c:\tools\apache-maven-3.3.9\conf``

  + NOTE: I recommend against installing into "Program Files" we will be using command line and     
        scripting. Spaces in folder/file names and scripts do not mix.

        
7.	Install java JDK (yes JDK not JRE). Please use the latest java (which at the time of this writing    
    is version 8.
    - Download Java JDK from [oracle website](http://www.oracle.com/technetwork/java/javase/downloads/index.html "http://www.oracle.com/technetwork/java/javase/downloads/index.html")    
    - run installation
    - Create a link to the ``C:\Program Files\Java`` from a path that does     
      not have spaces in it. This will need to be run from an Administrator console    
      ``Right-Click`` on the ``Cmd.exe`` link and click *Run As Administrator*    
      For example: 
 ```cmd
        mklink /J C:\tools\java "C:\Program Files\Java"
        mklink /J C:\tools\java32 "C:\Program Files (x86)\Java"
 ```
8.	We will need both Java and Maven in the `PATH` and `JAVA_HOME` and `M2` and `M2_HOME`    
   (also `M3` and `M3_HOME`) set to properly run maven. 
   - NOTE: (`M3`/`M3_HOME` may not be needed any more, however this was an issue at one point so I'm keeping them)
   - You can add the following to your system environment variables and `PATH` or create a    
     `setenv_m3.cmd` in a convenient location (one that's been added to the system path, for me it's ``c:\work\bin``
     or simply put it into the current folder (where you checked out this project).

 ```cmd
        @set M2=c:\tools\apache-maven-3.3.9
        @set M3=%M2%
        @set M2_HOME=%M2%
        @set M3_HOME=%M2%
        @set JAVA_HOME=C:\tools\java\jdk1.8.0_92
        @set PATH=%M2%\bin;%JAVA_HOME%\bin;%PATH%
        @echo M2=%M2%
        @echo JAVA_HOME=%JAVA_HOME%
 ```


9.	Ok enough foreplay lets get down to cases. In the checked out folder run:
 ```cmd
           setenv_m3.cmd
           mvn clean install
 ```

### Unix or Linux systems

 - NOTE: Order of installs is important *java* then *maven*
 
1.	Install Oracle JDK. on debian or ubuntu based systems you can use this script:
 ```sh
    #!/bin/bash
    RELEASE=`lsb_release -cs`
    LINE_CNT=`grep  "http://ppa.launchpad.net/webupd8team" /etc/apt/sources.list | wc -l`
    if [[ ! ${LINE_CNT} -gt 1 ]]; then
            sudo echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu ${RELEASE} main" | sudo tee -a /etc/apt/sources.list
            sudo echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu ${RELEASE} main" | sudo tee -a /etc/apt/sources.list
            sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
    fi
    sudo apt-get update
    sudo apt-get install oracle-java8-installer
 ```   
 - if you are not that lucky, go to [oracle website](http://www.oracle.com/technetwork/java/javase/downloads/index.html "http://www.oracle.com/technetwork/java/javase/downloads/index.html")    
 and download the package appropriate for your system (`rpm` or `tar.gz`)  
  + if `rpm` (put the correct version below instead of ``8u92``  use the latest :
 ```sh
    ## Oracle java 1.8.0_92 ##
    sudo rpm -Uvh jdk-8u92-linux-x64.rpm
    ## java ##
    sudo alternatives --install /usr/bin/java java /usr/java/latest/jre/bin/java 200000
    ## javaws ##
    sudo alternatives --install /usr/bin/javaws javaws /usr/java/latest/jre/bin/javaws 200000
     
    ## Java Browser (Mozilla) Plugin 32-bit ##
    sudo alternatives --install /usr/lib/mozilla/plugins/libjavaplugin.so libjavaplugin.so /usr/java/latest/jre/lib/i386/libnpjp2.so 200000
     
    ## Java Browser (Mozilla) Plugin 64-bit ##
    sudo alternatives --install /usr/lib64/mozilla/plugins/libjavaplugin.so libjavaplugin.so.x86_64 /usr/java/latest/jre/lib/amd64/libnpjp2.so 200000
     
    ## Install javac only if you installed JDK (Java Development Kit) package ##
    sudo alternatives --install /usr/bin/javac javac /usr/java/latest/bin/javac 200000
    sudo alternatives --install /usr/bin/jar jar /usr/java/latest/bin/jar 200000
 ```
  + if `tar.gz` (place the file in your home folder (put the correct version instead of    
     ``8u92``  use the latest :
 ```sh
    ## Oracle java 1.8.0_92 ##
    tar -xvf jdk-8u92-linux-x64.tar.gz
 ```
  + If this is your option the JAVA_HOME will be `~/jdk1.8.0_92` (or whatever the current version is)
     add the following lines to your `~/.bashrc` or `~/.profile` (replacing the ``<your user id>`` with your    
     actual user id
 ```sh
    export JAVA_HOME=/home/<your user id>/jdk1.8.0_92
    export PATH=${JAVA_HOME}/bin:${PATH}
 ```
  + NOTE: The settings will not take effect until you logout/login again. Do that before doing step 2 

2.	Download and install(unzip) Maven or again on debian, ubuntu or debian clones:
 ```sh
    sudo apt-get maven
 ```
  + If downloading and un-zipping manualy
 ```sh
    tar -xvf apache-maven-<version>-bin.tar.gz
 ```
  + If this is your option the `M2` will be ~/apache-maven-\<version\> 
    add the following lines to your `~/.bashrc` or `~/.profile` after the java lines (replacing the     
     ``<your user id>`` with your actual user id and `<version>` with the actuall maven version
 ```sh
    export M2=/home/<your user id>/apache-maven-<version>
    export M3=${M2}
    export M2_HOME=${M2}
    export M3_HOME=${M2}
    export PATH=${M2}/bin:${PATH}
 ```
   + NOTE: Ignore step 8 and any references to the ``setenv_m3.sh`` script. The settings will    
           not take effect until you logout/login again. 
         
8.	We will need both Java and Maven in the PATH and `JAVA_HOME` and `M2` and `M2_HOME`    
   (also `M3` and `M3_HOME`) set to properly run maven. 
 - NOTE: (`M3`/`M3_HOME` may not be needed, however this was an issue at one point so I'm keeping them)
 - You can add the following to your `.profile` (or `.bashrc`) however I do not like fixing specific versions     
     in the initialization. place `setenv_m3.sh` in ``~/bin``
     or simply put it into the current folder (where you checked out this project).

 ```sh
    export M2=/usr/share/maven/
    export M3=${M2}
    export M2_HOME=${M2}
    export M3_HOME=${M2}
    export JAVA_HOME=/usr/lib/jvm/java-8-oracle/
    export PATH=${M2}/bin:${JAVA_HOME}/bin:${PATH}
    echo M2=${M2}
    echo JAVA_HOME=${JAVA_HOME}
 ```

9.	OK enough foreplay, lets get down to cases. In the checked out folder run:
 ```sh
           . ~/bin/setenv_m3.sh
           mvn clean install
 ```

### Build Results

10.	If everything goes well: 
 - The `target` folder where the project was checked out will have **virtualize-cache-\<VERSION\>-bin-dist.*** and **virtualize-cache-\<VERSION\>.jar** files.    
 Pick the `bin` archive that's appropriate to the target `OS` type. and unzip it.

## Notes on this Project
This project was created in late 2016 and all the references to maven and Java versions will     
probably get old very quickly 
            
## Author and Legal information

### Author

Nick Rapoport

### Copyright

Copyright&copy;  2016 Nick Rapoport -- All rights reserved (free 
for duplication under the AGPLv3)

### License

AGPLv3

## Based On 

#### Projects
- [Caffeine - a high performance cache implementation for Java 8](https://github.com/ben-manes/caffeine/ "https://github.com/ben-manes/caffeine")

#### Date
2016-10-27

