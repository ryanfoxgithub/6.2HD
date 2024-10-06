pipeline {
    agent any
    
    environment {
        AZURE_CREDENTIALS = credentials('azure-service-principal') // ID from Jenkins credentials
        AZURE_APP_NAME = 'VulnerableWebApp' // Replace with your App Service name
        AZURE_RESOURCE_GROUP = 'YourResourceGroupName' // Replace with your Resource Group name
        AZURE_REGION = 'ap-southeast-2' // Replace with your region
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
                bat 'mvn test'
                junit 'target/surefire-reports/*.xml'
            }
            post {
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
        //stage('packaging'){
        //    steps {
        //        echo 'this is packaging'
        //        bat 'powershell.exe -Command "Compress-Archive -Path * -DestinationPath output.zip -Force"'
        //    }
        //}
        stage('Deploy to Azure App Service') {
            steps {
                withCredentials([string(credentialsId: 'azure-service-principal', variable: 'AZURE_CREDENTIALS')]) {
                    bat '''
                        REM Log in to Azure using Service Principal
                        az login --service-principal -u %AZURE_CLIENT_ID% -p %AZURE_CLIENT_SECRET% --tenant %AZURE_TENANT_ID%

                        REM Deploy the JAR to Azure App Service
                        az webapp deploy --resource-group %AZURE_RESOURCE_GROUP% --name %AZURE_APP_NAME% --src-path target\\VulnerableWebApp-0.0.1-SNAPSHOT.jar --type jar
                    '''
                }
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
