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
        stage('Deploy to Elastic Beanstalk') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding', 
                    credentialsId: 'aws-credentials'
                ]]) {
                bat '''
                SET AWS_ACCESS_KEY_ID=%AWS_ACCESS_KEY_ID%
                SET AWS_SECRET_ACCESS_KEY=%AWS_SECRET_ACCESS_KEY%
                SET AWS_DEFAULT_REGION=ap-southeast-2

                REM Upload JAR to S3
                aws s3 cp target\\VulnerableWebApp-0.0.1-SNAPSHOT.jar s3://elasticbeanstalk-ap-southeast-2-376129847649/VulnerableWebApp-%BUILD_NUMBER%.jar

                REM Create a new application version
                aws elasticbeanstalk create-application-version ^
                    --application-name WebServer ^
                    --version-label v%BUILD_NUMBER% ^
                    --source-bundle S3Bucket=elasticbeanstalk-ap-southeast-2-376129847649,S3Key=VulnerableWebApp-%BUILD_NUMBER%.jar ^
                    --region ap-southeast-2

                REM Update the environment to use the new version
                aws elasticbeanstalk update-environment ^
                    --environment-name WebServer-env ^
                    --version-label v%BUILD_NUMBER% ^
                    --region ap-southeast-2
                '''
                }
            }
        }
        stage('Deploy to AWS CodeDeploy') {
            steps {
                script {
                    // Assuming you have configured AWS credentials in Jenkins credentials store
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials']]) {
                        bat "aws deploy create-deployment " +
                            "--application-name deployvuln " +
                            "--deployment-config-name CodeDeployDefault.OneAtATime " +
                            "--deployment-group-name deployvulngroup " +
                            "--description 'Deploying' " +
                            "--s3-location bucket=elasticbeanstalk-ap-southeast-2-376129847649,key=builds/myapp.zip,bundleType=zip " +
                            "--region ap-southeast-2"
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
