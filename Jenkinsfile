pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "krishnaprasad367/springboot-demo"
        IMAGE_TAG    = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Debug Info') {
            steps {
                echo "BRANCH_NAME = ${env.BRANCH_NAME}"
                echo "CHANGE_ID   = ${env.CHANGE_ID}"
                echo "CHANGE_TARGET = ${env.CHANGE_TARGET}"
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('CI - Test & Build') {
            when {
                anyOf {
                    branch 'dev'         // runs on dev branch
                    changeRequest()      // runs on PRs
                }
            }
            steps {
                bat '''
                  java -version
                  mvn -version
                  mvn clean test package
                '''
            }
        }

        stage('CD - Docker Build & Push') {
            when {
                allOf {
                    not { changeRequest() }   // skip PRs
                    anyOf {
                        branch 'dev'
                        branch 'main'
                    }
                }
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} .
                        docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
                        docker logout
                    '''
                }
            }
        }
    }
}
