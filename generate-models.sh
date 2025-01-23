#!/bin/bash
echo "Generating backend models..."
quicktype -o backend/src/main/java/com/jk/TutorFlow/models/Lesson.java --lang=java --package=com.jk.TutorFlow.models lesson.json


echo "Generating frontend models..."
quicktype -o frontend/src/models/lesson.ts --lang=typescript lesson.json


