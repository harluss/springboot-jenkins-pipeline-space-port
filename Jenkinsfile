pipeline {
    agent any

    tools {
       maven 'MAVEN'
       jdk 'JDK'
    }
    environment {
       IMAGE_NAME = readMavenPom().getArtifactId()
       IMAGE_VERSION = readMavenPom().getVersion()
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
            environment {
                SONAR_VER = '3.9.0.2155'
            }
            steps {
                withSonarQubeEnv(installationName: 'SONARQUBE_SERVER') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_VER}:sonar'
                }
            }
        }
        stage('Build Docker Image') {
//             when {
//                 anyOf {
//                     changeRequest()
//                     branch 'main'
//                 }
//             }
            steps {
                echo '${IMAGE_NAME}:${IMAGE_VERSION}'
//                 sh 'docker build -t ${IMAGE_NAME} .'
                def dockerImage = docker.build('${IMAGE_NAME}:${IMAGE_VERSION}')
                dockerImage.push()
                dockerImage.push('latest')
            }
        }
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploy SKADOOSH!'
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
