package pe.com.creamos.catedrappv2.view.node

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.ar.core.AugmentedImage
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.concurrent.CompletableFuture

/**
 * Node for rendering an augmented image. The image is framed by placing the virtual picture frame
 * at the corners of the augmented image trackable.
 */
class ChurchNode(context: Context?) :
    AnchorNode() {

    private var image: AugmentedImage? = null

    companion object {
        private const val TAG = "AugmentedImageNode"
        private var pope: CompletableFuture<ModelRenderable?>? = null
    }

    init {
        if (pope == null) {
            pope = ModelRenderable.builder()
                .setSource(context, Uri.parse("models/Church.sfb")).build()
        }
    }

    fun setImage(image: AugmentedImage) {
        this.image = image
        if (!pope!!.isDone) {
            CompletableFuture.allOf(pope)
                .thenAccept {
                    setImage(
                        image
                    )
                }
                .exceptionally { throwable: Throwable? ->
                    Log.e(TAG, "Exception loading", throwable)
                    null
                }
        }
        anchor = image.createAnchor(image.centerPose)
        val localPosition = Vector3()
        localPosition[0.0f, 0.0f] = 0.0f
        val cornerNode = Node()
        cornerNode.setParent(this)
        cornerNode.localPosition = localPosition
        cornerNode.localRotation = Quaternion(
            Vector3(
                -1.0f,
                0.0f,
                0.0f
            ), 90f
        )
        cornerNode.localScale = Vector3(0.2f, 0.2f, 0.2f)
        cornerNode.renderable = pope!!.getNow(null)
    }
}