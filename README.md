# Real-Time Encrypted Chat Application

## Project Overview
This project is a secure, real-time messaging application built with Spring Boot. Designed to facilitate instant communication, the system ensures data privacy through robust end-to-end encryption protocols. The application serves as a complete solution, integrating a robust Java backend with a dynamic JSP-driven frontend, and prioritizes code reliability through comprehensive unit testing.

## Architecture and Core Features
The core communication layer relies on WebSockets to enable seamless, low-latency data transfer between users, eliminating the overhead of traditional HTTP polling. To guarantee absolute communication privacy, the system implements RSA end-to-end encryption, ensuring that message payloads remain fully encrypted from the sender to the recipient.

The architecture includes a secure user management module that handles registration, authentication, and login processes. The presentation layer utilizes Spring MVC and JavaServer Pages (JSP), alongside HTML and CSS, to deliver a responsive user interface. Furthermore, the backend business logic and encryption services are rigorously validated through automated unit tests using JUnit 5 and Mockito, demonstrating a strong commitment to maintainable and production-ready code.

## Technical Stack
* Backend Framework: Java, Spring Boot, Spring MVC
* Real-Time Communication: WebSockets
* Security & Cryptography: RSA End-to-End Encryption
* Database: Relational SQL Database / In-Memory H2
* Presentation Layer: JSP, HTML, CSS
* Testing Architecture: JUnit 5, Mockito

## Getting Started

### Prerequisites
* Java 17 or higher
* Maven or Gradle
* Configured SQL Database (or rely on the default H2 in-memory configuration)

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/andrada15/your-repo-name.git](https://github.com/andrada15/your-repo-name.git)
