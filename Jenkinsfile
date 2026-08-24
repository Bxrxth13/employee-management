pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'java'
    }

    environment {
        APP_NAME = 'employee-management'
        DOCKER_IMAGE = 'jana06/employee-management'
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
        timeout(time: 1, unit: 'HOURS')
        skipDefaultCheckout(true)
    }

    stages {

        stage('Checkout') {
            steps {
                echo '=== STAGE 1: Checking out source code from Git ==='
                checkout scm
            }
        }

        stage('Maven Clean') {
            steps {
                echo '=== STAGE 2: Cleaning previous build artifacts ==='
                bat 'mvn clean'
            }
        }

        stage('Compile') {
            steps {
                echo '=== STAGE 3: Compiling Java source code ==='
                bat 'mvn compile'
            }
        }

        stage('Unit Test') {
            steps {
                echo '=== STAGE 4: Running Maven unit tests ==='
                bat 'mvn test'
            }

            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }

                failure {
                    echo '====================================================='
                    echo 'UNIT TESTS FAILED!'
                    echo 'Docker build and deployment will NOT continue.'
                    echo '====================================================='
                }
            }
        }

        stage('Package JAR') {
            steps {
                echo '=== STAGE 5: Creating Spring Boot JAR ==='
                bat 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo '=== STAGE 6: Building Docker image ==='

                bat """
                    docker build ^
                    -t %DOCKER_IMAGE%:latest ^
                    -t %DOCKER_IMAGE%:%BUILD_NUMBER% .
                """
            }
        }

        stage('Docker Hub Login') {
            steps {
                echo '=== STAGE 7: Authenticating with Docker Hub ==='

                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS}",
                        usernameVariable: 'DOCKERHUB_USER',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    bat '''
                        echo %DOCKERHUB_TOKEN% | docker login -u %DOCKERHUB_USER% --password-stdin
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                echo '=== STAGE 8: Pushing Docker image to Docker Hub ==='

                bat '''
                    docker push %DOCKER_IMAGE%:latest
                    docker push %DOCKER_IMAGE%:%BUILD_NUMBER%
                '''
            }
        }
    }

    post {

        success {
            echo '====================================================='
            echo 'CI PIPELINE SUCCESSFUL'
            echo 'Maven build: PASSED'
            echo 'Unit tests: PASSED'
            echo 'JAR package: CREATED'
            echo 'Docker image: BUILT'
            echo 'Docker Hub push: COMPLETED'
            echo "Image: ${DOCKER_IMAGE}:latest"
            echo "Build image: ${DOCKER_IMAGE}:${BUILD_NUMBER}"
            echo '====================================================='
        }

        failure {
            echo '====================================================='
            echo 'PIPELINE FAILED'
            echo 'Later stages were stopped automatically.'
            echo 'Check the failed stage above.'
            echo '====================================================='
        }

        always {
            echo '=== Pipeline execution completed ==='
        }
    }
}
