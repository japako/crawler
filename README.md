# crawler


**Running Instructions:**

Clone current repository get into directory created and execute following steps.
This commands were written for Unix like OS. Small modifications are required for Windows users.
* `mvnw clean install` - this should download maven and do all necessery thing like compilation, testing etc.
* `java -jar target/buildit-crawler-0.0.1-SNAPSHOT.jar http://wiprodigital.com ./result.txt` - this should start application, start crawling wiprodigital.com and produce result.txt file in a current directory.

Please note that by default program will use default parameters:
`crawler.batch.size=10` - maximum number of links in given iteration

`crawler.pool.size=2` - maximum number of Threads processing links from given batch

`crawler.sleep.time=10000` - delay after each iteration - simple throttling mechanism to avoid crawler ban  

If you want to use a different setting please create a file `myconfig.properites` in project home directory and insert above settings into a file and tweak them! Shall you be happy with settings please start application now with folowing command:

`java -jar target/buildit-crawler-0.0.1-SNAPSHOT.jar http://wiprodigital.com/ ./result.txt --spring.config.location=file://path/to/myconfig.properties`


**Limitations of this solution**
* Crawler doesn't use multi-threading in the best way.
* There is no retry strategy for failing connections etc.
* There is just one sample of test. 


**Things to improve**
The important question is why do we want to improve. First of all the program can work as requested and therfore Acceptance Criteria could be met. However there arethings that should be improved by looking at the project from engenieering point fo view. List of potential improvements:
* Better test coverage (unit test, mock test etc.)
* Threading model could be better. Fork And Join approach from Java 7 could be used as well as streams from Java 8.
* Code is using to much spring but ot was done in such way to demonstrate some Spring knowledge ;)




