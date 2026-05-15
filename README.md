# CPC451 - Assignment 2: Hadoop MapReduce Word Count

## 📌 Project Overview

This repository contains the implementation of a **Basic Word Count** program using the **Hadoop MapReduce** framework. The system processes unstructured text data, performs word frequency analysis, and outputs the results in a formatted table .

> **Important:** Before proceeding, ensure you have completed the environment configuration as specified in **Lab 1 (Single-Node Setup)** and **Lab 2 (Maven Integration)**.

---

## 🛠️ Tech Stack & Requirements

* 
**Operating System**: Ubuntu Linux (via VMware) 


* **Framework**: Apache Hadoop 3.3.6
* **Language**: Java (JDK 8)
* **Build Tool**: Apache Maven
* **Dataset**: [Alice's Adventures in Wonderland](https://www.gutenberg.org/files/11/11-0.txt)

---

## 🚀 Execution Guideline

### 1. Create the Project Directory

To keep the assignment separate from previous lab work, create a fresh workspace:

```bash
cd ~
mkdir -p Assignment2/src/main/java/com/assignment
cd Assignment2

```

### 2. Environment Setup

Start the core Hadoop services to enable the HDFS and YARN frameworks:

```bash
start-dfs.sh
start-yarn.sh
# Check that NameNode, DataNode, and ResourceManager are active
jps

```

### 3. Access the Web UI

Monitor your cluster and job progress via your browser inside the Ubuntu VM:

* **HDFS (NameNode Status)**: `http://localhost:9870`
* **YARN (Resource Manager)**: `http://localhost:8088`

### 4. Project Compilation

Build the JAR file using Maven to package your Java classes:

```bash
mvn clean package

```

### 5. Data Ingestion

Download the dataset and upload it to the **Hadoop Distributed File System (HDFS)** :

```bash
# Download dataset
wget https://www.gutenberg.org/files/11/11-0.txt -O assignment_data.txt

# Upload to HDFS
hdfs dfs -mkdir -p /assignment/input
hdfs dfs -put assignment_data.txt /assignment/input/

```

### 6. Running the Job

Execute the MapReduce job. The YARN UI will track this application in real-time:

```bash
hadoop jar target/word-count-1.0.jar com.assignment.WordCount /assignment/input /assignment/output

```

### 7. Viewing Results

Inspect the final formatted table output:

```bash
hdfs dfs -cat /assignment/output/part-r-00000 | head -n 20

```

---

## 📁 Repository Structure

```text
.
├── src
│   └── main
│       └── java
│           └── com
│               └── assignment
│                   └── WordCount.java  # Main Source Code with Table Formatting
├── pom.xml                             # Maven Dependencies (Hadoop 3.3.6)
├── assignment_data.txt                 # Input Dataset (Alice in Wonderland)
└── README.md                           # Documentation

```

---

## 📊 Methodology & Optimization

* 
**Mapper**: Tokenizes text and cleans data using Regex .


* 
**Reducer**: Performs final aggregation and generates a custom header and table-style output .


* **Optimization**: The ecosystem is optimized for large input through **Data Locality** and parallel processing across mapped blocks.
