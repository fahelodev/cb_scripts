def projectName = "TSOFT"

project projectName, {
    procedure "Procedimiento", {
        formalParameter "bambooPlanKey", type: "entry", defaultValue: "MIINSB-BFFBCSB"
        formalParameter "environment", type: "entry", defaultValue: "Sandbox"

        formalOutputParameter 'Pais'

        step "Paso 1", shell: 'ec-groovy',command: '''@Grab(group="org.codehaus.groovy.modules.http-builder", module="http-builder", version="0.7.1")
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import groovy.json.JsonSlurper

def url = "http://152.139.147.195:8085/rest/api/latest/"
def headers = ["Authorization": "Basic c2VydmljZXVzZXI6UjRzQjNycnlwaTMj"]

def bambooPlanKey = "$[bambooPlanKey]"
def environment = "$[environment]"

println "bambooPlanKey: " + bambooPlanKey
println "environment: " + environment

bambooPlanKey = bambooPlanKey.split("-")

def projectKey = bambooPlanKey[0]
def planKey =  bambooPlanKey[1]

println projectKey
def client = new RESTClient( url )

response = client.get(path :"deploy/project/forPlan", query: ["planKey" : projectKey + "-" + planKey],  headers: headers)
def deploymentProjectId = response.responseData[0].id
//println "deploymentProjectId: " + deploymentProjectId

def Slurper = new JsonSlurper()
def json = response.responseData //'[{"id":1504837649,"name":"bff-mi-inversion-sandbox-dply","description":"bff-mi-inversion"},{"id":1504837645,"name":"ms-servicios-demogenerador-sandbox-dply","description":"ms-servicios-demogenerador"}]'
def obj = Slurper.parseText(json)

obj.each { v -> ectool setProperty '/myProcedure/Deployment' + v.name --value v.id }
'''
        }
        step "Paso 2", command : 'ectool setProperty /myPipeline/Deployment/Pais --value "Chile"'
        step "Paso 3", command : 'ectool setOutputParameter Pais "CHILE"'
    }

    pipeline "Pipeline", {
        formalParameter "bambooPlanKey", type: "entry", defaultValue: "MIINSB-BFFBCSB"
        formalParameter "environment", type: "entry", defaultValue: "Sandbox"

        stage "Stage 1", {
            task "Tarea 1", {
                taskType = 'PROCEDURE'
                subprocedure = 'Procedimiento'
                stageSummaryParameters = '[{"name":"Pais","label":"Pais"}]'
                actualParameter = [
                    'bambooPlanKey': '$[bambooPlanKey]',
                    'environment': '$[environment]',
                ]               
            }
            task "Tarea 2", {
                taskType = 'COMMAND'
                subprocedure = 'RunCommand'
                subpluginKey = 'EC-Core'               
                actualParameter = [
                    'commandToRun': 'echo $[/myStageRuntime/ec_summary/Pais]',
                ]       
            }

            task "Tarea 3A", {
                taskType = 'MANUAL'
                formalParameter 'Deployment', defaultValue: null, {
                    propertyReference = '$[/myPipeline/Deployment]'
                    required = '1'
                    type = 'select'
                }    
            }
        }
    }

    property 'Deployment', {
    }
}