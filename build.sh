#!/usr/bin/env bash
# Builds a runnable JAR so Maple can be started with `java -jar maple.jar`.
set -euo pipefail

BUILD_DIR=build
JAR_FILE=maple.jar
MAIN_CLASS=maple.Maple

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

find src/main/java -name '*.java' -print0 | xargs -0 javac -d "$BUILD_DIR"
jar --create --file "$JAR_FILE" --main-class "$MAIN_CLASS" -C "$BUILD_DIR" .

echo "Built $JAR_FILE -- run it with: java -jar $JAR_FILE"
