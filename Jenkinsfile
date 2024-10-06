pipeline {
    agent any
    
    environment {
        // Azure Service Principal Credentials
        AZURE_CLIENT_ID = credentials('c48bc080-b763-4851-ad13-e2c11f1e1bb8')             // ID for client ID
        AZURE_CLIENT_SECRET = credentials('Wkw8Q~COYQp4rZlzH.AX7W-4DBJ6yaf1lKeezaEE')     // ID for client secret
        AZURE_TENANT_ID = credentials('d02378ec-1688-46d5-8540-1c28b5f470f6')             // ID for tenant ID
        AZURE_SUBSCRIPTION_ID = credentials('3ccc9a1e-ed72-4663-ad20-8295ba375c6f') // ID for subscription ID

        // Azure App Service Details
        AZURE_APP_NAME = 'VulnTest'            // Replace with your App Service name
        AZURE_RESOURCE_GROUP = 'WebStuff'   // Replace with your Resource Group name
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
                // Bind each credential separately
                withCredentials([
                    string(credentialsId: 'azure-client-id', variable: 'AZURE_CLIENT_ID'),
                    string(credentialsId: 'azure-client-secret', variable: 'AZURE_CLIENT_SECRET'),
                    string(credentialsId: 'azure-tenant-id', variable: 'AZURE_TENANT_ID')
                ]) {
                    bat '''
                        REM Log in to Azure using Service Principal
                        az login --service-principal -u %AZURE_CLIENT_ID% -p %AZURE_CLIENT_SECRET% --tenant %AZURE_TENANT_ID%

                        REM Deploy the JAR to Azure App Service
                        az webapp deploy --resource-group %AZURE_RESOURCE_GROUP% --name %AZURE_APP_NAME% --src-path target\\VulnerableWebApp-0.0.1-SNAPSHOT.jar --type jar
                    '''
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
