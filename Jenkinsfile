pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'java'
    }

    environment {
    DOCKER_IMAGE = 'jana06/employee-management'
    DOCKER_HOME = 'C:\\Users\\janardhan\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin'
    PATH = "${DOCKER_HOME};${env.PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build and Test') {
            steps {
                bat 'mvn clean package'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE%:latest .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    bat '''
                        @echo off
                        echo %DOCKER_TOKEN%|docker login -u %DOCKER_USER% --password-stdin
                        if errorlevel 1 exit /b 1

                        docker push jana06/employee-management:latest
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'BUILD, TEST AND DOCKER PUSH SUCCESSFUL'
        }

        failure {
            echo 'PIPELINE FAILED - DOCKER DEPLOYMENT STOPPED'
        }
    }
}
