# BMS DevOps Project Documentation

## 1. Project Overview

**Project Name:** Building Management System (BMS)

**Project Type:** Java Application – DevOps CI/CD Implementation

**Cloud Platform:** AWS

**Deployment Platform:** Amazon EKS / Kubernetes

### Objective

Implement an end-to-end automated CI/CD pipeline for the BMS Java application.

### CI/CD Flow

```text
Developer
    ↓
GitHub
    ↓
GitHub Webhook
    ↓
Jenkins
    ↓
Maven Build
    ↓
Docker Build
    ↓
Docker Hub
    ↓
AWS EKS
    ↓
Kubernetes Deployment
    ↓
BMS Application
```

---

## 2. Technologies Used

| Technology     | Purpose                     |
| -------------- | --------------------------- |
| Linux / Ubuntu | DevOps environment          |
| Git            | Version control             |
| GitHub         | Source code management      |
| Jenkins        | CI/CD automation            |
| Maven          | Java application build      |
| Docker         | Containerization            |
| Docker Hub     | Container image registry    |
| AWS EC2        | Jenkins / DevOps server     |
| AWS EKS        | Kubernetes cluster          |
| Kubernetes     | Application deployment      |
| CoreDNS        | Kubernetes DNS              |
| kube-proxy     | Kubernetes networking       |
| NodePort       | Application external access |

---

## 3. GitHub Repository

Repository:

```text
git@github.com:dhanesh-pathare/bms-devops.git
```

Local project directory:

```bash
cd /home/ubuntu/bms-devops
```

Check Git status:

```bash
git status
```

Check remote:

```bash
git remote -v
```

---

## 4. DevOps Server

The DevOps environment was configured on an AWS EC2 Ubuntu server.

### Java

```bash
java -version
```

Java version:

```text
OpenJDK 21.0.12
```

### Git

```bash
git --version
```

### Docker

```bash
docker --version
```

### Maven

```bash
mvn -version
```

### Jenkins

Jenkins was configured on port:

```text
8080
```

Check Jenkins:

```bash
sudo systemctl status jenkins
```

---

## 5. GitHub SSH Authentication

A dedicated SSH key was configured for Jenkins.

SSH key:

```text
/var/lib/jenkins/.ssh/jenkins_bms
```

Test GitHub authentication:

```bash
sudo -u jenkins ssh \
-i /var/lib/jenkins/.ssh/jenkins_bms \
-o IdentitiesOnly=yes \
-T git@github.com
```

GitHub authentication was successfully verified.

---

## 6. Jenkins Configuration

### Jenkins Job

```text
BMS-DevOps-Pipeline
```

### GitHub Credential

```text
github-ssh-bms
```

### Docker Hub Credential

```text
dockerhub-credentials
```

Jenkins was added to the Docker group:

```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

Verify:

```bash
sudo -u jenkins docker ps
```

---

## 7. GitHub Webhook

GitHub Webhook was configured to automatically trigger Jenkins whenever code is pushed to the repository.

Webhook endpoint:

```text
http://<JENKINS-IP>:8080/github-webhook/
```

Jenkins trigger:

```text
GitHub hook trigger for GITScm polling
```

### Webhook Flow

```text
git push
    ↓
GitHub
    ↓
Webhook
    ↓
Jenkins
    ↓
Pipeline automatically starts
```

The webhook was successfully tested.

---

## 8. Maven Build

The BMS Java application was built using Maven.

Command:

```bash
mvn clean package -DskipTests
```

Generated JAR:

```text
target/bms-app-1.0.jar
```

Maven successfully generated the application artifact.

---

## 9. Docker Containerization

The application was containerized using Docker.

Docker base image:

```text
eclipse-temurin:21-jre
```

Build Docker image:

```bash
docker build -t dhaneshpathare/bms-devops:1 .
```

Check Docker images:

```bash
docker images
```

Run the application:

```bash
docker run -d \
--name bms-app-container \
-p 8081:8080 \
dhaneshpathare/bms-devops:1
```

Check container:

```bash
docker ps
```

Application response:

```text
BMS DevOps Application is Running!
```

---

## 10. Docker Hub

Docker Hub repository:

```text
dhaneshpathare/bms-devops
```

Jenkins creates an image tag using the Jenkins build number.

Example:

```text
dhaneshpathare/bms-devops:10
```

Latest image:

```text
dhaneshpathare/bms-devops:latest
```

### Docker Push Flow

```text
Docker Build
    ↓
