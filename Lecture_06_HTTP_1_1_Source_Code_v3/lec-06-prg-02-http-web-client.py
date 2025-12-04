import requests

print("## HTTP client started.")

print("## GET request for http://localhost:8080/temp/")
http_request = requests.get('http://localhost:8080/temp/')
print("## GET response [start]")
print(http_request.text)
print("## GET response [end]")

print("## GET request for http://localhost:8080/?var1=9&var2=9")
http_request = requests.get('http://localhost:8080/?var1=9&var2=9')
print("## GET response [start]")
print(http_request.text)
print("## GET response [end]")

print("## POST request for http://localhost:8080/ with var1 is 9 and var2 is 9")
http_request = requests.post('http://localhost:8080', data={'var1':'9','var2':'9'})
print("## POST response [start]")
print(http_request.text)
print("## POST response [end]")

print("## HTTP client completed.")
