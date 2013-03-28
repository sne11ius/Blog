Deprecation warning ;)
======================

This thing is deprecated & ugly. Please go to https://github.com/sne11ius/jlog to find a new and improved version.

Blog
====

Simple java blog with gplus login.

Stack
=====
 - mongodb & com.github.jmkgreen.morphia
 - sparkweb
 - freemarker
 - com.google.apis

run
=====
1. Try

        mvn compile assembly:single && java -jar target/Blog-0.0.1-SNAPSHOT-jar-with-dependencies.jar
2. See it fail
3. Add proper `nu.wasis.blog.util.PrivateConstants.java`
4. ...
5. Profit
