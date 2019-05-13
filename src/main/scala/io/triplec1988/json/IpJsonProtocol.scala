package io.triplec1988.json

import io.triplec1988.models.{IpAddress, IpMetadata, MaliciousIp, StatusCode}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsString, JsValue, RootJsonFormat}

trait IpJsonProtocol extends DefaultJsonProtocol {
  implicit object IpAddressFormat extends RootJsonFormat[IpAddress] {
    override def write(obj: IpAddress): JsValue = JsString(obj.value)

    override def read(json: JsValue): IpAddress = json match {
      case JsString(ipAddress) => IpAddress(ipAddress)
      case _ => throw DeserializationException("String value expected")
    }
  }

  implicit object StatusCodeFormat extends RootJsonFormat[StatusCode] {
    override def write(obj: StatusCode): JsValue = JsNumber(obj.value)

    override def read(json: JsValue): StatusCode = json match {
      case JsNumber(statusCode) => StatusCode(statusCode.toInt)
      case _ => throw DeserializationException("Numerical value expected")
    }
  }

  implicit val ipFormat = jsonFormat(IpMetadata, "ip", "status_code")
  implicit val maliciousIpFormat = jsonFormat1(MaliciousIp)
}
