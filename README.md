# Sporty Application

## Running the Application

To run the application, simply execute:

```
docker-compose up
```

The application will be available on port **8080**.

## Note on Tests

Some tests are missing because I ran out of time. The implementation took me more than 90 minutes as I am new to Kafka.

##  Design Choices
- **Docker**: The application is containerized using Docker to ensure consistency across different environments and simplify deployment.
- **Spring Boot**: Chosen for its ease of use in building RESTful APIs and microservices.
- **Kafka**: Used for handling asynchronous communication between services, which is essential for scalability and reliability.
- **WebClient**: Utilized for making non-blocking HTTP requests to other services, improving performance
- **ThreadPoolTaskScheduler**: Implemented to manage scheduled tasks efficiently.
- **ConcurrentHashMap**: Used for thread-safe operations on shared data structures to use it as in-memory cache.
- **Mock Api End point**: Developed a mock API endpoint to simulate external service interactions during development and testing.

## AI Assistance
- Used for suggesting solution for publishing events on 10 seconds. That is why the ConcurrentHashMap is ConcurrentHashMap<String, ScheduledFuture>.
  My original idea was to use ConcurrentHashMap<String> and in EventSchedulerService.java just add:
```java
@Scheduled(fixedRate = 10000)
public void fetchEventScores() {
    for (String eventId : scheduledTasks) {
        try {
            externalApiService.getResultsAndPublish(eventId);
        } catch (Exception e) {
            log.error("Error processing event {}: {}", eventId, e.getMessage());
        }
    }
}
```
- But this way some events would be published before the 10 seconds passed because this cron will be running in the background.
  It was the simplest way and then I used AI to suggest how to improve it.
- Used AI for the Kafka, this is my first time working with Kafka. Also used AI for creating the docker-compose file for the kafka setup. My previous experience is with GCP Pub/Sub and some RabitMQ experience.

## Future Improvements
- Implement missing tests to ensure code quality and reliability. New to kafka but I am sure there are test containers so we can write integration tests for the KafkaPublisherService.java
- Enhance error handling and logging for better maintainability. There are too much logs now on info level
- Improving the EventStatusService to make it more open for extension to handle more statuses in the future. 
