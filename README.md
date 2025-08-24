# 🔐 Password Strength Checker with HIBP

A cross-language cybersecurity project that checks the **strength** and **safety** of passwords using:
- ✅ **Python backend** for entropy calculation + [Have I Been Pwned API](https://haveibeenpwned.com/API/v3) lookup  
- ✅ **Java Swing GUI** frontend for user interaction  
- ✅ Rule-based warnings (length, uppercase, lowercase, numbers, special symbols)  
- ✅ JSON-based communication between Python ↔ Java  
- ✅ Advice + Breach status in a clean interface  

---

## 📂 Project Structure

```bash
Password_Checker_Project/
│
├── PasswordChecker.py            # Python script (entropy, HIBP, advice → JSON output)
├── PasswordCheckerUI.java        # Java Swing GUI (main UI)
├── lib/
│   └── json-20231013.jar         # JSON library for Java


---

## ⚙️ Features

- **Password Strength Analysis**
  - Entropy-based calculation (bits of randomness)
  - Classification: Weak, Medium, Strong, Very Strong
- **HIBP API Check**
  - Uses k-Anonymity lookup to check if password has appeared in known breaches
  - Password is never sent in full
- **Password Hygiene Warnings**
  - Warns if missing uppercase, lowercase, number, special symbol, or length < 8
- **Java Swing GUI**
  - Enter password securely
  - Toggle "Show/Hide" password
  - Displays results with **color-coded strength**
  - Breach status: ✅ safe / ⚠️ compromised
- **Unicode Support**
  - Displays ⚠️ and ✅ properly in advice panel
- **JSON Interface**
  - Python returns a JSON response that Java parses and displays in the GUI

---

## 📊 JSON Response Format

When you run:

```bash
python PasswordChecker.py My$ecurePass2025!

You get output like:
{
  "strength": "Strong",
  "entropy": 54.2,
  "advice": "✅ Good password hygiene.",
  "breached": false
}

Another example (compromised password):
{
  "strength": "Compromised",
  "entropy": 18.3,
  "advice": "⚠️ This password appeared in 21100 breaches. Do NOT use it.\n⚠️ Improvement Tips:\n- Should contain an uppercase letter\n- Should contain a special symbol",
  "breached": true
}

## 🚀 How to Run

### 1️⃣ Install Python dependencies
Make sure you have **Python 3.12** (or later) installed.  
Install the required module:

```bash
pip install requests

### 2️⃣ Compile Java GUI

From the project root:

```bash
javac -cp ".;lib/json-20231013.jar" PasswordCheckerUI.java

### 3️⃣ Run Java GUI

```bash
java -cp ".;lib/json-20231013.jar" PasswordCheckerUI

###4️⃣ Using the App

Enter your password
- Click Check
- View results:
  - Strength
  - Entropy score
  - Hygiene advice
  - Breach status

## 📊 Example Outputs

**Password:** 123456
**Strength:** Compromised
**Advice:** ⚠️ This password appeared in 23M breaches. Do NOT use it.
**Password:** My$ecurePass2025!
**Strength:** Strong
**Advice:** ✅ Good password hygiene.

##🔐 Security Notes

- This tool never stores passwords locally.
- Always use this responsibly; do not test real production credentials.

##🛠️ Tech Stack

- Python 3.12
- Java Swing (JDK 24)
- requests (Python)
- org.json (Java JSON library)

##📜 License

**MIT License** – feel free to fork, improve, and use responsibly.




