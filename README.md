## mavenJsob ###

#### mavenJson is a maven plugin to obfuscate javascripts that are about to be packed into war files ####

#####Usage Example:#####
This will have to be used with a maven war plugin running in exploded deploy mode since maven processes and packages files into war all in one go.

    <build>
      	<finalName>visit-web</finalName>
    		<plugins>
    			<plugin>
    				<groupId>org.apache.maven.plugins</groupId>
    				<artifactId>maven-war-plugin</artifactId>
    				<version>2.2</version>
    				<executions>
    					<execution>
    						<id>prepare-war</id>
    						<phase>prepare-package</phase>
    						<goals>
    							<goal>exploded</goal>
    						</goals>
    					</execution>
    				</executions>
    				<!-- <configuration> <webappDirectory>/sample/servlet/container/deploy/directory</webappDirectory> 
    					</configuration> -->
    			</plugin>
    			<plugin>
    				<groupId>org.apache.maven.plugins</groupId>
    				<artifactId>maven-jsobfuscator-plugin</artifactId>
    				<version>0.0.1-SNAPSHOT</version>
    				<executions>
    					<execution>
    						<configuration>
    							<jsSourceFiles>
    								<jsSourceFile>${project.build.directory}/${project.build.finalName}/plain.js</jsSourceFile>
    							</jsSourceFiles>
    							<externSource>${project.build.directory}/${project.build.finalName}/extern.js</externSource>
    							<externType>File</externType>
    						</configuration>
    						<goals>
    							<goal>garble</goal>
    						</goals>
    						<id>Obfuscation Step</id>
    						<phase>prepare-package</phase>
    					</execution>
    				</executions>
    			</plugin>
    		</plugins>
    	</build>