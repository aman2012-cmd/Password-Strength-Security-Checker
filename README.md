## 🔐 Password Strength & Security Checker

This is a modern desktop application for analyzing password strength and security. Built with Java Swing for the frontend and a Python backend for the core logic, this tool offers a comprehensive assessment of any password you enter. Its sleek, non-scrolling interface ensures all critical information is visible at a glance.

## ✨ Features at a Glance

**- Entropy Calculation:** The application calculates the password's entropy, providing a quantitative measure of its cryptographic strength in bits.  

**- Rule-Based Strength Analysis:** It checks for common best practices, such as a minimum length of 8 characters, and the inclusion of uppercase, lowercase, numbers, and special symbols.  

**- Have I Been Pwned (HIBP) Integration:** Using the HIBP API, the application hashes the first five characters of your password's SHA-1 hash to check for exposure in known data breaches, providing a count if it's found.  

**- Dynamic and Intuitive GUI:** The user interface is designed for clarity, with a non-resizable window, consistent spacing, and an animated color indicator that gives instant visual feedback on password strength.  

**- Decoupled Architecture:** The separation of the Java GUI and the Python logic makes it easy to update the security rules or API integrations in the Python script without needing to recompile the Java application.  


## ⚙️ How It Works
The application operates on a client-server model, but everything runs on your local machine.

**1. Frontend (Java):** The Java Swing GUI captures the password input.  

**2. Communication:** The Java application spawns a new process that runs the Python script, passing the password as a command-line argument.  

**3. Backend (Python):** The PasswordChecker.py script performs all the heavy lifting:  

 - It calculates the entropy of the password using logarithmic functions.
   
 - It validates the password against a set of predefined rules.
   
 - It securely queries the HIBP API to check for breaches without transmitting the full password.
   
**4. Data Exchange:** The Python script returns a JSON object containing the strength rating, entropy value, advice, and breach status.  

**5. GUI Update:** The Java application reads the JSON output and updates the GUI in real-time.  


## 🚀 Getting Started  

**Prerequisites**  

**Java Development Kit (JDK) 8 or higher:** The Java application requires the JDK to compile and run.  

**Python 3.x:** The core logic is written in Python.  

**requests Library:** The Python script needs this library to make API calls.  


**Installation**  

*1. Clone the Repository:*  

*2. Install Python Dependencies:*  

*3. Configure the Java Path:*  

Open NonScrollingPasswordCheckerUI.java and modify the ProcessBuilder line to point to the correct paths for your Python executable and the PasswordChecker.py script:
(Remember to use double backslashes \\ for file paths in Java strings.)  

*4. Run the Application:*  

Compile and run the Java file using your IDE or the command line.
