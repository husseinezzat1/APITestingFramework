
# API Testing Framework

This framework developed in Java and integrated with Rest-Assured that allows to perform Restful API requests and create automation test cases


## Built with

- Java (Jdk 11)
- Rest-Assured v4.1.1
- Junit5 v5.9.2 (Jupiter)
- Maven
- Allure Reporting v2.10.0



## Cloning Locally

Make sure that : 
- Best Buy API Playground is cloned and started on your local machine refer to this link : https://github.com/BestBuy/api-playground
- Java and Maven installed
Clone the repository

```bash
  git clone https://github.com/husseinezzat1/APITestingFramework/
  cd APITestingFramework
```
    
## Running Tests


To run tests and generate the allure report, There're two ways :
- run the following commands

```bash
  mvn clean test
  mvn allure:serve
```

The allure report will be geenerated and opened automatically on your default browser

- Open the project using IDE such as : IntelliJ or Eclipse and then you can run the test classes under path src/test/java/tests

then to generate the allure report, open terminal and run the following commands

```bash
  mvn allure:serve
```


## Documentation

[Check this slides for more information](https://docs.google.com/presentation/d/1PvOHNynog_prZ0Sm2ksNRn3SApBETcKo5xbqO3DYOd4/edit?usp=sharing)

