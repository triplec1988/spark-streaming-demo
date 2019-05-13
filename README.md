# Spark Streaming Demo

This is a demo for Spark Streaming. While the options are vast with Spark Streaming this demo focuses on streaming text files from a local directory and writing the result set out as textfiles to a different local path. For more information on Spark Streaming and different use cases check out the Spark Streaming documentation here: https://spark.apache.org/docs/latest/streaming-programming-guide.html#a-quick-example

This demo focuses on batch based streaming with a polling interval of the source of five seconds. There are many different ways you can set up a Spark Streaming application, this is perhaps the simplest way making it ideal for a small, self-contained demo.

## Requirements

In order to run the demo application you'll need to ensure you have Spark installed. As of building this the current Spark version (2.4.3) still is using OpenJDK8 and Scala 2.11. Those will also need to be available as well as SBT.

```bash
brew install scala
brew install sbt@1
brew cask install homebrew/cask-versions/adoptopenjdk8
brew install apache-spark
```

Once Homebrew finishes doing its thing enter `spark-shell` in a terminal window just to verify that Spark is installed and running.

## Running the Demo

In order to run the demo you'll first need to compile the project and generate an artifact. This demo uses the SBT Assembly plugin. While not strictly necessary for Spark projects Assembly is nice because it generates a fat JAR with all the project dependencies. This eliminates the need to provide multiple JARs to Spark.

```bash
sbt clean compile assembly
```

The SBT `assembly` task will output the location of the JAR. You'll need this to submit the job to the Spark master. In a new terminal window you can submit the job to the Spark master through the `spark-submit` command. You can read more about the `spark-submit` options here: https://spark.apache.org/docs/latest/submitting-applications.html. The arguments passed in the demo are `--class` and `--master`.

```bash
spark-submit --class io.triplec1988.BlacklistIpCount --master local /{YOUR_PATH_TO}/spark-streaming-demo/target/scala-2.11/spark-streaming-demo-assembly-0.1.0-SNAPSHOT.jar
``` 

This will push the job to the Spark master and run it. Now the demo needs some data. In another new terminal window navigate back to the project root and run the data generation script:

```bash
./bin/generate-data.sh 
```

This generates some fake data and writes it out ever second to `/tmp/spark-streaking-demo/test-data/in/`. This is where the Spark job in the demo listens for new files. Result sets are then writen out to `/tmp/spark-streaming-demo/test-data/out/`. To verify that the demo worked cat a few of the the result files (part-0000):

```bash
cat /tmp/spark-streaming-demo/test-data/out/malicious-ips-1557773235000/part-00000

# {"value":"1.1.1.1"}
# {"value":"3.3.3.3"}
# {"value":"2.2.2.2"}
```

## Questions or Issues

If something isn't working quite right open an issue on Github and I'll take a look.