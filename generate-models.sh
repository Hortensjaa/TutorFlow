#!/bin/bash
echo "Generating backend models..."
quicktype -o backend/src/main/java/com/jk/TutorFlow/models/LessonModel.java --lang=java --package=com.jk.TutorFlow.models models/lesson.json
quicktype -o backend/src/main/java/com/jk/TutorFlow/models/UserModel.java --lang=java --package=com.jk.TutorFlow.models models/user.json
quicktype -o backend/src/main/java/com/jk/TutorFlow/models/StudentModel.java --lang=java --package=com.jk.TutorFlow.models models/student.json

echo "Generating frontend models..."
quicktype -o frontend/src/models/lesson.ts --lang=typescript models/lesson.json
quicktype -o frontend/src/models/user.ts --lang=typescript models/user.json
quicktype -o frontend/src/models/student.ts --lang=typescript models/student.json


