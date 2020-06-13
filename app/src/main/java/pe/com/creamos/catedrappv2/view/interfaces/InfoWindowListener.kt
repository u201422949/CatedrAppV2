package pe.com.creamos.catedrappv2.view.interfaces

import com.google.ar.core.AugmentedImage
import com.google.ar.sceneform.Node
import pe.com.creamos.catedrappv2.model.Question

interface InfoWindowListener {

    fun onCloseClicked(node: Node)

    fun onDueTimeWindow(node: Node, image: AugmentedImage, question: Question, answer: Boolean)
}