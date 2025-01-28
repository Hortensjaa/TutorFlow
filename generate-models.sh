#!/bin/bash
echo "Generating backend models..."
quicktype -o backend/src/main/java/com/jk/TutorFlow/models/LessonModel.java --lang=java --package=com.jk.TutorFlow.models lesson.json
quicktype -o backend/src/main/java/com/jk/TutorFlow/models/UserModel.java --lang=java --package=com.jk.TutorFlow.models user.json


echo "Generating frontend models..."
quicktype -o frontend/src/models/lesson.ts --lang=typescript lesson.json
quicktype -o frontend/src/models/user.ts --lang=typescript user.json


