import re
import math
import hashlib
import requests
import json
import sys

def calculate_entropy(password):
    """Calculate entropy of a password"""
    charsets = [r'[a-z]', r'[A-Z]', r'[0-9]', r'[^a-zA-Z0-9]']
    R = 0
    if re.search(charsets[0], password): R += 26
    if re.search(charsets[1], password): R += 26
    if re.search(charsets[2], password): R += 10
    if re.search(charsets[3], password): R += 32
    L = len(password)
    return math.log2(R ** L) if R > 0 else 0

def check_rules(password):
    """Check basic password rules"""
    warnings = []
    if len(password) < 8:
        warnings.append("Should be at least 8 characters long")
    if not re.search(r"[a-z]", password):
        warnings.append("Should contain a lowercase letter")
    if not re.search(r"[A-Z]", password):
        warnings.append("Should contain an uppercase letter")
    if not re.search(r"[0-9]", password):
        warnings.append("Should contain a number")
    if not re.search(r"[^a-zA-Z0-9]", password):
        warnings.append("Should contain a special symbol")
    return warnings

def check_pwned(password):
    """Check if the password has been exposed in breaches using HIBP API"""
    sha1 = hashlib.sha1(password.encode('utf-8')).hexdigest().upper()
    prefix, suffix = sha1[:5], sha1[5:]
    url = f"https://api.pwnedpasswords.com/range/{prefix}"

    try:
        response = requests.get(url, timeout=5)
        if response.status_code != 200:
            return False, "API Error"
    except Exception:
        return False, "Connection Error"

    hashes = (line.split(':') for line in response.text.splitlines())
    for h, count in hashes:
        if h == suffix:
            return True, int(count)
    return False, 0

def check_password_strength(password):
    """Evaluate password strength"""
    entropy = calculate_entropy(password)
    breached, count = check_pwned(password)
    rule_warnings = check_rules(password)

    advice = []
    if breached:
        advice.append(f"⚠️ This password appeared in {count} breaches. Do NOT use it.")
    if rule_warnings:
        advice.append("⚠️ Improvement Tips:\n- " + "\n- ".join(rule_warnings))
    if not breached and not rule_warnings:
        advice.append("✅ Good password hygiene.")

    if entropy < 28:
        strength = "Weak"
    elif entropy < 36:
        strength = "Medium"
    elif entropy < 60:
        strength = "Strong"
    else:
        strength = "Very Strong"

    if breached:
        strength = "Compromised"

    return {
        "strength": strength,
        "entropy": round(entropy, 2),
        "advice": "\n".join(advice),
        "breached": breached
    }

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(json.dumps({"error": "No password provided"}))
        sys.exit(1)

    password = sys.argv[1]
    result = check_password_strength(password)
    print(json.dumps(result))
