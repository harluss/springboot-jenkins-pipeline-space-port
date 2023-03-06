pipeline {
    agent any

    tools {
       maven 'MVN_3_8_5'
       jdk 'JDK_17'
    }

    stages {
        stage('Build') {
            steps {
//                 sh 'mvn clean verify'
                  echo 'about to fail'
                  sh 'exit 1'
            }
            post {
                success {
                    echo 'test passed'
                }
                failure {
                    error 'test failed'
                }
            }
        }
        stage('KABOOM PR') {
            when {
                changeRequest()
            }
            steps {
                echo 'KABOOM!'
            }
            success {
                echo 'test passed'
            }
        }
        stage('SKADOOSH MAIN') {
            when {
                branch "main"
            }
            steps {
                echo 'SKADOOSH!'
            }
        }
    }
}
