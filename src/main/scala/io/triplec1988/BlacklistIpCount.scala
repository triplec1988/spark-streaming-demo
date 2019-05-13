package io.triplec1988

import io.triplec1988.json.IpJsonProtocol
import io.triplec1988.models.{IpAddress, IpMetadata, MaliciousIp}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import spray.json._

object BlacklistIpCount extends IpJsonProtocol {

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("BlacklistIpCount")
    val scc = new StreamingContext(sparkConf, Seconds(5))

    // Bootstrap the directory to stream writes from
    val stream = scc.textFileStream("file:///tmp/spark-streaming-demo/test-data/in")

    // When streaming text from the files in the dir we want to parse the JSON into our case class for better handling
    val jsonStream = stream.map { _.parseJson.convertTo[IpMetadata] }

    // Filter out successful requests
    val badRequests = jsonStream.filter(_.statusCode.value != 200)

    // Map the IpMetadata object to a tuple of IP Address and 1. This makes it easy to reduce by key (IP Address)
    val ipTuples = badRequests.map { meta => (meta.ip.value, 1) }

    // Get a DStream that is a stream of tuples of IP Address and Count. This lets us know how often a given IP made a
    // bad request with in the given stream interval
    val reducedTuples = ipTuples.reduceByKey(_ + _)

    // Some baseline filtering here. Don't want to output ips that make a reasonable number of bad requests
    val maliciousIps = reducedTuples.filter(_._2 > 3)
    val resultSet = maliciousIps.map { case (ip, _) => MaliciousIp(IpAddress(ip)).toJson.compactPrint }

    // Simple way to deal with the result set. Easy to have this publish to S3 rather than write out as a local file.
    // See Spark Streaming documentation:
    // https://spark.apache.org/docs/latest/streaming-programming-guide.html#output-operations-on-dstreams
    resultSet.saveAsTextFiles("/tmp/spark-streaming-demo/test-data/out/malicious-ips")

    scc.start()
    scc.awaitTermination()
  }
}
