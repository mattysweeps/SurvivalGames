#!/usr/bin/env bash

# Setup git
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

source ./scripts/variables.sh
source ./scripts/upload-javadoc.sh
source ./scripts/maven-release.sh
source ./scripts/plugin-release.sh
