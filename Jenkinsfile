pipeline {
    agent any

    environment {
        DOCKER_REPO = 'krishnaprasad367/springboot-demo'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Debug') {
            steps {
                echo "BRANCH_NAME = ${env.BRANCH_NAME}"
                echo "CHANGE_ID = ${env.CHANGE_ID}"
                echo "CHANGE_TARGET = ${env.CHANGE_TARGET}"
            }
        }
        stage('Test') {
            // when {
            //     allOf {
            //         branch 'dev'
            //         // not { changeRequest() }
            //         changeRequest()
            //     }
            // }
            steps {
                bat '''
                  mvn clean test
                '''
            }
        }
        stage('Build') {
            // when {
            //     allOf {
            //         branch 'dev'
            //         not { changeRequest() }
            //     }
            // }
            steps {
                bat '''
                  mvn clean package -DskipTests
                '''
            }
        }

        stage('Docker Build & Push') {
            when {
                allOf {
                    branch 'dev'
                    // changeRequest()
                    // not { changeRequest() }
                    expression { env.CHANGE_ID == null }
                }
            }
            environment {
                IMAGE_TAG = "springboot-demo-${env.BUILD_NUMBER}"
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
                      docker version
                      echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                      docker build -t %DOCKER_REPO%:%IMAGE_TAG% .
                      docker push %DOCKER_REPO%:%IMAGE_TAG%
                      docker logout
                    '''

                //   echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                //   docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} .
                //   docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
                //   docker logout
                }
            }
        }

        stage('Deploy to DEV') {
            when {
                branch 'dev'
                // not { changeRequest() }
                expression { env.CHANGE_ID == null }
            }
            environment {
                IMAGE_TAG = "springboot-demo-${env.BUILD_NUMBER}"
            }
            steps {
                withCredentials([file(credentialsId: 'KUBECONFIG', variable: 'KUBECONFIG')]) {
                    bat '''
                      kubectl config use-context springboot-demo-dev
                      kubectl apply -n springboot-demo-dev -f k8s/
                      kubectl set image deployment/springboot-app \
                        springboot-app=krishnaprasad367/springboot-demo:%IMAGE_TAG%  -n springboot-demo-dev
                      kubectl rollout status deployment/springboot-app -n springboot-demo-dev
                    '''
                }
            }
        }

        stage('Deploy to PROD') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying PROD image to PROD environment'
            // TODO: Deploy the app to prod environment
            }
        }
    }
}
