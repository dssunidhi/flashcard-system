
Run Instructions
Step-by-Step:

    1. Install Java 17 and Maven
    2. Clone or copy the project folder as flashcard-system
    3. Navigate to the root folder and run:
        mvn spring-boot:run

API Endpoints:
    
    1.Add Flashcard : POST http://localhost:8080/flashcard
        Content-Type: application/json
        {
        "studentId": "stu001",
        "question": "What is Newton's Second Law?",
        "answer": "Force equals mass times acceleration"
        }
    
    2.Get Flashcards (Mixed Subjects)
        GET http://localhost:8080/get-subject?student_id=stu001&limit=5

    
