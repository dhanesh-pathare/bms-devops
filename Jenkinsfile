pipeline {
    agent any

    environment {
        IMAGE_NAME = "dhaneshpathare/bms-devops"
        IMAGE_TAG  = "${BUILD_NUMBER}"
    }

    stages {

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Docker Login & Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                        docker push ${IMAGE_NAME}:latest
                        docker logout
                    '''

        stage('Deploy BMS') {
            steps {
                sh '''
                    echo "Deploying BMS application..."

                    docker rm -f bms-app-container || true

                    docker run -d \
                      --name bms-app-container \
                      -p 8081:8080 \
                      ${IMAGE_NAME}:${IMAGE_TAG}

                    echo "Waiting for BMS application..."

                    for i in {1..12}; do
                        if curl -fsS http://localhost:8081 >/dev/null; then
                            echo "BMS application is UP"
                            exit 0
                        fi
                        sleep 5
                    done

                    echo "BMS application health check FAILED"
                    docker logs bms-app-container --tail 50
                    exit 1
                '''
            }
        }                }
            }
        }
    }

    post {
        success {
            echo 'BMS CI/CD Pipeline completed successfully!'
        }

        failure {
            echo 'BMS CI/CD Pipeline failed!'
        }
    }
}
