pipeline {
    agent any

    environment {
        SPRING_ENV_FILE = 'api/.env'
        IIS_DEPLOY_DIR = 'C:\\inetpub\\wwwroot\\powerbi'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Create API .env') {
            steps {
                withCredentials([
                    string(credentialsId: 'db-url', variable: 'DB_URL'),
                    string(credentialsId: 'db-user', variable: 'DB_USERNAME'),
                    string(credentialsId: 'db-pass', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'jwt-secret', variable: 'JWT_SECRET'),
                    string(credentialsId: 'azure-client-id', variable: 'AZURE_CLIENT_ID'),
                    string(credentialsId: 'azure-client-secret', variable: 'AZURE_CLIENT_SECRET'),
                    string(credentialsId: 'azure-tenant-id', variable: 'AZURE_TENANT_ID'),
                    string(credentialsId: 'azure-redirect-url', variable: 'AZURE_REDIRECT_URL')
                ]) {
                    script {
                        writeFile file: "${SPRING_ENV_FILE}", text: """
                            DB_URL=${DB_URL}
                            DB_USERNAME=${DB_USERNAME}
                            DB_PASSWORD=${DB_PASSWORD}

                            JWT_SECRET=${JWT_SECRET}

                            AZURE_CLIENT_ID=${AZURE_CLIENT_ID}
                            AZURE_CLIENT_SECRET=${AZURE_CLIENT_SECRET}
                            AZURE_TENANT_ID=${AZURE_TENANT_ID}
                            AZURE_REDIRECT_URL=${AZURE_REDIRECT_URL}
                        """
                    }
                }
            }
        }


        stage('Build Spring Boot') {
            steps {
                dir('api') {
                    bat './gradlew clean build'
                }
            }
        }

        stage('Deploy Spring Boot API as Windows Service') {
            steps {
                script {
                    def nssmPath = 'C:\\Tools\\nssm\\nssm.exe'
                    def jarPath = 'C:\\jenkins\\workspace\\power-bi\\api\\build\\libs\\powerbi-api.jar' // or wherever Jenkins workspace is
                    def javaPath = "java"

                    bat """
                    IF EXIST "${nssmPath}" (
                        echo NSSM found.
                    ) ELSE (
                        echo NSSM not found at ${nssmPath}. Please install NSSM or add a download step.
                        exit /b 1
                    )

                    REM Stop and remove existing service if exists
                    ${nssmPath} stop PowerBiApiService
                    ${nssmPath} remove PowerBiApiService confirm

                    REM Install the new service
                    ${nssmPath} install PowerBiApiService "${javaPath}" -jar "${jarPath}"

                    REM Set working directory to folder of the jar
                    ${nssmPath} set PowerBiApiService AppDirectory "${jarPath.substring(0, jarPath.lastIndexOf('\\'))}"

                    REM Optional: redirect output to logs
                    ${nssmPath} set PowerBiApiService AppStdout "${jarPath}.out.log"
                    ${nssmPath} set PowerBiApiService AppStderr "${jarPath}.err.log"

                    REM Start the service
                    ${nssmPath} start PowerBiApiService
                    """
                }
            }
        }


        stage('Build React Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploy your artifacts here...'
                // Example:
                // sh 'scp api/target/app.jar user@your-server:/deploy/path/'
                // sh 'scp -r frontend/build user@your-server:/deploy/path/frontend/'
            }
        }

        stage('Cleanup .env') {
            steps {
                sh "rm -f ${SPRING_ENV_FILE}"
            }
        }
    }
}
