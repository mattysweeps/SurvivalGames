#!/usr/bin/env bash

# Checkout branch. Create if it doesn't exist.
BRANCH_NAME=${MAVEN_BRANCH_NAME}
if [ "$(git branch | grep -o ${BRANCH_NAME})" = "${BRANCH_NAME}" ]; then
    echo "Checking out branch ${BRANCH_NAME}"
    git checkout ${BRANCH_NAME}
else
    echo "Checking out new branch ${BRANCH_NAME}"
    git checkout -b ${BRANCH_NAME}
fi

rm -fr ./*
rm .travis.yml
git add -A

# Commit Maven build
echo "Adding maven build"
mkdir -p ${MAVEN_GROUP}
cp -rf ${HOME}/.m2/repository/${MAVEN_GROUP}/* ${MAVEN_GROUP}
git add -f .
git commit -m "Latest maven dependency on successful travis build ${TRAVIS_BUILD_NUMBER} auto-pushed to ${BRANCH_NAME}"

# Push up
echo "Pushing up to origin"
git push -f origin ${BRANCH_NAME}
