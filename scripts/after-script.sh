#!/usr/bin/env bash
./scripts/upload-javadoc.sh
./scripts/maven-release.sh
./scripts/plugin-release.sh

