package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.fragment_main.*
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.R.layout.fragment_main
import pe.com.creamos.catedrappv2.databinding.FragmentMainBinding
import pe.com.creamos.catedrappv2.model.AdditionalInformation
import pe.com.creamos.catedrappv2.model.Option
import pe.com.creamos.catedrappv2.model.Question
import pe.com.creamos.catedrappv2.util.TypeScore
import pe.com.creamos.catedrappv2.view.interfaces.InfoWindowListener
import pe.com.creamos.catedrappv2.view.node.*
import pe.com.creamos.catedrappv2.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), InfoWindowListener, View.OnClickListener {

    private val TAG: String = MainFragment::class.java.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var dataBinding: FragmentMainBinding

    private var infoList: List<AdditionalInformation>? = ArrayList()
    private var questionList: List<Question>? = ArrayList()
    private var goArFragment: ArFragment? = null
    private var infoNode: InfoNode? = null
    private var questionNode: QuestionNode? = null
    private var answerNode: AnswerNode? = null
    private var answeredQuestion = false

    //private val user: User? = null
    private val giOption = 4
    private var goImageMapPope: HashMap<AugmentedImage, PopeNode> =
        HashMap()
    private var goImageMapChurch: HashMap<AugmentedImage, ChurchNode> =
        HashMap()
    private var goImageMapInfo: HashMap<AugmentedImage, InfoNode> =
        HashMap()
    private var goImageMapQuestion: HashMap<AugmentedImage, QuestionNode> =
        HashMap()
    private var goImageMapAnswer: HashMap<AugmentedImage, AnswerNode> =
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

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.refresh()
        observeViewModel()

        goArFragment = childFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        goArFragment?.arSceneView?.scene?.addOnUpdateListener(this::onUpdateFrame)
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                dataBinding.user = it
                dataBinding.score = it.score

                infoList = listOf(
                    AdditionalInformation(
                        "1",
                        "papa.jpg",
                        "Juan Pablo II",
                        "Descripción acerca de Juan Pablo Segundo"
                    ),
                    AdditionalInformation(
                        "2",
                        "virgen.png",
                        "Virgen de la puerta",
                        "Descripción acerca de Virgen de la puerta"
                    )
                )

                questionList = listOf(
                    Question(
                        "1",
                        "papa.jpg",
                        "¿Cómo se llama el primer Papa qué visitó el Perú?",
                        "Juan Pablo II fue el primer Papa que visitó el Perú en Febrero de 1985",
                        3,
                        listOf(
                            Option("1", "LUIS IV"),
                            Option("2", "CARLOS V"),
                            Option("3", "JUAN PABLO II"),
                            Option("4", "LUIS V")
                        )
                    ),
                    Question(
                        "2",
                        "virgen.png",
                        "¿Cómo se le conoce popularmente a Nuestra Señora de la Evangelización?",
                        "Juan Pablo II fue el primer papa que visitó el Perú en Febrero de 1985",
                        2,
                        listOf(
                            Option("1", "Santa Rosa de Lima"),
                            Option("2", "Patrona de facto del Perú"),
                            Option("3", "Virgen Mariana"),
                            Option("4", "Ninguna de las anteriores")
                        )
                    )
                )
            }
        })
        viewModel.information.observe(viewLifecycleOwner, Observer { informationList ->
            informationList?.let {
            }
        })

        viewModel.infoLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                infoList = listOf(
                    AdditionalInformation(
                        "1",
                        "papa.jpg",
                        "Juan Pablo II",
                        "Descripción acerca de Juan Pablo Segundo"
                    ),
                    AdditionalInformation(
                        "2",
                        "virgen.png",
                        "Virgen de la puerta",
                        "Descripción acerca de Virgen de la puerta"
                    )
                )
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {

            }
        })
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        val frame = goArFragment!!.arSceneView.arFrame ?: return

        val updatedAugmentedImages =
            frame.getUpdatedTrackables(
                AugmentedImage::class.java
            )
        for (augmentedImage in updatedAugmentedImages) {
            when (augmentedImage.trackingState) {
                TrackingState.PAUSED ->                     // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    /*String text = "Detected Image: " + augmentedImage.getIndex() + ", image name: " + augmentedImage.getName();

                    SnackbarHelper.getInstance().showMessage(this, text);*/
                    if (infoNode != null) {
                        Log.i(
                            TAG,
                            "goArFragment: " + goArFragment!!.arSceneView.scene.children
                                .toString()
                        )
                        Log.i(
                            TAG,
                            "goImageMapInfo: $goImageMapInfo"
                        )
                        goArFragment!!.arSceneView.scene.removeChild(infoNode)
                        goImageMapInfo.clear()
                    } else if (questionNode != null) {
                        Log.i(
                            TAG,
                            "goArFragment: " + goArFragment!!.arSceneView.scene.children
                                .toString()
                        )
                        Log.i(
                            TAG,
                            "goImageMapQuestion: $goImageMapQuestion"
                        )
                        goArFragment!!.arSceneView.scene.removeChild(questionNode)
                        goImageMapQuestion.clear()
                    }

                TrackingState.TRACKING -> when (giOption) {
                    1 -> if (!goImageMapPope.containsKey(augmentedImage)) {
                        val popeNode = PopeNode(context)
                        popeNode.setImage(augmentedImage)
                        goImageMapPope[augmentedImage] = popeNode
                        goArFragment!!.arSceneView.scene.addChild(popeNode)
                    }
                    2 -> if (!goImageMapChurch.containsKey(augmentedImage)) {
                        val churchNode = ChurchNode(context)
                        churchNode.setImage(augmentedImage)
                        goImageMapChurch[augmentedImage] = churchNode
                        goArFragment!!.arSceneView.scene.addChild(churchNode)
                    }
                    3 -> if (!goImageMapInfo.containsKey(augmentedImage)) {
                        for (info in infoList!!) {
                            if (augmentedImage.name == info.idImage) {
//                                if (LocalStorage.getStringFromSharedPreferences(
//                                        this@MainActivity,
//                                        info.getIdImage()
//                                    ).isEmpty()
//                                ) {
                                infoNode =
                                    InfoNode(context, info, this)
                                infoNode?.setImage(augmentedImage)
                                goImageMapInfo[augmentedImage] = infoNode!!
                                goArFragment!!.arSceneView.scene.addChild(infoNode)
                                Log.i(
                                    TAG,
                                    "goArFragment: " + goArFragment!!.arSceneView.scene
                                        .children.toString()
                                )
                                Log.i(
                                    TAG,
                                    "goImageMapInfo: $goImageMapInfo"
                                )
//                                    LocalStorage.saveStringSharedPreferences(
//                                        this@MainActivity,
//                                        info.getIdImage(),
//                                        info.getIdImage()
//                                    )
                                animateScore(TypeScore.ADDITIONAL_INFORMATION)
                                //Toast.makeText(MainActivity.this, "Se sumarán 10 puntos", Toast.LENGTH_LONG).show();
//                                } else {
//                                    // Toast.makeText(MainActivity.this, "No se sumarán puntos", Toast.LENGTH_LONG).show();
//                                }
                            }
                        }
                    }

                    4 -> if (!goImageMapQuestion.containsKey(augmentedImage)) {
                        for (question in questionList!!) {
                            if (augmentedImage.name == question.idImage) {
//                                if (LocalStorage.getStringFromSharedPreferences(
//                                        this@MainActivity,
//                                        info.getIdImage()
//                                    ).isEmpty()
//                                ) {
                                if (!answeredQuestion) {
                                    questionNode =
                                        QuestionNode(context, question, this)
                                    questionNode?.setImage(augmentedImage)
                                    goImageMapQuestion[augmentedImage] = questionNode!!
                                    goArFragment!!.arSceneView.scene.addChild(questionNode)
                                    Log.i(
                                        TAG,
                                        "goArFragment: " + goArFragment!!.arSceneView.scene
                                            .children.toString()
                                    )
                                    Log.i(
                                        TAG,
                                        "goImageMapQuestion: $goImageMapQuestion"
                                    )
//                                    LocalStorage.saveStringSharedPreferences(
//                                        this@MainActivity,
//                                        info.getIdImage(),
//                                        info.getIdImage()
//                                    )
                                    animateScore(TypeScore.ADDITIONAL_INFORMATION)
                                }
                                //Toast.makeText(MainActivity.this, "Se sumarán 10 puntos", Toast.LENGTH_LONG).show();
//                                } else {
//                                    // Toast.makeText(MainActivity.this, "No se sumarán puntos", Toast.LENGTH_LONG).show();
//                                }
                            }
                        }
                    }
                }
                TrackingState.STOPPED -> {
                    goImageMapPope.remove(augmentedImage)
                    goImageMapChurch.remove(augmentedImage)
                    goImageMapInfo.remove(augmentedImage)
                    goImageMapQuestion.remove(augmentedImage)
                }
            }
        }
    }

    private fun animateScore(typeScore: TypeScore) {
        dataBinding.typeScore = typeScore
        linearScore.visibility = View.VISIBLE

        val accumulatedScore = progressScore.progress + typeScore.points
        progressScore.progress = accumulatedScore
        linearScore.postDelayed({ linearScore.visibility = View.GONE }, 2000)
    }

    override fun onCloseClicked(node: Node) {
        node.let {
            goArFragment!!.arSceneView.scene.removeChild(it)
            goImageMapInfo.clear()
            goImageMapQuestion.clear()
            goImageMapAnswer.clear()
        }
    }

    override fun onDueTimeWindow(
        node: Node,
        image: AugmentedImage,
        question: Question,
        answer: Boolean
    ) {
        answeredQuestion = true
        onCloseClicked(node)

        Toast.makeText(context, "La respuesta es $answer", Toast.LENGTH_LONG).show()

        answerNode =
            AnswerNode(context, question, answer, this)
        answerNode?.setImage(image)
        goImageMapAnswer[image] = answerNode!!

        goArFragment!!.arSceneView.scene.addChild(answerNode)
        Log.i(
            TAG,
            "goArFragment: " + goArFragment!!.arSceneView.scene
                .children.toString()
        )
        Log.i(
            TAG,
            "goImageMapAnswer: $goImageMapAnswer"
        )
    }

    override fun onClick(v: View?) {
        v?.let {
            if (it == (menuFab)) {
                Navigation.findNavController(it)
                    .navigate(MainFragmentDirections.actionMainFragmentToMenuFragment())
            } else if (it == linearUserInfo) {
                Navigation.findNavController(it)
                    .navigate(
                        MainFragmentDirections.actionMainFragmentToUserDetailFragment()
                            .setUserArgs(viewModel.user.value)
                    )
            }
        }
    }
}
