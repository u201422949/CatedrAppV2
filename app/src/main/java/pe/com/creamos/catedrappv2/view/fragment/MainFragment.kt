package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.ux.ArFragment
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.databinding.FragmentMainBinding
import pe.com.creamos.catedrappv2.model.AdditionalInformation
import pe.com.creamos.catedrappv2.view.interfaces.InfoWindowInterface
import pe.com.creamos.catedrappv2.view.node.ChurchNode
import pe.com.creamos.catedrappv2.view.node.InfoNode
import pe.com.creamos.catedrappv2.view.node.PopeNode
import pe.com.creamos.catedrappv2.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), InfoWindowInterface {

    private val TAG: String = MainFragment::class.java.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var dataBinding: FragmentMainBinding

    private var infoList: List<AdditionalInformation>? = ArrayList()
    private var goArFragment: ArFragment? = null
    private var infoNode: InfoNode? = null

    //private val user: User? = null
    private val giOpcion = 3
    private var goImageMapPope: HashMap<AugmentedImage, PopeNode> =
        HashMap<AugmentedImage, PopeNode>()
    private var goImageMapChurch: HashMap<AugmentedImage, ChurchNode> =
        HashMap<AugmentedImage, ChurchNode>()
    private var goImageMapInfo: HashMap<AugmentedImage, InfoNode> =
        HashMap<AugmentedImage, InfoNode>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                infoList = listOf<AdditionalInformation>(
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
        viewModel.information.observe(viewLifecycleOwner, Observer { informationList ->
            informationList?.let {
            }
        })

        viewModel.infoLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                infoList = listOf<AdditionalInformation>(
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
                            "goImageMapInfo: " + goImageMapInfo.toString()
                        )
                        goArFragment!!.arSceneView.scene.removeChild(infoNode)
                        goImageMapInfo.clear()
                    }
                TrackingState.TRACKING -> when (giOpcion) {
                    1 -> if (!goImageMapPope.containsKey(augmentedImage)) {
                        val popeNode = PopeNode(context)
                        popeNode.setImage(augmentedImage)
                        goImageMapPope.put(augmentedImage, popeNode)
                        goArFragment!!.arSceneView.scene.addChild(popeNode)
                    }
                    2 -> if (!goImageMapChurch.containsKey(augmentedImage)) {
                        val churchNode = ChurchNode(context)
                        churchNode.setImage(augmentedImage)
                        goImageMapChurch.put(augmentedImage, churchNode)
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
                                    InfoNode(context, info.title, info.description, this)
                                infoNode?.setImage(augmentedImage)
                                goImageMapInfo.put(augmentedImage, infoNode!!)
                                goArFragment!!.arSceneView.scene.addChild(infoNode)
                                Log.i(
                                    TAG,
                                    "goArFragment: " + goArFragment!!.arSceneView.scene
                                        .children.toString()
                                )
                                Log.i(
                                    TAG,
                                    "goImageMapInfo: " + goImageMapInfo.toString()
                                )
//                                    LocalStorage.saveStringSharedPreferences(
//                                        this@MainActivity,
//                                        info.getIdImage(),
//                                        info.getIdImage()
//                                    )
                                // animateScore(TypeScore.ADDITIONAL_INFORMATION)
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
                }
            }
        }
    }

    override fun onCloseClicked(node: Node) {
        TODO("Not yet implemented")
    }
}
