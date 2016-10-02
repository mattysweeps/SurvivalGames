#!/usr/bin/env bash

# Checkout branch. Create if it doesn't exist.
BRANCH_NAME=${PAGES_BRANCH_NAME}
if [ "$(git branch | grep -o ${BRANCH_NAME})" = "${BRANCH_NAME}" ]; then
    echo "Checking out branch ${BRANCH_NAME}"
    git checkout ${BRANCH_NAME}
else
    echo "Checking out new branch ${BRANCH_NAME}"
    git checkout -b ${BRANCH_NAME}
fi

# Remove existing docs
echo "Removing existing docs"
git rm -rf ./docs

# Add current docs
echo "Adding new docs"
mkdir ./docs
cp -rf ../build/docs/javadoc/* ./docs/
git add -f .
git commit -m "Latest javadoc on successful travis build ${TRAVIS_BUILD_NUMBER} auto-pushed to gh-pages"

# Push up
echo "Pushing up to origin"
git push -f origin gh-pages

