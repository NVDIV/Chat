# Chat Application

## Description

This is a simple chat application built using Java that consists of a client-server architecture. The server can handle multiple clients, allowing them to send and receive messages in real-time. The application demonstrates basic networking concepts using sockets and multithreading.

## Features

- Multi-client support
- Real-time messaging
- User-defined nicknames
- Graceful shutdown on user command

## Technologies Used

- Java
- Sockets
- Multithreading
- Spring Boot (for REST API and rectangle management)

## Getting Started

### Prerequisites

- JDK 11 or higher
- Maven (for Spring Boot application)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/chat-application.git
   cd chat-application
   ```
2. Compile:

   ``` bash
   mvn clean install
   ```
   
3. Run the chat server:

   ``` bash
   java -cp target/chat-application.jar org.example.ChatServer
   ```

4. Run the chat client in a new terminal:
   
   ``` bash
   java -cp target/chat-application.jar org.example.ChatClient
   ```

### Usage

Start the server and the client as described above.
Enter your nickname when prompted.
Start chatting with other users.
To exit the chat, type /quit.

## Contact
If you’d like to reach out, feel free to connect with me:
- [LinkedIn](https://www.linkedin.com/in/nadiia-rybak-5092b8336)
- [Email](mailto:nvdiv5@gmail.com)

Thanks for visiting!