Docker Login
    ↓
Docker Push
    ↓
Docker Hub
```

---

## 11. Jenkins CI/CD Pipeline

The Jenkins pipeline contains the following stages:

```text
1. Maven Build
2. Docker Build
3. Docker Login & Push
4. Deploy BMS
5. Application Health Check
```

### Maven Build

```bash
mvn clean package -DskipTests
```

### Docker Build

```bash
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
```

### Docker Push

```bash
docker push ${IMAGE_NAME}:${IMAGE_TAG}
docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
docker push ${IMAGE_NAME}:latest
```

### Deployment

The existing BMS container is removed before starting the new version:

```bash
docker rm -f bms-app-container || true
```

The application is then started using the new Docker image.

### Health Check

```bash
curl -fsS http://localhost:8081
```

Successful response:

```text
BMS DevOps Application is Running!
```

---

# 12. AWS EKS

The BMS application was deployed to Amazon EKS.

### EKS Cluster

```text
Cluster Name: bms-eks
Region: ap-south-1
Kubernetes Version: 1.34
Status: ACTIVE
```

Configure kubectl:

```bash
aws eks update-kubeconfig \
--region ap-south-1 \
--name bms-eks
```

Verify cluster:

```bash
kubectl get nodes
```

---

## 13. EKS Node Group

Node group:

```text
bms-nodes
```

Node type:

```text
t3.small
```

Configuration:

```text
Desired: 1
Minimum: 1
Maximum: 2
```

Check nodes:

```bash
kubectl get nodes
```

Expected status:

```text
Ready
```

---

# 14. Kubernetes Namespace

Namespace:

```text
bms
```

File:

```text
k8s/namespace.yaml
```

Apply:

```bash
kubectl apply -f k8s/namespace.yaml
```

Verify:

```bash
kubectl get namespace
```

---

# 15. Kubernetes Deployment

Deployment file:

```text
k8s/deployment.yaml
```

Deployment:

```text
bms-app
```

Replicas:

```text
2
```

Docker image:

```text
dhaneshpathare/bms-devops:latest
```

Container port:

```text
8080
```

Apply deployment:

```bash
kubectl apply -f k8s/deployment.yaml
```

Check pods:

```bash
kubectl get pods -n bms -o wide
```

Final status:

```text
2 BMS Pods → Running
```

---

# 16. Kubernetes Service

Service file:

```text
k8s/service.yaml
```

Service name:

```text
bms-service
```

Service type:

```text
NodePort
```

Application port:

```text
8080
```

NodePort:

```text
30081
```

Apply:

```bash
kubectl apply -f k8s/service.yaml
```

Check service:

```bash
kubectl get svc -n bms
```

Expected:

```text
8080:30081/TCP
```

---

# 17. Kubernetes Endpoints

Check application endpoints:

```bash
kubectl get endpoints -n bms
```

The BMS service successfully registered both application pods.

Example:

```text
192.168.x.x:8080
192.168.x.x:8080
```

Traffic can therefore be distributed between the two BMS pods.

---

# 18. CoreDNS

CoreDNS provides internal DNS resolution inside the Kubernetes cluster.

Check CoreDNS:

```bash
kubectl get pods -n kube-system
```

CoreDNS pods were successfully running.

### Internal DNS Test

```bash
kubectl run bms-test --rm -it \
--restart=Never \
--image=curlimages/curl:8.10.1 \
-- curl -v \
http://bms-service.bms.svc.cluster.local:8080
```

Successful response:

```text
HTTP/1.1 200
BMS DevOps Application is Running!
```

This confirmed:

```text
Kubernetes DNS
      ↓
Service
      ↓
BMS Pods
      ↓
