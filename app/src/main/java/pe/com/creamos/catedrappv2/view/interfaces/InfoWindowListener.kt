package pe.com.creamos.catedrappv2.view.interfaces

import com.google.ar.core.AugmentedImage
import com.google.ar.sceneform.Node
import pe.com.creamos.catedrappv2.model.Question
import pe.com.creamos.catedrappv2.util.TypeScore

interface InfoWindowListener {

    fun onCloseClicked(node: Node, element: Any, typeScore: TypeScore, wasRead: Boolean)

    fun onDueTimeWindow(node: Node, image: AugmentedImage, question: Question, answer: Boolean)
}