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
        stage('CI - Test (DEV)') {
            when {
                allOf {
                    branch 'dev'
                    not { changeRequest() }
                }
            }
            steps {
                bat '''
                  mvn clean test
                '''
            }
        }
        stage('CI - Build (DEV)') {
            when {
                allOf {
                    branch 'dev'
                    not { changeRequest() }
                }
            }
            steps {
                bat '''
                  mvn clean package -DskipTests
                '''
            }
        }

        /* =======================
           BUILD IMAGE – DEV
        ======================= */
        stage('Docker Build & Push - DEV') {
            when {
                allOf {
                    branch 'dev'
                    not { changeRequest() }
                }
            }
            environment {
                IMAGE_TAG = "springboot-demo-${BUILD_NUMBER}"
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

        /* =======================
           DEPLOY – DEV
        ======================= */
        stage('Deploy to DEV') {
            when {
                branch 'dev'
            }
            steps {
                echo 'Deploying DEV image to DEV environment'
            }
        }

        /* =======================
           DEPLOY – PROD (MAIN)
        ======================= */
        stage('Deploy to PROD') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying PROD image to PROD environment'
            // placeholder
            }
        }
    }
}
