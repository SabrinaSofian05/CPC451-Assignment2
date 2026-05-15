# CPC451 - Assignment 2: Hadoop MapReduce Word Count

## 📌 Project Overview

This repository contains the implementation of a **Basic Word Count** program using the **Hadoop MapReduce** framework. The goal of this project is to process unstructured text data, perform word frequency analysis, and output the results in a formatted table .

The system is tested using **"Alice's Adventures in Wonderland"** by Lewis Carroll, sourced from Project Gutenberg.

---

## 🛠️ Tech Stack & Requirements

* 
**Operating System**: Ubuntu Linux (via VMware) 


* **Framework**: Apache Hadoop 3.3.6
* 
**Language**: Java (JDK 8) 


* **Build Tool**: Apache Maven
* **Dataset**: [Alice's Adventures in Wonderland](https://www.gutenberg.org/files/11/11-0.txt)

---

## 🚀 Execution Guideline

### 1. Environment Setup

Ensure your Hadoop services are running before starting the job:

```bash
start-dfs.sh
start-yarn.sh
# Verify services using 'jps'
jps

```

### 2. Project Compilation

Navigate to the project directory and build the JAR file using Maven:

```bash
mvn clean package

```

### 3. Data Ingestion

Download the dataset and upload it to the **Hadoop Distributed File System (HDFS)** :

```bash
# Download dataset
wget https://www.gutenberg.org/files/11/11-0.txt -O assignment_data.txt

# Upload to HDFS
hdfs dfs -mkdir -p /assignment/input
hdfs dfs -put assignment_data.txt /assignment/input/

```

### 4. Running the Job

Execute the MapReduce job using the generated JAR:

```bash
hadoop jar target/word-count-1.0.jar com.assignment.WordCount /assignment/input /assignment/output

```

### 5. Viewing Results

View the formatted table output:

```bash
hdfs dfs -cat /assignment/output/part-r-00000 | head -n 20

```

---

## 📊 MapReduce Flow & Optimization

The project follows the standard MapReduce lifecycle to handle large-scale data input efficiently:

1. 
**Mapper**: Tokenizes the input text, cleans punctuation via Regex, and emits `(word, 1)` pairs .


2. **Shuffle & Sort**: Hadoop automatically groups intermediate keys to ensure all instances of a word reach the same reducer.
3. 
**Reducer**: Aggregates the counts and applies a custom table-style formatting for the final output .



### Ecosystem Optimizations

* **Data Locality**: Map tasks are scheduled on nodes containing the data blocks to minimize network congestion.
* 
**Combiner (Optional)**: In high-volume scenarios, a Combiner can be utilized as a "Local Reducer" to minimize the data sent over the network during the shuffle phase .



---

## 📁 Repository Structure

```text
.
├── src
│   └── main
│       └── java
│           └── com
│               └── assignment
│                   └── WordCount.java  # Main Java Source Code
├── pom.xml                             # Maven Dependencies
├── assignment_data.txt                 # Input Dataset (Alice in Wonderland)
└── README.md                           # Documentation

```

---

### Pro-Tip for your GitHub:

Include a screenshot of your **YARN Web UI (localhost:8088)** showing the `SUCCEEDED` status of your application. This provides visual proof that your "Flow" was successfully executed by the Hadoop resource manager.
