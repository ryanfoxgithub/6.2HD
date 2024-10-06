pipeline {
    agent any
    
    environment {
        AZURE_APP_NAME = 'VulnTest'            // Replace with your App Service name
        AZURE_RESOURCE_GROUP = 'WebStuff'      // Replace with your Resource Group name
        AZURE_SUBSCRIPTION_ID = '3ccc9a1e-ed72-4663-ad20-8295ba375c6f' // Optionally, store as a separate credential
        AZURE_TENANT_ID = 'd02378ec-1688-46d5-8540-1c28b5f470f6'             // Optionally, store as a separate credential
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
                // Bind Azure Service Principal credentials
                withCredentials([
                    usernamePassword(credentialsId: 'AzureServicePrincipal', passwordVariable: 'AZURE_CLIENT_SECRET', usernameVariable: 'AZURE_CLIENT_ID')
                ]) {
                bat '''
                    echo Logging in to Azure...
                    az login --service-principal -u %AZURE_CLIENT_ID% -p %AZURE_CLIENT_SECRET% --tenant %AZURE_TENANT_ID%
                    
                    echo Setting Azure subscription...
                    az account set --subscription %AZURE_SUBSCRIPTION_ID%
                    
                    echo Deploying WAR to Azure App Service...
                    az webapp deploy --resource-group %AZURE_RESOURCE_GROUP% --name %AZURE_APP_NAME% --src-path target\\VulnerableWebApp-0.0.1-SNAPSHOT.war --type war
                    
                    echo Deployment completed.
                '''
                }
            }
        }
        stage('Verify Deployment') {
            steps {
                script {
                    try {
                        def response = httpRequest 'https://vulntest-aehtatgbe6gggtgp.australiacentral-01.azurewebsites.net/'
                        echo "Response Status: ${response.status}"
                        if (response.status != 200) {
                            error "Application is not responding as expected. Status Code: ${response.status}"
                        } else {
                            echo "Deployment verified successfully."
                        }
                    } catch (Exception e) {
                        error "Failed to verify deployment: ${e.getMessage()}"
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
