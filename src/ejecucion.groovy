/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
	pipeline {
		agent any
		
		environment {
			STAGE = ''
		}
		
		parameters {
			choice choices: ['gradle', 'maven'], description: 'Indicar la herramienta de construccion.', name: 'buildTool'
		}

		stages {
			stage('Pipeline') {
				steps {
					script {
						println 'Pipeline'
						if (params.buildTool == 'gradle'){
							def ejecucion = load 'gradle.groovy'
							ejecucion.call()
						} else {
							def ejecucion = load 'maven.groovy'
							ejecucion.call()
						}
					}
				}
			}
		}
		post {
			always {
				slackSend channel: '#jenkins-ci', color: 'normal', message: "${username}" + ' ha ejecutado un Pipeline.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
				slackSend channel: '#jenkins-ci', color: 'normal', message: 'Job Name: ' + env.JOB_NAME + ', BuildTool: ' +  params.buildTool + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
			}
			success{
				slackSend channel: '#jenkins-ci', color: '#29AE4A', message: 'Ejecucion exitosa.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
			}
			failure {
				slackSend channel: '#jenkins-ci', color: '#EC4D34', message: 'Ejecucion Fallida en Stage: ' + "${STAGE}" + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
			}
		}
	}

}

return this;