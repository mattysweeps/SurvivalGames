#!/usr/bin/env bash
bash ./scripts/upload-javadoc.sh
bash ./scripts/maven-release.sh
bash ./scripts/plugin-release.sh
