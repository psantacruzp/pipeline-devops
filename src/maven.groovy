/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
    stage('Compile') {
		STAGE = env.STAGE_NAME
        bat "mvn clean compile -e"
    }
		
	stage('Sonar') {
		STAGE = env.STAGE_NAME
        def scannerHome = tool 'sonar-scanner';
        withSonarQubeEnv('sonar-server') {
        bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven-developer -Dsonar.sources=src -Dsonar.java.binaries=build"
			}
	}
          
    stage('Test') {
		STAGE = env.STAGE_NAME
        bat "mvn clean test -e"
    }
	
    stage('Package') {
		STAGE = env.STAGE_NAME
        bat "mvn clean package -e"
    }
		
    stage('Run') {
		STAGE = env.STAGE_NAME
        bat "start /min mvn spring-boot:run &"
    }
	
    stage('Test Applications') {
		STAGE = env.STAGE_NAME
        bat "start chrome http://localhost:8081/rest/mscovid/test?msg=testing"
	}
}

return this;