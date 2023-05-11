
# iMedia24 Backend Challenge

This is a Spring Boot application for the iMedia24 backend challenge that provides product management functionalities.

## Prerequisites

- Java 11
- Docker
- Gradle

## Getting Started

To run the application, follow these steps:

1. **Clone the repository:**

   Use the following command to clone this repository to your local machine:

    ```bash
    git clone https://github.com/ayoubelmohammadi/imedia24-backend-challenge.git
    ```

2. **Build the application:**

   Navigate into the directory of the project and use Gradle to build the application:

    ```bash
    cd imedia24-backend-challenge
    ./gradlew clean build
    ```

3. **Build the Docker image:**

   Build the Docker image using the following command:

    ```bash
    docker build -t imedia24-backend-challenge .
    ```

4. **Run the Docker container:**

   Run the Docker container with the following command:

    ```bash
    docker run -p 8080:8080 imedia24-backend-challenge
    ```

Now, you should be able to access the application at `http://localhost:8080`.

## API Documentation

You can access the Swagger UI for API documentation at `http://localhost:8080/swagger-ui/`.

---

## Running Tests

You can run the tests with the following command:

```bash
./gradlew test
```

## Contributing

If you would like to contribute, please fork the repository and make changes as you'd like. Pull requests are warmly welcome.

## License

MIT

---
