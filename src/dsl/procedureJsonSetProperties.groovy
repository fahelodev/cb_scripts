import groovy.json.JsonSlurper
import com.electriccloud.client.groovy.ElectricFlow
ef = new ElectricFlow()
def startingpath='$[startingpath]'
def Slurper = new JsonSlurper()
def json = """
$[json]
""".stripIndent()
obj = Slurper.parseText(json)

void scan(org.apache.groovy.json.internal.LazyMap object, String path) {
  object.each { k, v ->
    scan(v, "${path}/${k}")
  }
}
void scan(java.util.ArrayList object, String path) {
  object.eachWithIndex { v, i ->
    scan(v, "${path}/${i}")
  }
}
void scan(object, String path) {
  println "${path} ${object}"
  ef.setProperty(propertyName: "${path}", value: "${object}")
}
scan(obj, startingpath)
