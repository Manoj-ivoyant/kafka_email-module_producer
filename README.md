# Kafka Email Module Producer

The Kafka Email Module Producer is a service designed to handle HTTP POST requests for sending emails. It accepts two types of request bodies—one for sending emails without attachments ('fromEmail', 'toEmail', 'subject', 'body') and another for sending emails with attachments ('fromEmail', 'toEmail', 'subject', 'body', 'attachment'). Upon receiving these POST requests, it processes the email data and stores it in a Cassandra cluster.

## Introduction

The Kafka Email Module Producer is a service dedicated to facilitating the sending of emails through HTTP POST requests. It validates, processes, and stores email data into a Cassandra cluster while concurrently publishing corresponding events to Kafka.

## Features

- Handles two types of HTTP POST requests for sending emails: with and without attachments.
- Stores email data in a Cassandra cluster for persistence.
- Publishes events to Kafka, creating separate topics for emails with and without attachments.
- Utilizes Docker Compose with configured Kafka and Zookeeper services for coordination among producers and consumers.

## HTTP Endpoints

### POST /send-email

Endpoint for sending emails without attachments.

Request body (JSON):
- `fromEmail`: Sender's email address.
- `toEmail`: Recipient's email address.
- `subject`: Email subject.
- `body`: Email body content.

### POST /send-email-with-attachment

Endpoint for sending emails with attachments.

Request body (JSON):
- `fromEmail`: Sender's email address.
- `toEmail`: Recipient's email address.
- `subject`: Email subject.
- `body`: Email body content.
- `attachment`: File attachment for the email.

## Setup and Configuration

### Docker Compose

Utilize the provided Docker Compose YAML file to set up Kafka and Zookeeper services for coordination. Ensure Zookeeper is running to manage coordination between producers and consumers.

### Configuration

- Adjust Kafka configuration parameters if needed for your environment.
- Check and modify the Cassandra connection settings within the producer code if required.

## How It Works

Upon receiving an HTTP POST request to send an email, the producer processes the request content. It validates and stores the email data into the configured Cassandra cluster for persistent storage. Simultaneously, it publishes an event to Kafka.

### Kafka Topics

- Two separate Kafka topics are created:
  - Topic for emails without attachments.
  - Topic for emails with attachments.

## Prerequisites

- Docker installed to run the provided Docker Compose file.
- Accessible and properly configured Cassandra cluster.
- HTTP client (e.g., Postman) to make HTTP POST requests to the service.

## Usage

1. Ensure Docker is running.
2. Start the Kafka and Zookeeper services using the provided Docker Compose YAML file.
3. Configure the Cassandra cluster connection settings within the producer.
4. Run the Kafka Email Module Producer service.
5. Use an HTTP client to send POST requests to `/send-email` and `/send-email-with-attachment` endpoints with appropriate JSON request bodies to send emails with and without attachments, respectively.
