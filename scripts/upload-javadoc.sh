#!/usr/bin/env bash

# Clone
BRANCH_NAME=${PAGES_BRANCH_NAME}
git clone --single-branch --branch ${BRANCH_NAME} https://${GH_TOKEN}@github.com/${USER}/${PROJECT} ${BRANCH_NAME}
cd ${BRANCH_NAME}

pwd

# Remove existing docs
echo "Removing existing docs"
rm -fr ./*
git add -A

# Add current docs
echo "Adding new docs"
mkdir ./docs
cp -rf ../build/docs/javadoc/* ./docs/
git add -f .
git commit -m "Latest javadoc on successful travis build ${TRAVIS_BUILD_NUMBER} auto-pushed to gh-pages"

# Push up
echo "Pushing up to origin"
git push -f origin gh-pages

cd ../
