#!/usr/bin/env bash

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --branch=gh-pages https://${GH_TOKEN}@github.com/Dove-Bren/QuestManager gh-pages

# Commit and Push the Changes
cd gh-pages
git rm -rf ./docs
mkdir ./docs
cp -rf ../build/docs/javadoc/* ./docs/
git add -f .
git commit -m "Lastest javadoc on successful travis build ${TRAVIS_BUILD_NUMBER} auto-pushed to gh-pages"
git push -f origin gh-pages

