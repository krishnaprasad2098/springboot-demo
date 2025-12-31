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
            when {
                anyOf {
                    changeRequest()
                    expression { env.BRANCH_NAME == 'dev' || env.BRANCH_NAME ==~ /feature-.*/ }
                }
            }
            steps {
                sh '''
                  mvn clean test
                '''
            }
        }
        stage('Build') {
            when {
                anyOf {
                    changeRequest()
                    expression { env.BRANCH_NAME == 'dev' || env.BRANCH_NAME ==~ /feature-.*/ }
                }
            }
            steps {
                sh '''
                  mvn clean package -DskipTests
                '''
            }
        }

        stage('Docker Build & Push') {
            when {
                allOf {
                    branch 'dev'
                    not { changeRequest() }
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
                    sh '''
                      echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                      docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} .
                      docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
                      docker logout
                    '''
                // bat '''
                //   docker version
                //   echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                //   docker build -t %DOCKER_REPO%:%IMAGE_TAG% .
                //   docker push %DOCKER_REPO%:%IMAGE_TAG%
                //   docker logout
                // '''
                }
                    writeFile file: 'image-tag.txt', text: IMAGE_TAG
                    archiveArtifacts artifacts: 'image-tag.txt', fingerprint: true
            }
        }

        stage('Deploy to DEV') {
            when {
                branch 'dev'
                not { changeRequest() }
            }
            environment {
                IMAGE_TAG = "springboot-demo-${env.BUILD_NUMBER}"
            }
            steps {
                withCredentials([file(credentialsId: 'KUBECONFIG', variable: 'KUBECONFIG')]) {
                    sh '''
                      kubectl apply -n springboot-demo-dev -f k8s/
                      kubectl set image deployment/springboot-app \
                        springboot-app=krishnaprasad367/springboot-demo:%IMAGE_TAG% -n springboot-demo-dev
                      kubectl rollout status deployment/springboot-app -n springboot-demo-dev
                    '''
                }
            }
        }

        stage('Deploy to PROD') {
            when {
                branch 'main'
                not { changeRequest() }
            }
            steps {
                copyArtifacts(
                    projectName: 'springboot-demo/dev',
                    selector: lastSuccessful(),
                    filter: 'image-tag.txt'
                )

                script {
                    env.IMAGE_TAG = readFile('image-tag.txt').trim()
                    echo "Promoting image from DEV: ${IMAGE_TAG}"
                }

                withCredentials([file(credentialsId: 'KUBECONFIG', variable: 'KUBECONFIG')]) {
                    sh '''
                      kubectl apply -n springboot-demo-prod -f k8s/
                      kubectl set image deployment/springboot-app \
                        springboot-app=krishnaprasad367/springboot-demo:${IMAGE_TAG} \
                        -n springboot-demo-prod
                      kubectl rollout status deployment/springboot-app -n springboot-demo-prod
                    '''
                }
            }
        }
    }
}
