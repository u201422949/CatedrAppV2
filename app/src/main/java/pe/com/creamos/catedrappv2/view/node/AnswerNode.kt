/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pe.com.creamos.catedrappv2.view.node

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.ar.core.AugmentedImage
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import kotlinx.android.synthetic.main.modal_question_window.view.*
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.databinding.ModalQuestionWindowRightBinding
import pe.com.creamos.catedrappv2.databinding.ModalQuestionWindowWrongBinding
import pe.com.creamos.catedrappv2.model.Question
import pe.com.creamos.catedrappv2.view.interfaces.InfoWindowListener
import java.util.concurrent.CompletableFuture


class AnswerNode(
    context: Context?,
    question: Question?,
    answer: Boolean,
    infoWindowInterface: InfoWindowListener?
) : AnchorNode(), View.OnClickListener {

    private var image: AugmentedImage? = null
    private var view: View? = null
    private var goInfoWindowStage: CompletableFuture<ViewRenderable?>? = null
    private lateinit var infoWindowListener: InfoWindowListener
    private lateinit var dataBindingRight: ModalQuestionWindowRightBinding
    private lateinit var dataBindingWrong: ModalQuestionWindowWrongBinding

    companion object {
        private val TAG = AnswerNode::class.simpleName
    }

    init {
        if (goInfoWindowStage == null) {
            infoWindowInterface?.let {
                this.infoWindowListener = it
            }

            if (answer) {
                dataBindingRight = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.modal_question_window_right,
                    null,
                    false
                )

                dataBindingRight.question = question
                view = dataBindingRight.root
            } else {
                dataBindingWrong = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.modal_question_window_wrong,
                    null,
                    false
                )

                dataBindingWrong.question = question
                view = dataBindingWrong.root
            }

            view?.imgInfoClose?.setOnClickListener(this)
            goInfoWindowStage = ViewRenderable.builder().setView(context, view).build()
        }
    }

    fun setImage(image: AugmentedImage) {
        this.image = image

        // If any of the models are not loaded, then recurse when all are loaded.
        if (!goInfoWindowStage!!.isDone) {
            CompletableFuture.allOf(goInfoWindowStage)
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

        // Set the anchor based on the center of the image.
        anchor = image.createAnchor(image.centerPose)

        // Make the 4 corner nodes.
        val localPosition = Vector3()
        val baseNode = Node()
        localPosition[image.extentX, 0.0f] = image.extentZ
        baseNode.setParent(this)
        baseNode.localPosition = localPosition
        baseNode.localRotation = Quaternion(
            Vector3(
                -1.0f,
                0.0f,
                0.0f
            ), 90f
        )
        baseNode.localScale = Vector3(0.2f, 0.2f, 0.2f)
        baseNode.renderable = goInfoWindowStage!!.getNow(null)
        Log.i(TAG, "Info Window Location")
        Log.i(TAG, "x: " + localPosition.x)
        Log.i(TAG, "y: " + localPosition.y)
        Log.i(TAG, "z: " + localPosition.z)
        Log.i(TAG, "Info Window Rotation")
        Log.i(TAG, "x: " + baseNode.localRotation.x)
        Log.i(TAG, "y: " + baseNode.localRotation.y)
        Log.i(TAG, "z: " + baseNode.localRotation.z)
    }

    override fun onClick(v: View) {
        infoWindowListener.onCloseClicked(this)
    }
}