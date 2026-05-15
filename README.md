# CPC451 - Assignment 2: Hadoop MapReduce Word Count

## 📌 Project Overview

This repository contains the implementation of a **Basic Word Count** program using the **Hadoop MapReduce** framework. The system processes unstructured text data, performs word frequency analysis, and outputs the results in a formatted table .

> **Important:** Before proceeding, ensure you have completed the environment configuration as specified in **Lab 1 (Single-Node Setup)** and **Lab 2 (Maven Integration)**.

---

## 🛠️ Tech Stack & Requirements

* **Operating System**: Ubuntu Linux (via VMware) 
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

After the job completes, you can inspect the output stored in HDFS. Hadoop generates results in a part-file format (e.g., `part-r-00000`).

#### **View Full Results**

To see the entire word count table in your terminal:

```bash
hdfs dfs -cat /assignment/output/part-r-00000

```

#### **View Snippets (Head or Tail)**

If the dataset is large, use these commands to view only the beginning or the end of the results:

```bash
# View the first 20 lines (includes the table header)
hdfs dfs -cat /assignment/output/part-r-00000 | head -n 20

# View the last 20 lines
hdfs dfs -cat /assignment/output/part-r-00000 | tail -n 20

```

#### **Find the Most Frequent Words**

To fulfill the requirement of discussing the result, use this command to sort the table numerically and find the top 10 most frequent words in *Alice's Adventures in Wonderland*:

```bash
hdfs dfs -cat /assignment/output/part-r-00000 | sort -k3 -nr | head -n 10

```

* **`-k3`**: Sorts based on the third column (the frequency count).
* **`-nr`**: Sorts **n**umerically and in **r**everse order (highest to lowest).

---

### 💡 Pro-Tip for the Report
When you run the "Most Frequent" command, you will likely see that common English words (stop-words) like **"the"**, **"and"**, and **"to"** have the highest frequency. In your **Result and Discussion** section, you can mention that:

1. **Stop-word Filtering**: While the current implementation counts all words, a future optimization could involve a "Stop-word Filter" in the Mapper to ignore common words and focus on unique story elements.

2. **Frequency Analysis**: Discuss how the **Combiner** helps aggregate these highly frequent words locally on the Map node before they are sent over the network, which is a key ecosystem optimization for large-scale inputs.

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

* **Mapper**: Tokenizes text and cleans data using Regex .
* **Reducer**: Performs final aggregation and generates a custom header and table-style output .
* **Optimization**: The ecosystem is optimized for large input through **Data Locality** and parallel processing across mapped blocks.
