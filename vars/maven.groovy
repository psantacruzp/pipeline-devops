/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
	if (params.Stage.contains('compile')) {
		stage('Compile') {
			STAGE = env.STAGE_NAME
	        bat "mvn clean compile -e"
	    }
	} else { println 'No ha especificado ejecutar el Stage: COMPILE' }
	
	if(params.Stage.contains('sonar')){
		stage('Sonar') {
			STAGE = env.STAGE_NAME
	        def scannerHome = tool 'sonar-scanner';
	        withSonarQubeEnv('sonar-server') {
	        bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven-developer -Dsonar.sources=src -Dsonar.java.binaries=build"
				}
		}
	} else { println 'No ha especificado ejecutar el Stage: SONAR' } 
			
	if(params.Stage.contains('test')){
		stage('Test') {
			STAGE = env.STAGE_NAME
	        bat "mvn clean test -e"
	    }
	} else { println 'No ha especificado ejecutar el Stage: TEST' }	
	          
	if(params.Stage.contains('package')){    
	    stage('Package') {
			STAGE = env.STAGE_NAME
	        bat "mvn clean package -e"
	    }
	} else { println 'No ha especificado ejecutar el Stage: PACKAGE' }
	
	if(params.Stage.contains('run')){		
	    stage('Run') {
			STAGE = env.STAGE_NAME
	        bat "start /min mvn spring-boot:run &"
	    }
	} else { println 'No ha especificado ejecutar el Stage: RUN' }

	if(params.Stage.contains('testApp')){		
	    stage('Test Applications') {
			STAGE = env.STAGE_NAME
	        bat "start chrome http://localhost:8081/rest/mscovid/test?msg=testing"
		}
	} else { println 'No ha especificado ejecutar el Stage: Test Applications' }
} 
return this;