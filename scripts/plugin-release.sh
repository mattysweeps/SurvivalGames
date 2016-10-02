#!/usr/bin/env bash

# Get version
VERSION=$(ls ${HOME}/.m2/repository/${MAVEN_GROUP}/${PROJECT} | sed 's/maven-metadata-local.xml//' | xargs)

# If the version is not a snapshot version.
if [ "$(echo ${VERSION} | grep -o SNAPHOT)" != "SNAPSHOT" ]; then

    # Create new release
    API_JSON="
        {
          \"tag_name\": \"v$VERSION\",
          \"target_commitish\": \"master\",
          \"name\": \"$PROJECT v$VERSION\",
          \"body\": \"Plugin release of version ${VERSION} from Travis build ${TRAVIS_BUILD_NUMBER}\",
          \"draft\": false,
          \"prerelease\": false
        }
    "

    RESPONSE=$(curl --data "$API_JSON" https://api.github.com/repos/${USER}/${PROJECT}/releases?access_token=${GH_TOKEN})
    ID=$(echo ${RESPONSE} | jq .id)

    # Upload assets to release
    JAR_NAME="${PROJECT}-${VERSION}.jar"
    curl --data "$(cat ../build/libs/${JAR_NAME})" https://uploads.github.com/repos/${USER}/${PROJECT}/releases/${ID}/assets?name=${JAR_NAME}&access_token=${GH_TOKEN}
fi
