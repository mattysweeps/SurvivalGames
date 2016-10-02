#!/usr/bin/env bash

# Checkout branch. Create if it doesn't exist.
BRANCH_NAME=${MAVEN_BRANCH_NAME}
if [ "$(git branch | grep -o ${BRANCH_NAME})" = "${BRANCH_NAME}" ]; then
  git checkout ${BRANCH_NAME}
else
  git checkout -b ${BRANCH_NAME}
fi

# Commit Maven build
mkdir -p ${MAVEN_GROUP}
cp -rf ${HOME}/.m2/repository/${MAVEN_GROUP}* ${MAVEN_GROUP}
git add -f .
git commit -m "Latest maven dependency on successful travis build ${TRAVIS_BUILD_NUMBER} auto-pushed to ${BRANCH_NAME}"

# Push up
git push -f origin ${BRANCH_NAME}
