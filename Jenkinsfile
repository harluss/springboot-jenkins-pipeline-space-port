pipeline {
    agent any

    tools {
       maven 'MAVEN'
       jdk 'JDK'
    }
    environment {
       FOO = 'bar'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout(time: 5, unit: 'MINUTES')
        disableConcurrentBuilds()
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
        stage('ENV TEST') {
            environment {
                IMAGE = readMavenPom().getArtifactId()
                VERSION = readMavenPom().getVersion()
            }
            steps {
                echo 'EVN TEST!'
                echo '${env.FOO} bar'
                echo '${FOO} bar 2'
                echo 'Docker image: ${IMAGE}, ver: ${VERSION}'
            }
        }
        stage('KABOOM PR') {
            when {
                changeRequest()
            }
            steps {
                echo 'KABOOM!'
            }
        }
        stage('SKADOOSH MAIN') {
            when {
                branch 'main'
            }
            steps {
                echo 'SKADOOSH!'
            }
        }
    }
    post {
        success {
            echo 'Build passed'
        }
        failure {
            error 'Build failed'
        }
    }
}
