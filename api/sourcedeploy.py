import sys
import os
import hashlib
import hmac
import base64
import requests
import time

# unix timestamp 설정
timestamp = int(time.time() * 1000)
timestamp = str(timestamp)

# Ncloud API Key 설정
ncloud_accesskey = os.environ['NCP_ACCESS_KEY']
ncloud_secretkey = os.environ['NCP_SECRET_KEY']

# 암호화 문자열 생성을 위한 기본값 설정
method = "POST"
space = " "
new_line = "\n"

# API 서버 정보
api_url = "https://vpcsourcepipeline.apigw.ntruss.com"

# API PATH - SourcePipeline를 실행시키는 API Path
api_path = "/api/v1" + os.environ['NCP_DO_SOURCEDEPLOY_PATH']

# hmac으로 암호화할 문자열 생성
message = method + space + api_path + new_line + timestamp + new_line + ncloud_accesskey
message = bytes(message, 'UTF-8')

# hmac_sha256 암호화
ncloud_secretkey = bytes(ncloud_secretkey, 'UTF-8')
signingKey = base64.b64encode(hmac.new(ncloud_secretkey, message, digestmod=hashlib.sha256).digest())

# http 호출 헤더값 설정
http_header = {
    'x-ncp-apigw-timestamp': timestamp,
    'x-ncp-iam-access-key': ncloud_accesskey,
    'x-ncp-apigw-signature-v2': signingKey
}

# api 호출
try:
    response = requests.post(api_url + api_path, headers=http_header)
    response.raise_for_status()  # HTTPError 발생 가능
    print (response.text)
except requests.exceptions.RequestException as e:
    print("error occurred: ", e)
    sys.exit(1)