Application
```

was working successfully.

---

# 19. kube-proxy

kube-proxy handles Kubernetes service networking and NodePort traffic.

Check kube-proxy:

```bash
kubectl get pods -n kube-system
```

kube-proxy status:

```text
Running
```

Important log messages included:

```text
Using iptables Proxier
Syncing iptables rules
SyncProxyRules complete
```

This confirmed kube-proxy was functioning correctly.

---

# 20. NodePort Testing

The NodePort was tested directly on the EKS worker node.

Command:

```bash
kubectl debug node/ip-192-168-60-109.ap-south-1.compute.internal \
-it \
--image=public.ecr.aws/amazonlinux/amazonlinux:2023 \
-- chroot /host \
curl -v \
http://192.168.60.109:30081
```

Successful response:

```text
HTTP/1.1 200
BMS DevOps Application is Running!
```

This confirmed the complete Kubernetes networking path:

```text
Worker Node
    ↓
NodePort 30081
    ↓
Kubernetes Service
    ↓
BMS Pod
    ↓
BMS Application
```

---

# 21. Final Architecture

```text
                         ┌──────────────┐
                         │    GitHub    │
                         └──────┬───────┘
                                │
                         GitHub Webhook
                                │
                                ▼
                         ┌──────────────┐
                         │   Jenkins    │
                         └──────┬───────┘
                                │
                           Maven Build
                                │
                                ▼
                         ┌──────────────┐
                         │    Docker    │
                         └──────┬───────┘
                                │
                           Docker Push
                                │
                                ▼
                         ┌──────────────┐
                         │  Docker Hub  │
                         └──────┬───────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │     AWS EKS     │
                       │                 │
                       │   Kubernetes    │
                       │                 │
                       │ ┌─────────────┐ │
                       │ │ BMS Pod 1   │ │
                       │ └─────────────┘ │
                       │ ┌─────────────┐ │
                       │ │ BMS Pod 2   │ │
                       │ └─────────────┘ │
                       │       ▲         │
                       │       │         │
                       │ BMS Service     │
                       │       ▲         │
                       │       │         │
                       │ NodePort 30081  │
                       └───────┬─────────┘
                               │
                               ▼
                       BMS Application
```

---

# 22. End-to-End CI/CD Flow

```text
Developer
    ↓
git push origin main
    ↓
GitHub
    ↓
GitHub Webhook
    ↓
Jenkins
    ↓
Maven Build
    ↓
Docker Build
    ↓
Docker Hub Push
    ↓
AWS EKS
    ↓
Kubernetes Deployment
    ↓
2 BMS Pods
    ↓
BMS Service
    ↓
NodePort 30081
    ↓
BMS Application
    ↓
Health Check
    ↓
HTTP 200
```

---

# 23. Final Validation

| Component                 | Status |
| ------------------------- | ------ |
| GitHub Repository         | ✅      |
| GitHub SSH Authentication | ✅      |
| GitHub Webhook            | ✅      |
| Jenkins                   | ✅      |
| Maven Build               | ✅      |
| Docker Build              | ✅      |
| Docker Hub Push           | ✅      |
| AWS EKS Cluster           | ✅      |
| EKS Node                  | ✅      |
| Kubernetes Namespace      | ✅      |
| Kubernetes Deployment     | ✅      |
| 2 BMS Pods                | ✅      |
| CoreDNS                   | ✅      |
| kube-proxy                | ✅      |
| Kubernetes Service        | ✅      |
| Service Endpoints         | ✅      |
| NodePort 30081            | ✅      |
| Application Health Check  | ✅      |
| HTTP 200 Response         | ✅      |

---

# 24. Final Application Result

```text
BMS DevOps Application is Running!
```

The BMS DevOps project successfully implemented an end-to-end CI/CD and Kubernetes deployment workflow.

---

# 25. Interview Project Summary

**Interview Answer:**

> I implemented an end-to-end CI/CD pipeline for a Java-based Building Management System application. I used GitHub for source code management and Jenkins for CI/CD automation. Maven was used to build the Java application, Docker was used for containerization, and Docker Hub was used as the image registry. For deployment, I used AWS EKS with Kubernetes. I created a Kubernetes Deployment with two replicas and exposed the application through a NodePort Service. I also configured GitHub Webhook for automatic Jenkins triggering and performed application health checks after deployment. The final application was successfully deployed and returned an HTTP 200 response.

---

# 26. Project Completion

```text
BMS DevOps Project
        ↓
     COMPLETED
        ↓
GitHub → Jenkins → Maven → Docker
        ↓
Docker Hub → AWS EKS → Kubernetes
        ↓
BMS Application
        ↓
HTTP 200 ✅
```

**Project Status: COMPLETED ✅**
