pipeline {
    agent {
        docker {
            image '3.8.7-eclipse-temurin-17-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean verify'
            }
        }
    }
}
