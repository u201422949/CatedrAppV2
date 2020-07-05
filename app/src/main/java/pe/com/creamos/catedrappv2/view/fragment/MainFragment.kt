package pe.com.creamos.catedrappv2.view.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.fragment_main.*
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.R.layout.fragment_main
import pe.com.creamos.catedrappv2.databinding.FragmentMainBinding
import pe.com.creamos.catedrappv2.model.AdditionalInformation
import pe.com.creamos.catedrappv2.model.Question
import pe.com.creamos.catedrappv2.model.QuestionAndOptions
import pe.com.creamos.catedrappv2.util.*
import pe.com.creamos.catedrappv2.view.interfaces.InfoWindowListener
import pe.com.creamos.catedrappv2.view.node.AnswerNode
import pe.com.creamos.catedrappv2.view.node.InfoNode
import pe.com.creamos.catedrappv2.view.node.QuestionNode
import pe.com.creamos.catedrappv2.viewmodel.MainViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), InfoWindowListener, View.OnClickListener {

    private val TAG = MainFragment::class.java.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var dataBinding: FragmentMainBinding

    private val infoList: MutableList<AdditionalInformation>? = ArrayList()
    private val questionList: MutableList<QuestionAndOptions>? = ArrayList()
    private var goArFragment: ArFragment? = null
    private var infoNode: InfoNode? = null
    private var questionNode: QuestionNode? = null
    private var answerNode: AnswerNode? = null
    private var goImageGeneralNode: HashMap<AugmentedImage, AnchorNode> =
        HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            fragment_main,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuFab.setOnClickListener(this)
        logoutFab.setOnClickListener(this)
        photoFab.setOnClickListener(this)
        linearUserInfo.setOnClickListener(this)

        goArFragment = childFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        goArFragment?.arSceneView?.scene?.addOnUpdateListener(this::onUpdateFrame)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.refresh()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                dataBinding.user = it.user
                dataBinding.score = it.score
            }
        })

        viewModel.information.observe(viewLifecycleOwner, Observer { informationList ->
            informationList?.let {
                infoList?.clear()
                infoList?.addAll(it)
            }
        })

        viewModel.question.observe(viewLifecycleOwner, Observer { questList ->
            questList?.let {
                questionList?.clear()
                questionList?.addAll(it)
            }
        })

        viewModel.completedGoal.observe(viewLifecycleOwner, Observer { complete ->
            complete?.let {
                if (it.completed!!) {
                    val typeScore = TypeScore.COMPLETED_GOAL
                    typeScore.points = it.bonus!!
                    validateScore(typeScore, true)
                }
            }
        })

        viewModel.infoLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                //TODO: Mostrar mensaje error carga de datos y salir de aplicación
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                //TODO: Levantar loading
                Log.i(TAG, "Loading es: $it")
            }
        })
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun onUpdateFrame(frameTime: FrameTime) {
        val frame = goArFragment!!.arSceneView.arFrame ?: return

        Log.i(TAG, "frametime: $frameTime")

        val updatedAugmentedImages =
            frame.getUpdatedTrackables(
                AugmentedImage::class.java
            )

        for (augmentedImage in updatedAugmentedImages) {
            when (augmentedImage.trackingState) {
                TrackingState.PAUSED -> {
                    Log.i(
                        TAG,
                        "goArFragment: " + goArFragment!!.arSceneView.scene.children
                            .toString()
                    )
                    Log.i(TAG, "goImageGeneralNode: $goImageGeneralNode")

                    if (infoNode != null) {
                        goArFragment!!.arSceneView.scene.removeChild(infoNode)
                        goImageGeneralNode.clear()
                    } else if (questionNode != null) {
                        goArFragment!!.arSceneView.scene.removeChild(questionNode)
                        goImageGeneralNode.clear()
                    }
                }

                TrackingState.TRACKING -> {
                    if (!goImageGeneralNode.containsKey(augmentedImage)) {
                        when (val arElement = getKindOfArElement(augmentedImage)) {
                            is AdditionalInformation -> showAdditionalInformationModal(
                                augmentedImage,
                                arElement
                            )

                            is QuestionAndOptions -> showQuestionModal(
                                augmentedImage,
                                arElement
                            )
                        }
                    }
                }

                TrackingState.STOPPED -> goImageGeneralNode.remove(augmentedImage)
            }
        }
    }

    private fun getKindOfArElement(augmentedImage: AugmentedImage): Any? {
        augmentedImage.let {
            val filteredInfo = infoList!!.firstOrNull {
                it.idImage.equals(augmentedImage.name, true)
            }

            filteredInfo?.let {
                if (it.wasRead!!)
                    it.updateDate?.let { update ->
                        return if (validateDiffTime(update))
                            it
                        else null
                    }
                else
                    return it
            }

            val filteredQuestion =
                questionList!!.firstOrNull {
                    it.question.idImage.equals(augmentedImage.name, true)
                }

            filteredQuestion?.let {
                if (it.question.questionWasRead!!)
                    it.question.updateDate?.let { update ->
                        return if (validateDiffTime(update))
                            it
                        else null
                    }
                else
                    return it
            }
        }

        return null
    }

    private fun validateDiffTime(updateTime: LocalDateTime): Boolean {
        val now =
            LocalDateTime.now(ZoneOffset.of(ZONE_OFFSET)).toEpochSecond(ZoneOffset.of(ZONE_OFFSET))
        val registered = updateTime.toEpochSecond(ZoneOffset.of(ZONE_OFFSET))
        return (now - registered) > DIFFERENCE_TIME_SECONDS
    }

    private fun showAdditionalInformationModal(
        augmentedImage: AugmentedImage, information: AdditionalInformation
    ) {
        infoNode =
            InfoNode(context, information, this)
        infoNode?.setImage(augmentedImage)
        goImageGeneralNode[augmentedImage] = infoNode!!
        goArFragment!!.arSceneView.scene.addChild(infoNode)
    }

    private fun showQuestionModal(
        augmentedImage: AugmentedImage, questionAndOptions: QuestionAndOptions
    ) {
        questionNode =
            QuestionNode(context, questionAndOptions, this)
        questionNode?.setImage(augmentedImage)
        goImageGeneralNode[augmentedImage] = questionNode!!
        goArFragment!!.arSceneView.scene.addChild(questionNode)
    }

    private fun showAnswerModal(
        image: AugmentedImage, question: Question, answer: Boolean
    ) {
        answerNode =
            AnswerNode(context, question, answer, this)
        answerNode?.setImage(image)
        goImageGeneralNode[image] = answerNode!!

        goArFragment!!.arSceneView.scene.addChild(answerNode)
    }


    override fun onCloseClicked(node: Node, element: Any, typeScore: TypeScore, wasRead: Boolean) {
        node.let {
            viewModel.updateElementRead(element, typeScore)

            if (!wasRead) {
                validateScore(typeScore, false)
            }

            goArFragment!!.arSceneView.scene.removeChild(it)
            goImageGeneralNode.clear()
        }
    }

    private fun validateScore(typeScore: TypeScore, nullGoalsViewModel: Boolean) {
        viewModel.updateScoreAndChallenge(typeScore, nullGoalsViewModel)

        if (linearScore.visibility == View.VISIBLE)
            Handler().postDelayed({
                animateScore(typeScore)
            }, TIME_WAIT_FOR_HIDE_SCORE_VIEW)
        else
            animateScore(typeScore)
    }

    private fun animateScore(typeScore: TypeScore) {
        linearScore.let {
            dataBinding.typeScore = typeScore
            linearScore.visibility = View.VISIBLE
            linearScore.postDelayed({ linearScore.visibility = View.GONE }, TIME_HIDE_SCORE_VIEW)
        }
    }

    override fun onDueTimeWindow(
        node: Node,
        image: AugmentedImage,
        question: Question,
        answer: Boolean
    ) {
        onCloseClicked(node, question, TypeScore.QUESTION, true)
        showAnswerModal(image, question, answer)
    }

    override fun onClick(view: View?) {
        view?.let {
            when (view) {
                menuFab -> Navigation.findNavController(it)
                    .navigate(MainFragmentDirections.actionMainFragmentToMenuFragment())

                linearUserInfo -> Navigation.findNavController(it).navigate(
                    MainFragmentDirections.actionMainFragmentToUserDetailFragment(
                        viewModel.user.value?.user,
                        viewModel.user.value?.score
                    )
                )

                logoutFab -> showExitDialog(it)
                photoFab -> showSharingDialog()
            }
        }
    }

    private fun showExitDialog(view: View) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.modal_exit_confirm)

        val btnLogOut = dialog.findViewById(R.id.btnLogOut) as Button
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button

        btnLogOut.setOnClickListener {
            showRatingDialog(view)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun showRatingDialog(view: View) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.modal_exit_rating_reviews)

        val btnAssess = dialog.findViewById(R.id.btnAssess) as Button
        val edtMessage = dialog.findViewById(R.id.edtMessage) as EditText
        val ratingApp = dialog.findViewById(R.id.ratingApp) as AppCompatRatingBar

        btnAssess.setOnClickListener {
            viewModel.saveRatingOnRemote(ratingApp.rating.toInt(), edtMessage.text.toString())
            Navigation.findNavController(view)
                .navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSharingDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.modal_share)

        val btnAccept = dialog.findViewById(R.id.btnAccept) as Button

        btnAccept.setOnClickListener {
            viewModel.saveLog()
            dialog.dismiss()
        }

        dialog.show()
    }
}
