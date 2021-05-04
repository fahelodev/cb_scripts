@Grab(group="org.codehaus.groovy.modules.http-builder", module="http-builder", version="0.7.1")
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import groovy.json.JsonSlurper

/*
def url = "http://152.139.147.195:8085/rest/api/latest/"
def headers = ["Authorization": "Basic c2VydmljZXVzZXI6UjRzQjNycnlwaTMj"]

def bambooPlanKey = "$[bambooPlanKey]"
def environment = "$[environment]"

println "bambooPlanKey: " + bambooPlanKey
println "environment: " + environment

bambooPlanKey = bambooPlanKey.split("-")

def projectKey = bambooPlanKey[0]
def planKey =  bambooPlanKey[1]


def client = new RESTClient( url )
response = client.get(path :"deploy/project/forPlan", query: ["planKey" : projectKey + "-" + planKey],  headers: headers)
def deploymentProjectId = response.responseData[0].id
println "deploymentProjectId: " + deploymentProjectId*/

def Slurper = new JsonSlurper()
def json = '[{"id":1504837649,"name":"bff-mi-inversion-sandbox-dply","description":"bff-mi-inversion"},{"id":1504837645,"name":"ms-servicios-demogenerador-sandbox-dply","description":"ms-servicios-demogenerador"}]'
def obj = Slurper.parseText(json)

obj.each { v -> ectool setProperty $[path] + v.name --value v.id }

/*void scan(org.apache.groovy.json.internal.LazyMap object, String path) {
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
}

scan(obj, "/myPipeline/Deployment")*/