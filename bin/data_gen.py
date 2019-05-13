import json
import random

MALICIOUS_IPS = ["1.1.1.1", "2.2.2.2", "3.3.3.3"]
UNAUTHORIZED = 401


def generate_request_json():
    malicious = True if random.randint(1, 5) % 5 is 0 else False
    if malicious:
        print(json.dumps({"ip": pick_random_malicious_ip(), "status_code": UNAUTHORIZED}))
    else:
        random_unauthorized = True if random.randint(1, 10) % 10 is 0 else False
        status_code = UNAUTHORIZED if random_unauthorized else 200
        random_ip = f"{random.randint(0, 99)}.{random.randint(0, 99)}.{random.randint(0, 99)}.{random.randint(0, 99)}"
        print(json.dumps({"ip": random_ip, "status_code": status_code}))


def pick_random_malicious_ip():
    index = random.randint(0,2)
    return MALICIOUS_IPS[index]

def main():
    for x in range(100):
        generate_request_json()

if __name__ == "__main__":
    # execute only if run as a script
    main()
