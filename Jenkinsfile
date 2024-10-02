pipeline {
    agent any

    environment {
        EB_APP_NAME = 'vulnelastic'
        EB_ENV_NAME = 'Vulnelastic-env'
        AWS_ACCESS_KEY_ID = credentials('aws-access-key')
        AWS_SECRET_ACCESS_KEY = credentials('aws-secret-key')
        REGION = 'us-east-1'
        APPLICATION_NAME = 'YourApplicationName'
        ENVIRONMENT_NAME = 'YourEnvironmentName'
        S3_BUCKET = 'your-s3-bucket-for-deployment'
        VERSION_LABEL = "v${BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo "Building Code..."
                bat 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                echo "Running Tests..."
                bat 'mvn -X test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/test-results/*.xml'
                }
                success {
                    echo "Tests Passed."
                }
                failure {
                    emailext to: 'mcubed132@gmail.com',
                        subject: "Unit and Integration Tests Failed",
                        body: "One or more tests have failed.",
                        attachLog: true, 
                        compressLog: true 
                }
            }
        }
        stage('Code Quality Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    bat 'mvn sonar:sonar'
                }
            }
        }
        stage('packaging'){
            steps {
                echo 'this is packaging'
                bat 'powershell.exe -Command "Compress-Archive -Path * -DestinationPath output.zip"'
            }
        }
        stage('Deploy to Staging') {
            steps {
                echo 'Deploying to AWS Elastic Beanstalk...'
                bat """
                aws s3 cp output.zip s3://${S3_BUCKET}/${VERSION_LABEL}.zip
                aws elasticbeanstalk create-application-version --application-name ${APPLICATION_NAME} \\
                    --version-label ${VERSION_LABEL} --source-bundle S3Bucket=${S3_BUCKET},S3Key=${VERSION_LABEL}.zip
                aws elasticbeanstalk update-environment --environment-name ${ENVIRONMENT_NAME} --version-label ${VERSION_LABEL}
                """
            }
        }
        stage('Release') {
            steps {
                echo "Releasing the application..."
                // Assuming release commands are added here
            }
        }
    }
}
