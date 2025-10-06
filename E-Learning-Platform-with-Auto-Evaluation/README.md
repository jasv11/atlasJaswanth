<div align="center">

# E-Learning Platform with AI Auto-Evaluation

<p>A full-stack web application that automatically evaluates coding assignments using AI</p>

[![Live Demo](https://img.shields.io/badge/Live-Demo-success?style=for-the-badge)](https://elearning.jaswanth.dev)
[![GitHub Actions](https://img.shields.io/badge/CI/CD-Automated-blue?style=for-the-badge)](https://github.com/jasv11/atlasJaswanth/actions)

</div>

---

## Overview

This platform allows teachers to create assignments and students to submit solutions that are automatically compiled, tested, and graded using AI. Built with Java backend, React frontend, and deployed on AWS infrastructure with full CI/CD automation.

<table>
<tr>
<td width="50%">

### What It Does

- Teachers create coding or text assignments
- Students submit their solutions
- System automatically compiles and tests code
- AI provides intelligent feedback and grading
- Real-time score tracking and analytics

</td>
<td width="50%">

### Technology Used

**Frontend:** React, TailwindCSS
**Backend:** Java 11, Jetty, Maven
**Database:** AWS DynamoDB
**Storage:** AWS S3
**AI:** AWS Bedrock (Claude Sonnet 3.5 V2)
**Infrastructure:** AWS EC2, Nginx, Let's Encrypt
**CI/CD:** GitHub Actions (Self-hosted runner)

</td>
</tr>
</table>

---

## Architecture

<table>
<tr>
<td width="33%" valign="top">

### Frontend
**React**

- Landing page with role selection
- Student dashboard for submissions
- Teacher dashboard for assignments
- Real-time score display
- Responsive design

**Build:** npm
**Deploy:** Nginx static files

</td>
<td width="33%" valign="top">

### Backend
**Java REST API**

- Student registration/auth
- Assignment management
- File upload to S3
- Code compilation & testing
- AI-powered evaluation
- Score management

**Port:** 8080
**Build:** Maven
**Deploy:** Jetty embedded server

</td>
<td width="33%" valign="top">

### Infrastructure
**AWS Cloud**

- EC2 instance hosting
- DynamoDB for data storage
- S3 for file storage
- Bedrock for AI grading
- Route 53 for DNS
- IAM roles for security

**SSL:** Let's Encrypt
**Domain:** elearning.jaswanth.dev

</td>
</tr>
</table>

---

## How We Built It

<details open>
<summary><b>Step 1: Backend Development</b></summary>

```bash
# Created Java servlets for API endpoints
- StudentServlet: Registration and login
- AdminServlet: Assignment creation
- StudentAssignmentServlet: Submission handling
- ScoreServlet: Score retrieval

# Implemented evaluation engines
- CodeEvaluator: Compiles and runs test cases
- AIEvaluator: Uses AWS Bedrock for intelligent grading
- AssignmentEvaluator: Manages evaluation workflow

# Integrated AWS services
- DynamoDB: Store students, assignments, scores, logs
- S3: Store assignment files and submissions
- Bedrock: AI-powered text evaluation
```

</details>

<details open>
<summary><b>Step 2: Frontend Development</b></summary>

```bash
# Built React components
- LandingPage: Modern UI with animations
- AuthModal: Student registration
- StudentDashboard: View assignments and submit
- TeacherDashboard: Create and manage assignments

# Styled with TailwindCSS
- Gradient backgrounds
- Smooth animations
- Responsive grid layouts
- Glass morphism effects
```

</details>

<details open>
<summary><b>Step 3: AWS Infrastructure Setup</b></summary>

```bash
# Provisioned EC2 instance
- Amazon Linux 2023
- t2.medium instance
- Security groups configured

# Configured DynamoDB tables
- Students (studentId as key)
- student-scores (assignmentId as key)
- Assignments (assignmentId as key)
- SystemLogs (logId as key)

# Setup S3 bucket
- elearning-assignments
- Organized folders for teachers and students
```

</details>

<details open>
<summary><b>Step 4: CI/CD Implementation</b></summary>

```bash
# Installed GitHub Actions self-hosted runner on EC2
sudo ./svc.sh install
sudo ./svc.sh start

# Created workflow (.github/workflows/deploy-elearning.yml)
- Backend: Maven build, JUnit tests
- Frontend: npm build, optimization
- Deploy: Direct file copy
- Health checks and auto-rollback

# Automatic deployment on git push to main
```

</details>

<details open>
<summary><b>Step 5: Domain & HTTPS Setup</b></summary>

```bash
# Configured DNS in Route 53
- Created A record: elearning.jaswanth.dev → EC2 IP

# Installed SSL certificate with Let's Encrypt
sudo certbot --nginx -d elearning.jaswanth.dev

# Configured Nginx reverse proxy
- Frontend: / → Static files
- Backend API: /api → localhost:8080
- Auto SSL redirect
```

</details>

---

## Deployment Details

<table>
<tr>
<td width="50%" valign="top">

### Production Environment

**Server:** AWS EC2 (us-east-1)
**OS:** Amazon Linux 2023
**Domain:** https://elearning.jaswanth.dev
**SSL:** Let's Encrypt (Auto-renewal)
**Web Server:** Nginx + Jetty

**Services Running:**
```bash
# Nginx - Frontend & reverse proxy
systemctl status nginx

# Jetty - Backend API server
systemctl status jetty

# GitHub Actions Runner
systemctl status actions.runner.*
```

</td>
<td width="50%" valign="top">

### CI/CD Pipeline

**Trigger:** Push to main branch
**Runner:** Self-hosted on EC2
**Build Time:** ~2 minutes

**Pipeline Steps:**
1. Checkout code
2. Run backend tests (JUnit)
3. Build backend JAR (Maven)
4. Build frontend bundle (npm)
5. Upload artifacts
6. Deploy backend (copy JAR, restart Jetty)
7. Deploy frontend (copy files, reload Nginx)
8. Health check verification
9. Auto-rollback on failure

</td>
</tr>
</table>

---

## Local Development

<table>
<tr>
<td width="50%" valign="top">

### Backend Setup

```bash
cd backend/

# Configure AWS credentials
cp .env.example .env
# Edit .env with your credentials

# Create DynamoDB tables
mvn exec:java -Dexec.mainClass="com.elearning.app.SetupDynamoDB"

# Run server
mvn clean package
java -jar target/elearning-platform-1.0.0.jar
```

Server: http://localhost:8080

[Full Backend Documentation →](./backend/README.md)

</td>
<td width="50%" valign="top">

### Frontend Setup

```bash
cd frontend/

# Install dependencies
npm install

# Start dev server
npm start
```

App: http://localhost:3000

**Build for production:**
```bash
npm run build
```

[Full Frontend Documentation →](./frontend/README.md)

</td>
</tr>
</table>

## Features Showcase

<table>
<tr>
<td width="25%">

**Auto Compilation**

Compiles Java code submissions and runs predefined test cases

</td>
<td width="25%">

**AI Grading**

Uses Claude AI to evaluate text answers with intelligent feedback

</td>
<td width="25%">

**Real-time Scores**

Instant feedback and score tracking in DynamoDB

</td>
<td width="25%">

**File Management**

Secure file upload and storage in S3 with presigned URLs

</td>
</tr>
</table>

---

## Security

- HTTPS with SSL certificate
- AWS IAM roles
- CORS configured for API security
- GitHub Secrets for sensitive data
- Self-hosted runner

---

<div align="center">

## Live Demo

**Visit:** [elearning.jaswanth.dev](https://elearning.jaswanth.dev)

Try it out:
- Register as a student
- View sample assignments
- Submit solutions
- Get instant feedback

---

**Built with ❤️ as part of Atlas Capstone Project**

</div>
