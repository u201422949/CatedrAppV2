package pe.com.creamos.catedrappv2.view.fragment

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import pe.com.creamos.catedrappv2.util.SnackbarHelper
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class AugmentedRealityFragment : ArFragment() {

    private val TAG = AugmentedRealityFragment::class.java.simpleName

    private val sampleImageDatabase = "data.imgdb"
    private val minOpenGlVersion = 3.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val openGlVersionString =
            (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (openGlVersionString.toDouble() < this.minOpenGlVersion) {
            Log.e(
                TAG, "Sceneform requires OpenGL ES 3.0 or later"
            )

            activity?.let {
                SnackbarHelper()
                    .showError(it, "Sceneform requires OpenGL ES 3.0 or later")
            }
        }
    }

    override fun getSessionConfiguration(session: Session): Config? {
        val config = super.getSessionConfiguration(session)
        if (!setupAugmentedImageDatabase(config, session)) {
            activity?.let {
                SnackbarHelper().showError(
                    it, "Could not setup augmented image database"
                )
            }
        }
        return config
    }

    private fun setupAugmentedImageDatabase(
        config: Config,
        session: Session
    ): Boolean {
        var augmentedImageDatabase: AugmentedImageDatabase? = null
        val assetManager =
            if (context != null) context!!.assets else null
        if (assetManager == null) {
            Log.e(
                TAG, "Context is null, cannot intitialize image database."
            )
            return false
        }
        try {
            context!!.assets.open(sampleImageDatabase)
                .use { stream ->
                    augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, stream)
                }
        } catch (e: IOException) {
            Log.e(
                TAG, "IO exception loading augmented image database.", e
            )
            return false
        }
        config.augmentedImageDatabase = augmentedImageDatabase
        return true
    }
}