def projectName = "TSOFT"

getReleases(projectName).each{
  deleteRelease(projectName: projectName, releaseName: it.name)
}

getPipelines(projectName).each {
  deletePipeline(projectName: projectName, pipelineName: it.name) 
}

getProcedures(projectName).each {
  deleteProcedure(projectName: projectName, procedureName: it.name) 
}

deleteApplication(projectName).each {
  deleteApplication(projectName: projectName, applicationName: it.name) 
}

getEnvironments(projectName).each {
  deleteEnvironment(projectName: projectName, environmentName: it.name) 
}

deleteProject(projectName: projectName )  
