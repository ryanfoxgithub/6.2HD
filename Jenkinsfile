pipeline {
    agent any
    
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
                bat 'powershell.exe -Command "Compress-Archive -Path * -DestinationPath output.zip -Force"'
            }
        }
        stage('Deploy to Staging') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding', 
                    credentialsId: 'aws-credentials'
                ]]) {
                    bat "echo Deploying using AWS Access Key ID %AWS_ACCESS_KEY_ID%"
                    // Use AWS CLI or other AWS tools here
                }
            }
        }
        stage('Deploy to AWS CodeDeploy') {
            steps {
                script {
                    // Assuming you have configured AWS credentials in Jenkins credentials store
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials']]) {
                        bat """
                        aws deploy create-deployment \\
                        --application-name deployvuln \\
                        --deployment-config-name CodeDeployDefault.OneAtATime \\
                        --deployment-group-name deployvulngroup \\
                        --description 'Deploying my application' \\
                        --s3-location bucket=your-s3-bucket,key=builds/myapp.zip,bundleType=zip \\
                        --region ap-southeast-2
                        """
                    }
                }
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
