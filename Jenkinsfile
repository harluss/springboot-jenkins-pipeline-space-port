pipeline {
    agent any

    tools {
       maven 'MVN_3_8_5'
       jdk 'JDK_17'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean verify'
            }
        }
        stage('KABOOM') {
            steps {
                echo 'KABOOM!'
            }
        }
    }
}
