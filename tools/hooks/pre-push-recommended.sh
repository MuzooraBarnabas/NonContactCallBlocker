#!/bin/bash

./gradlew --quiet \
        app:testDebugUnitTest \
        app:connectedDebugAndroidTest
