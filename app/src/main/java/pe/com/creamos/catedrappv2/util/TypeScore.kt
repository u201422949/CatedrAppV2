package pe.com.creamos.catedrappv2.util

enum class TypeScore(val nameScore: String, val description: String, var points: Int) {

    ADDITIONAL_INFORMATION("INFO","Puntos de información adicional", 10),
    QUESTION("QUESTION","", 0),
    QUESTION_RIGHT("QUESTION","Puntos de Pregunta Bien Respondida", 30),
    QUESTION_WRONG("QUESTION","Puntos de Pregunta Mal Respondida", 30),
    COMPLETED_GOAL("GOAL", "Objetivo completado", 0)
}