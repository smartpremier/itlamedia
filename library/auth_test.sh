#!/bin/bash

# 1. 최초 인증 (0분)
HOST=localhost
PORT=9090
API_URL=$HOST:$PORT

TOKEN_RESPONSE=$(curl -X POST "http://$API_URL/api/auth/device?deviceId=IPTV-001")
ACCESS_TOKEN=$(echo $TOKEN_RESPONSE | jq -r .accessToken)
REFRESH_TOKEN=$(echo $TOKEN_RESPONSE | jq -r .refreshToken)

echo "ACCESS_TOKEN=$ACCESS_TOKEN"
echo "REFRESH_TOKEN=$REFRESH_TOKEN"

# 2. 2분 대기
total_time=120

echo "wait for $total_time seconds"
for ((i=0; i<total_time; i++)); do
	printf "%s" "."
    sleep 1
done

# 3. accessToken으로 보호된 리소스 접근 시도 (실패)
curl -H "Authorization: Bearer $ACCESS_TOKEN" http://$API_URL/api/protected-resource -vv

# 4. refreshToken으로 새로운 accessToken 발급 시도 (실패)
curl -X POST "http://$API_URL/api/auth/refresh?refreshToken=$REFRESH_TOKEN"

# 5. 재인증 필요
TOKEN_RESPONSE=$(curl -X POST "http://$API_URL/api/auth/device?deviceId=IPTV-001")
ACCESS_TOKEN=$(echo $TOKEN_RESPONSE | jq -r .accessToken)
REFRESH_TOKEN=$(echo $TOKEN_RESPONSE | jq -r .refreshToken)

echo "ACCESS_TOKEN=$ACCESS_TOKEN"
echo "REFRESH_TOKEN=$REFRESH_TOKEN"
