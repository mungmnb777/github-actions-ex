#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/runwithme.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# build 파일 복사
echo "[INFO]:[$TIME_NOW] > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행 (도커로)
echo "[INFO]:[$TIME_NOW] > $JAR_FILE 빌드 수행" >> $DEPLOY_LOG
docker build -t mungmnb777/runwithme $PROJECT_ROOT

# 컨테이너 실행
echo "[INFO]:[$TIME_NOW] > $JAR_FILE 도커 컨테이너 실행" >> $DEPLOY_LOG
docker run -d -p 80:8080 --name be-server mungmnb777/runwithme