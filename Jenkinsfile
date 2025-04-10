pipeline {
    agent any

    environment {
        SPRING_ENV_FILE = 'api\\build\\libs\\.env'
        REACT_ENV_FILE = 'power-bi-frontend\\.env'
        IIS_DEPLOY_DIR = 'C:\\inetpub\\wwwroot\\powerbi'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Stop and remove Spring Boot API service') {
            steps {
                script {
                    def nssmPath = 'C:\\Tools\\nssm\\nssm.exe'

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
                    """
                }
            }
        }

        stage('Test Spring Boot') {
            steps {
                dir('api') {
                    bat './gradlew test'
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

        stage('Inject API .env from Jenkins Secret File') {
            steps {
                withCredentials([file(credentialsId: '126d4149-50fe-49db-9e09-cfa6dd604c91', variable: 'ENV_FILE')]) {
                    bat """
                        copy /Y "%ENV_FILE%" "api\\build\\libs"
                    """
                }
            }
        }

        stage('Deploy Spring Boot API as Windows Service') {
            steps {
                script {
                    def nssmPath = 'C:\\Tools\\nssm\\nssm.exe'
                    def jarPath = 'C:\\Users\\SRVMTDDIGITS\\AppData\\Local\\Jenkins\\.jenkins\\workspace\\power-bi\\api\\build\\libs\\api-1.0.0.jar' // or wherever Jenkins workspace is
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
                    ${nssmPath} install PowerBiApiService ${javaPath} -jar "${jarPath}"

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
                withCredentials([file(credentialsId: '55fad737-deb4-4e6d-9639-20f105556553', variable: 'ENV_FILE')]) {
                    bat """
                        copy /Y "%ENV_FILE%" "power-bi-frontend"
                    """
                }
                dir('power-bi-frontend') {
                    bat 'npm ci'
                    bat 'npm run build'
                }
            }
        }

        stage('Test React Frontend') {
            steps {
                dir('power-bi-frontend') {
                    bat 'npx playwright test --worker 1'
                }
            }
        }


        stage('Move React distributin to IIS folder') {
            steps {
                dir ('power-bi-frontend') {
                    bat '''
                        echo 🚀 Deploy your artifacts here...
                        xcopy dist "C:\\Users\\SRVMTDDIGITS\\Documents\\PowerBi_frontend" /E /Y
                    '''
                }
                // Example:
                // sh 'scp api/target/app.jar user@your-server:/deploy/path/'
                // sh 'scp -r frontend/build user@your-server:/deploy/path/frontend/'
            }
        }

        stage('Cleanup .envs') {
            steps {
                bat "if exist ${SPRING_ENV_FILE} del /f /q ${SPRING_ENV_FILE}"
                bat "if exist ${REACT_ENV_FILE} del /f /q ${REACT_ENV_FILE}"
            }
        }
    }
}
