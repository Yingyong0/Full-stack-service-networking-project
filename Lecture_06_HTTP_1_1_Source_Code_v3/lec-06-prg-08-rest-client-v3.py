import requests

print("## REST API client started.")
print("## Make sure the REST server is running on http://127.0.0.1:5000")
print()

# Reads a non registered member : error-case
try:
    r = requests.get('http://127.0.0.1:5000/membership_api/0001')
    print("#1 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])
except (requests.exceptions.ConnectionError, requests.exceptions.MaxRetryError) as e:
    print("#1 ERROR: 无法连接到服务器！")
    print("   请确保REST服务器正在运行：")
    print("   1. 在另一个终端运行: python lec-06-prg-07-rest-server-v3.py")
    print("   2. 等待看到 'Running on http://127.0.0.1:5000' 提示")
    print("   3. 然后再运行此客户端程序")
    print(f"   详细错误: {str(e)}")
    exit(1)
except Exception as e:
    print("#1 ERROR:", str(e))

# Creates a new registered member : non-error case
try:
    r = requests.post('http://127.0.0.1:5000/membership_api/0001', data={'0001':'apple'})
    print("#2 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])
except (requests.exceptions.ConnectionError, requests.exceptions.MaxRetryError) as e:
    print("#2 ERROR: 连接失败 - 服务器未运行")
    exit(1)
except Exception as e:
    print("#2 ERROR:", str(e))

# Reads a registered member : non-error case
try:
    r = requests.get('http://127.0.0.1:5000/membership_api/0001')
    print("#3 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])
except Exception as e:
    print("#3 ERROR:", str(e))

# Creates an already registered member : error case
try:
    r = requests.post('http://127.0.0.1:5000/membership_api/0001', data={'0001':'xpple'})
    print("#4 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])
except Exception as e:
    print("#4 ERROR:", str(e))

# Updates a non registered member : error case
try:
    r = requests.put('http://127.0.0.1:5000/membership_api/0002', data={'0002':'xrange'})
    print("#5 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0002'])
except Exception as e:
    print("#5 ERROR:", str(e))

# Updates a registered member : non-error case
try:
    r = requests.post('http://127.0.0.1:5000/membership_api/0002', data={'0002':'xrange'})
    r = requests.put('http://127.0.0.1:5000/membership_api/0002', data={'0002':'orange'})
    print("#6 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0002'])
except Exception as e:
    print("#6 ERROR:", str(e))

# Delete a registered member : non-error case
try:
    r = requests.delete('http://127.0.0.1:5000/membership_api/0001')
    print("#7 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])
except Exception as e:
    print("#7 ERROR:", str(e))

# Delete a non registered member : non-error case
try:
    r = requests.delete('http://127.0.0.1:5000/membership_api/0001')
    print("#8 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])
except Exception as e:
    print("#8 ERROR:", str(e))

print()
print("## REST API client completed.")
