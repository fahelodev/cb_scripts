
pipeline 'New Bamboo API', {
  description = ''
  disableMultipleActiveRuns = '0'
  disableRestart = '0'
  enabled = '1'
  overrideWorkspace = '0'
  projectName = 'TSOFT'
  skipStageMode = 'ENABLED'

  formalParameter 'PlanKey', {
    expansionDeferred = '0'
    options = [
      'Develop': 'AOUSD0',
      'Master': 'AOUSD',
      'Release': 'AOUSD1',
      'AOCCN': 'AOCCN',
      'AOCC': 'AOCC',
      'AOCCC': 'AOCCC',
      'AOCIE': 'AOCIE',
      'AOCIEO': 'AOCIEO',
    ]
    orderIndex = '1'
    required = '1'
    type = 'select'
  }

  formalParameter 'ec_stagesToRun', {
    expansionDeferred = '1'
    required = '0'
  }

  stage 'Stage 1', {
    colorCode = '#00adee'
    completionType = 'auto'
    pipelineName = 'New Bamboo API'
    waitForPlannedStartDate = '0'

    gate 'PRE', {
      }

    gate 'POST', {
      }

    task 'Tarea 0 - Build Plan', {
      description = ''
      actualParameter = [
        'additionalBuildVariables': '',
        'config': 'Bamboo OnPremise',
        'customRevision': 'develop',
        'planKey': '$[PlanKey]',
        'projectKey': 'APIPRDO',
        'resultFormat': 'json',
        'resultPropertySheet': '/myJob/runResult',
        'waitForBuild': '1',
        'waitTimeout': '500',
      ]
      advancedMode = '0'
      allowOutOfOrderRun = '0'
      alwaysRun = '0'
      enabled = '1'
      errorHandling = 'stopOnError'
      insertRollingDeployManualStep = '0'
      resourceName = 'local'
      skippable = '0'
      stageSummaryParameters = '[{"name":"buildResultKey","label":"buildResultKey"},{"name":"buildUrl","label":"buildUrl"}]'
      subpluginKey = 'EC-Bamboo'
      subprocedure = 'RunPlan'
      taskType = 'PLUGIN'
      useApproverAcl = '0'
      waitForPlannedStartDate = '0'
    }

    task 'Tarea 1 - Print Build', {
      description = ''
      actualParameter = [
        'commandToRun': 'echo "$[/myStageRuntime/tasks["Tarea 0 - Build Plan"]/job/outputParameters/buildResultKey]"',
      ]
      advancedMode = '0'
      allowOutOfOrderRun = '0'
      alwaysRun = '0'
      enabled = '1'
      errorHandling = 'stopOnError'
      insertRollingDeployManualStep = '0'
      resourceName = 'local'
      skippable = '0'
      subpluginKey = 'EC-Core'
      subprocedure = 'RunCommand'
      taskType = 'COMMAND'
      useApproverAcl = '0'
      waitForPlannedStartDate = '0'
    }
  }

  // Custom properties

  property 'ec_counters', {

    // Custom properties
    pipelineCounter = '20'
  }

  property 'ec_customEditorData', {

    // Custom properties

    property 'parameters', {

      // Custom properties

      property 'PlanKey', {

        // Custom properties

        property 'options', {

          // Custom properties

          property 'option1', {

            // Custom properties

            property 'text', value: 'Develop', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOUSD0', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }

          property 'option2', {

            // Custom properties

            property 'text', value: 'Master', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOUSD', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }

          property 'option3', {

            // Custom properties

            property 'text', value: 'Release', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOUSD1', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }

          property 'option4', {

            // Custom properties

            property 'text', value: 'AOCCN', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOCCN', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }

          property 'option5', {

            // Custom properties

            property 'text', value: 'AOCC', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOCC', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }

          property 'option6', {

            // Custom properties

            property 'text', value: 'AOCCC', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOCCC', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }

          property 'option7', {

            // Custom properties

            property 'text', value: 'AOCIE', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOCIE', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }

          property 'option8', {

            // Custom properties

            property 'text', value: 'AOCIEO', {
              expandable = '1'
              suppressValueTracking = '0'
            }

            property 'value', value: 'AOCIEO', {
              expandable = '1'
              suppressValueTracking = '0'
            }
          }
          optionCount = '8'

          property 'type', value: 'list', {
            expandable = '1'
            suppressValueTracking = '0'
          }
        }
        formType = 'standard'
      }
    }
  }
}