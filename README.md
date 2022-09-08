# Prerequisites
- Java 17
- Gradle 7.5
- Docker

# Run the App

## Start a database
`docker-compose -f config/docker/local/docker-compose.yaml up -d`

## Launch the app
`./gradlew bootRun`

## Execute unit tests
`./gradlew test`

# App usage

## Scheduler
The languages percentages are fetched from GitHub regularly every day at 1 AM.

## Rest API
To get the languages percentages call the following endpoint:
`curl localhost:8080/api/language-percentage`

To fetch and save the languages percentages on demand manually, without having to wait for the scheduler, call the following endpoint:
`curl -X POST localhost:8080/api/language-percentage/reload-from-github`

Note: There is an API limit on GitHub endpoints, so repeating fetching manually might end with an error.
Calling it once per hour is fine in case you don't call it around 1 AM, the scheduled job would fail then :-) 
