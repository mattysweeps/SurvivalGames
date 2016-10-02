#!/usr/bin/env bash

# Checkout branch. Create if it doesn't exist.
BRANCH_NAME=${MAVEN_BRANCH_NAME}
git clone --single-branch --branch ${BRANCH_NAME} https://${GH_TOKEN}@github.com/${USER}/${PROJECT} ${BRANCH_NAME}
cd ${BRANCH_NAME}

# Commit Maven build
echo "Adding maven build"
mkdir -p ${MAVEN_GROUP}
cp -rf ${HOME}/.m2/repository/${MAVEN_GROUP}/* ${MAVEN_GROUP}
git add -f .
git commit -m "Latest maven dependency on successful travis build ${TRAVIS_BUILD_NUMBER} auto-pushed to ${BRANCH_NAME}"

# Push up
echo "Pushing up to origin"
git push -f origin ${BRANCH_NAME}

cd ../
