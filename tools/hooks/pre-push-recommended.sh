#!/bin/bash

./gradlew --quiet \
        ktlint \
        app:testDebugUnitTest \
        app:connectedDebugAndroidTest
