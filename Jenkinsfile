pipeline {
    agent any

    environment {
    APP_NAME = 'employee-management'
    DOCKER_IMAGE = 'employee-management:latest'
    CONTAINER_NAME = 'employee-app'
    SERVER_PORT = '8081'
}

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
        timeout(time: 1, unit: 'HOURS')
    }

    stages {
        // Stage 1: Source Code Checkout
        stage('Checkout') {
            steps {
                echo '=== STAGE 1: Checking out source code from Git ==='
                checkout scm
            }
        }

        // Stage 2: Clean Build Environment
        stage('Maven Clean') {
            steps {
                echo '=== STAGE 2: Cleaning target artifacts ==='
                sh 'mvn clean'
            }
        }

        // Stage 3: Compile Source Code
        stage('Compile') {
            steps {
                echo '=== STAGE 3: Compiling Java source files ==='
                sh 'mvn compile'
            }
        }

        // Stage 4: Unit Testing & Quality Gate (MUST STOP IF FAIL)
        stage('Unit Test') {
            steps {
                echo '=== STAGE 4: Running Unit Tests & Mockito suite ==='
                // Runs unit tests. If any test fails, exit code != 0, halting pipeline immediately.
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
                failure {
                    echo 'CRITICAL: Unit Tests Failed! Halting Jenkins Pipeline immediately. Docker build/deployment aborted.'
                }
            }
        }

        // Stage 5: Package Application JAR
        stage('Package') {
            steps {
                echo '=== STAGE 5: Packaging application into runnable JAR ==='
                sh 'mvn clean package -DskipTests'
            }
        }

        // Stage 6: Docker Build
        stage('Docker Build') {
            steps {
                echo '=== STAGE 6: Building Docker image ==='
                sh "docker build -t ${APP_NAME} ."
            }
        }

        // Stage 7: Stop Previous Container
        stage('Stop Previous Container') {
            steps {
                echo '=== STAGE 7: Stopping and removing existing running container if present ==='
                sh "docker stop ${CONTAINER_NAME} || true"
                sh "docker rm ${CONTAINER_NAME} || true"
            }
        }

        // Stage 8: Deploy Container
        stage('Deploy') {
            steps {
                echo '=== STAGE 8: Running new Docker container ==='
                sh "docker run -d --name ${CONTAINER_NAME} -p ${SERVER_PORT}:8080 ${APP_NAME}"
            }
        }
    }

    post {
        success {
            echo '====================================================='
            echo 'SUCCESS: Employee Management Deployment Completed!'
            echo "Access API at: http://localhost:${SERVER_PORT}/employees"
            echo "Swagger UI at: http://localhost:${SERVER_PORT}/swagger-ui.html"
            echo '====================================================='
        }
        failure {
            echo '====================================================='
            echo 'FAILURE: Jenkins Pipeline Execution Failed!'
            echo 'Check logs above for build or test errors.'
            echo '====================================================='
        }
    }
}
