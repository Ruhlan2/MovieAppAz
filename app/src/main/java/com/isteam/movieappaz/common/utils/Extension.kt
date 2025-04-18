package com.isteam.movieappaz.common.utils

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.CreateUserLoadingLayoutBinding
import com.isteam.movieappaz.databinding.ForgotPassSuccessDialogLayoutBinding
import com.isteam.movieappaz.databinding.LayoutBaseLoadingBinding
import com.isteam.movieappaz.databinding.VerificationEmailLayoutBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.OutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun ImageView.loadImageUrl(
    url: String,
    size: Int = 1024
) {
    val options = RequestOptions()
        .override(size)
        .placeholder(placeHolder(context))
        .error(R.drawable.app_logo)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()

    Glide.with(this).setDefaultRequestOptions(options).load(url).into(this)
}


private fun placeHolder(context: Context): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 8f
    circularProgressDrawable.centerRadius = 40f
    circularProgressDrawable.setTint(context.getColor(R.color.red_21))
    circularProgressDrawable.start()
    return circularProgressDrawable
}

fun Activity.reset() {
    val packageManager: PackageManager = packageManager
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    val componentName = intent?.component
    val mainIntent: Intent = Intent.makeRestartActivityTask(componentName)
    this.startActivity(mainIntent)
    this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}


fun Activity.openMailApplication() {
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.google.com/"))
        startActivity(browserIntent)
    } catch (e: Exception) {
        Log.e("test", e.localizedMessage as String)
    }
}

fun Activity.changeLanguage(language: LanguageCode) {
    val configuration = resources.configuration
    val locale = Locale(language.code)
    Locale.setDefault(locale)
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun Activity.restartActivityInLanguage(language: LanguageCode) {
    changeLanguage(language)
    recreate()
}


fun Fragment.enableTransitionAnimation() {
    val anim =
        TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    sharedElementReturnTransition = anim
    sharedElementEnterTransition = anim
}

fun Activity.showMotionToast(
    title: String,
    message: String,
    style: MotionToastStyle = MotionToastStyle.INFO,
) {
    MotionToast.createColorToast(
        this,
        title,
        message,
        style,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(this, R.font.urbanist_600)
    )
}

fun Context.createUserProgressDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @DrawableRes icon: Int,
): Dialog {
    val dialog = Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog)
    val layout = CreateUserLoadingLayoutBinding.inflate(LayoutInflater.from(this))
    dialog.setContentView(layout.root)
    dialog.setCancelable(false)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    layout.imageIcon.setImageResource(icon)
    layout.tvTitle.text = resources.getString(title)
    layout.tvMessage.text = resources.getString(message)

    return dialog
}

fun Context.createProgressDialog(): Dialog {
    val dialog = Dialog(this, com.denzcoskun.imageslider.R.style.Theme_AppCompat_Dialog)
    val binding = LayoutBaseLoadingBinding.inflate(LayoutInflater.from(this))

    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.setCancelable(false)
    dialog.setContentView(binding.root)

    return dialog
}

fun Context.showDialog(
    title: String,
    message: String,
    action: () -> Unit
) {
    val dialog = MaterialAlertDialogBuilder(this)
    dialog.setCancelable(false)
    dialog.setTitle(title).setMessage(message).setCancelable(false)
        .setPositiveButton(R.string.ok) { dialog, which ->
            action()
            dialog.cancel()
        }
    dialog.show()
}

fun Context.createForgotPassCompleteProgressDialog(
    onClickCheckMail: () -> Unit,
    onClickGoBack: (Dialog) -> Unit,
): Dialog {
    val dialog = Dialog(this, com.denzcoskun.imageslider.R.style.Theme_AppCompat_Dialog)
    val binding = ForgotPassSuccessDialogLayoutBinding.inflate(LayoutInflater.from(this))

    dialog.setCancelable(false)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
    dialog.window?.setLayout(
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.MATCH_PARENT
    )

    binding.buttonCheckMail.setSafeOnClickListener {
        dialog.cancel()
        onClickCheckMail()
    }

    binding.buttonGoBack.setSafeOnClickListener {
        onClickGoBack(dialog)
    }

    return dialog
}

fun Context.createVerificationDialog(
    onClickCheckMail: (Dialog) -> Unit,
    onClickGoBack: (Dialog) -> Unit,
): Dialog {
    val dialog = Dialog(this, com.denzcoskun.imageslider.R.style.Theme_AppCompat_Dialog)
    val binding = VerificationEmailLayoutBinding.inflate(LayoutInflater.from(this))

    dialog.setCancelable(false)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);

    dialog.window?.setLayout(
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.MATCH_PARENT
    )

    binding.buttonCheckMail.setSafeOnClickListener {
        onClickCheckMail(dialog)
    }

    binding.buttonGoBack.setSafeOnClickListener {
        onClickGoBack(dialog)
    }

    dialog.show()

    return dialog
}

fun Double.toRate(): String = "%.1f".format(this)


fun String.fullMonthTimeFormat(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val date: LocalDate = LocalDate.parse(this, inputFormat)
        val outputFormat = DateTimeFormatter.ofPattern("dd MMMM, yyyy", Locale.getDefault())
        date.format(outputFormat)
    } else {
        this
    }
}

fun Context.showNetworkDialog(
    message: String,
    positiveAction: () -> Unit,
    negativeAction: (Dialog) -> Unit,
): Dialog {
    val dialog = MaterialAlertDialogBuilder(this).setIcon(R.drawable.app_logo).setMessage(message)
        .setTitle(R.string.network_error).setCancelable(false)
        .setPositiveButton(R.string.ok) { dialog, _ ->
            positiveAction()
            dialog.cancel()
        }.setNegativeButton(R.string.refresh) { dialog, _ ->
        negativeAction(dialog as Dialog)
    }
    return dialog.create()
}

fun exitTheApplication(contextView: View, action: () -> Unit) {
    Snackbar.make(contextView, R.string.exit_app, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.ok) {
            action()
        }.show()

}


fun Fragment.onBackPress(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this.viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                action()
            }
        }
    )
}

fun RecyclerView.preview(fragment: Fragment) {
    this.doOnPreDraw {
        fragment.startPostponedEnterTransition()
    }
}

fun View.saveAsImageToPhone(isSharing: Boolean) {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)

    try {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "file.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/cineMate")
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        uri?.let {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
            outputStream?.let { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()
                if (isSharing) shareImage(it, this.context)
            }
        }
    } catch (e: Exception) {
        Log.e("test", "Error ${e.localizedMessage}")
    }
}

private fun shareImage(imageUri: Uri, context: Context) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("image/*")
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        context.startActivity(Intent.createChooser(shareIntent, "Share.."))
    } catch (e: Exception) {
        Log.e("test", "Error ${e.localizedMessage}")
    }
}

fun Activity.checkForAppUpdates(
    appUpdateManager: AppUpdateManager,
    updateType: Int,
    registerForActivityResult: ActivityResultLauncher<IntentSenderRequest>
) {
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo


    appUpdateInfoTask.addOnSuccessListener { info ->
        val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
        val isUpdateAllowed = when (updateType) {
            AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
            AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
            else -> false
        }
        if (isUpdateAvailable && isUpdateAllowed) {
            appUpdateManager.startUpdateFlowForResult(
                info,
                registerForActivityResult,
                AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
            )
        }
    }
}

