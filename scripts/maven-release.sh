#!/usr/bin/env bash
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --branch=repository https://${GH_TOKEN}@github.com/m0pt0pmatt/SpongeSurvivalGames repository

# Commit and Push the Changes
cd repository

DIR='io/github/m0pt0pmatt/survivalgames'
mkdir -p ${DIR}
cp -rf ${HOME}/.m2/repository/${DIR}* ${DIR}
git add -f .
git commit -m "Latest maven dependency on successful travis build ${TRAVIS_BUILD_NUMBER} auto-pushed to repository"
git push -f origin repository

