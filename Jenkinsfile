pipeline {
    agent any

    tools {
       maven 'MAVEN'
       jdk 'JDK'
    }
    environment {
       APP_NAME = readMavenPom().getArtifactId()
//        APP_VERSION = readMavenPom().getVersion()
       DOCKERHUB_USER = 'harluss'
       SONAR_VER = '3.9.0.2155'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        disableConcurrentBuilds()
        timeout(time: 5, unit: 'MINUTES')
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean verify'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml, **/target/failsafe-reports/*.xml'
                }
            }
        }
        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv(installationName: 'SONARQUBE_SERVER') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_VER}:sonar'
                }
            }
        }
        stage('Build and Publish Docker Image') {
//             when {
//                 anyOf {
//                     changeRequest()
//                     branch 'main'
//                 }
//             }
            steps {
                def IMAGE_NAME = '${DOCKERHUB_USER}/${APP_NAME}'

                sh """
                  docker build -t ${IMAGE_NAME} .
                  docker push ${IMAGE_NAME}
                """
            }
        }
    }
//     post {
//         success {
//             echo 'Build passed'
//         }
//         failure {
//             echo 'Sending notifications...'
//             error 'Build failed'
//         }
//     }
}
