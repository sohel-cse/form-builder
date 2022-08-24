package com.feiyilin.app

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.feiyilin.app.permissions.CallBackWrapper
import com.feiyilin.form.*
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : FormActivity() {

    lateinit var activityResultMultiLauncher: ActivityResultLauncher<Array<String>>
    lateinit var activityResultSingleLauncher: ActivityResultLauncher<String>


    private val multiWrapper: CallBackWrapper<Map<String, Boolean>> = CallBackWrapper()
    private val singleWrapper: CallBackWrapper<Boolean> = CallBackWrapper()
    var newItemCreated = 0
    var newSectionCreated = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultMultiLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), multiWrapper)
        activityResultSingleLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(), singleWrapper)
    }

    override fun initForm() {
        val cal = Calendar.getInstance()
        cal.set(2020, 6, 19)

        adapter?.apply {
            +FormItemSection().title("Text").tag("sec_text").apply {
                enableCollapse()
                +FormItemFile().tag("file").title("File Test").onCameraClicked {
                    dispatchTakePictureIntent(it)
                }.onGalleryClicked {
                    Log.i(TAG, "onGalleryClicked: $it")
                }.onDetailClicked {

                }.galleryOnly(true)
                +FormItemText().title("Text").tag("text").required()
                +FormItemText().title("Text").subTitle("with clear text icon").tag("text").clearIcon()
                +FormItemText().title("Text").subTitle("here is subtitle").tag("text_subtitle")
                +FormItemText().title("Text").subTitle("draggable").draggable()
                    .tag("text_draggable")
                +FormItemText().title("Text").subTitle("draggable in this section").draggable()
                    .tag("text_draggable_in_section")
                +FormItemText().title("With icon").iconTitle(drawable(R.drawable.ic_form_info))
                    .tag("text_icon")
                +FormItemText().title("Ready only").tag("read_only").value("www.feiyilin.com")
                    .readOnly()
                +FormItemTextFloatingHint().hint("Text with floating hint").tag("text")
                    .gravity(Gravity.START)
                +FormItemText().title("Email").tag("email")
                    .inputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS).addRule(EmailRule()).required()

                +FormItemText().title("Phone").tag("phone")
                    .inputType(EditorInfo.TYPE_CLASS_PHONE).imeOptions(EditorInfo.IME_ACTION_PREVIOUS)
                    .onEditorAction { item, _, _ ->
                        Toast.makeText(this@MainActivity, "EditorAction on ${item.title}", Toast.LENGTH_SHORT).show()
                        false
                    }
                +FormItemPassword().title("Password").tag("password")
                +FormItemLabel().title("Label").tag("label")
                    .subTitle("subtitle of label")
            }

            +FormItemSection().title("Number").apply {
                +FormItemNumber().title("Number").value(123456.789)
                +FormItemNumber().title("Currency").value(123456.789).toDisplayString {
                    NumberFormat.getCurrencyInstance().format(it.value)
                }
            }

            +FormItemSection().title("Multi-line text").apply {
                enableCollapse()
                +FormItemTextArea().hint("Multi-line text here ...").tag("notes")
                +FormItemTextAreaFloatingHint().hint("Multi-line text with floating hint here ...")
                    .tag("notes")
            }

            +FormItemSection().title("Navigation item").apply {
                enableCollapse()
                //FormItemLabel().title("Label").tag("label"),
                +FormItemNav().title("Nav item").tag("nav_item").onItemClicked { item, _ ->
                    Toast.makeText(this@MainActivity, "Click on ${item.title}", Toast.LENGTH_SHORT).show()
                }
                +FormItemNav().title("Nav item with subtitle").subTitle("www.abc.com")
                    .tag("nav_item_subtitle")
                +FormItemNav().title("Nav item with badge").tag("nav_item_badge").badge("")
                +FormItemNav().title("Nav item with badge and icon").tag("nav_item_badge_icon")
                    .badge("")
                    .iconTitle(drawable(R.drawable.ic_form_info))
                    .onTitleImageClicked { item, _ ->
                        Toast.makeText(this@MainActivity, "Click on ${item.title} title image", Toast.LENGTH_SHORT).show()
                    }
                    .onItemClicked { item, _ ->
                        Toast.makeText(this@MainActivity, "Click on ${item.title}", Toast.LENGTH_SHORT).show()
                    }

                +FormItemNav().title("Nav item with number badge").tag("nav_item_badge_num")
                    .badge("99").badgeColor(Color.parseColor("#ff9800"))
                    .iconSize(44, 44)
                    .iconTitle(drawable(R.drawable.ic_form_info))
            }
            +FormItemSection().title("Action").apply {
                +FormItemAction().title("Action").iconTitle(drawable(R.drawable.ic_form_info))
                +FormItemAction().title("Action").subTitle("description")
                    .iconTitle(drawable(R.drawable.ic_form_info)).alignment(Gravity.START)
                +FormItemAction().title("Action").subTitle("description")
                    .iconTitle(drawable(R.drawable.ic_form_info)).alignment(Gravity.END)
            }
            +FormItemSection().title("Radio").apply {
                +FormItemRadio().isOn().group("radio")
                    .title("item 0")
                    .tag("radio1_item0")
                +FormItemRadio().group("radio").title("item 1")
                    .tag("radio1_item1")
                +FormItemRadio().group("radio").title("item 2")
                    .tag("radio1_item2")
            }
            +FormItemSection().title("Radio custom").apply {
                +FormItemRadioCustom().isOn().group("radio")
                    .title("item 0")
                    .tag("radio0_item0").isOn()
                +FormItemRadioCustom().group("radio").title("item 1")
                    .tag("radio0_item1").isOn()
                +FormItemRadioCustom().group("radio").title("item 2")
                    .tag("radio0_item2").isOn()
            }
            +FormItemSection().title("Checkbox").apply {
                +FormItemCheck().isOn().title("Check")
                +FormItemCheckCustom().title("Check custom")
            }
            +FormItemSection().title("Switch").apply {
                +FormItemSwitch().isOn().title("Switch").tag("switch_native")
                +FormItemSwitchCustom().isOn().title("Switch custom").tag("switch")
                +FormItemSwitch().isOn().title("Show action item")
                    .tag("switch_show_action")
                +FormItemAction().title("Action").tag("action").subTitle("description")
                    .iconTitle(drawable(R.drawable.ic_form_info))
                +FormItemSwitch().isOn().title("Show date/time section")
                    .tag("switch_show_date")
            }

            +FormItemSection().title("Date / Time").tag("sec_date").apply {
                +FormItemDate().tag("date").title("Date").date(cal.time)
                +FormItemDate().tag("date_only").title("Date only").date(cal.time)
                    .dateOnly()
                +FormItemDate().tag("time_only").title("Time only  formElementValue formElementValue formElementValue formElementValue").date(cal.time)
                    .timeOnly()
            }
            +FormItemSection().title("SeekBar & Stepper").apply {
                +FormItemSeekBar().title("SeekBar").value(19)
                +FormItemSeekBar().title("SeekBar red").tag("seekbar_red").maxValue(50).minValue(10).value(29)
                +FormItemSeekBar().title("SeekBar orange").tag("seekbar_orange").maxValue(50).minValue(10).value(39)
                    .onSetup { _, viewHolder ->
                        if (viewHolder is FormSeekBarViewHolder) {
                            viewHolder.seekBar?.progressDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor("#ff9800"), PorterDuff.Mode.SRC_IN)
                            viewHolder.seekBar?.thumb?.colorFilter = PorterDuffColorFilter(Color.parseColor("#ff9800"), PorterDuff.Mode.SRC_IN)
                        }
                    }
                +FormItemStepper().title("Stepper").value(19)
            }

            +FormItemSection().title("Swipe Action").tag("sec_swipe").apply {
                +FormItemNav().title("Swipe left").trailingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").style(FormSwipeAction.Style.Destructive)
                    )
                )
                +FormItemNav().title("Swipe left: with icon").trailingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").style(FormSwipeAction.Style.Destructive)
                            .icon(drawable(R.drawable.ic_delete_white))
                    )
                )
                +FormItemNav().title("Swipe left: multiple actions").trailingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").backgroundColor(
                            color(android.R.color.holo_red_light)
                        ),
                        FormSwipeAction().title("Archive").backgroundColor(
                            color(android.R.color.holo_blue_light)
                        ),
                        FormSwipeAction().title("Mark as unread").backgroundColor(
                            color(android.R.color.holo_green_light)
                        )
                    )
                ).onSwipedAction { item, action, _ ->
                    Toast.makeText(this@MainActivity, "Inline callback ${item.title}: ${action.title}", Toast.LENGTH_SHORT)
                        .show()
                    if (action.title == "Delete") {
                        return@onSwipedAction adapter?.remove(item) ?: false
                    }
                    return@onSwipedAction false
                }
                +FormItemNav().title("Swipe left: multiple actions with icon").trailingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").backgroundColor(
                            color(android.R.color.holo_red_light)
                        ).icon(drawable(R.drawable.ic_delete_white)),
                        FormSwipeAction().title("Archive").backgroundColor(
                            color(android.R.color.holo_blue_light)
                        ).icon(drawable(R.drawable.ic_archive_white)),
                        FormSwipeAction().title("Mark as unread").backgroundColor(
                            color(android.R.color.holo_green_light)
                        ).icon(drawable(R.drawable.ic_markunread_white))
                    )
                )
                +FormItemNav().title("Swipe right").leadingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").style(FormSwipeAction.Style.Destructive)
                    )
                )
                +FormItemNav().title("Swipe right: with icon").leadingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").style(FormSwipeAction.Style.Destructive)
                            .icon(drawable(R.drawable.ic_delete_white))
                    )
                )
                +FormItemNav().title("Swipe right: multiple actions").leadingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").backgroundColor(
                            color(android.R.color.holo_red_light)
                        ),
                        FormSwipeAction().title("Archive").backgroundColor(
                            color(android.R.color.holo_blue_light)
                        ),
                        FormSwipeAction().title("Mark as unread").backgroundColor(
                            color(android.R.color.holo_green_light)
                        )
                    )
                )
                +FormItemNav().title("Swipe right: multiple actions with icon").leadingSwipe(
                    listOf(
                        FormSwipeAction().title("Delete").backgroundColor(
                            color(android.R.color.holo_red_light)
                        ).icon(drawable(R.drawable.ic_delete_white)),
                        FormSwipeAction().title("Archive").backgroundColor(
                            color(android.R.color.holo_blue_light)
                        ).icon(drawable(R.drawable.ic_archive_white)),
                        FormSwipeAction().title("Mark as unread").backgroundColor(
                            color(android.R.color.holo_green_light)
                        ).icon(drawable(R.drawable.ic_markunread_white))
                    )
                )
            }

            +FormItemSection().title("Choice").apply {
                +FormItemSelect().tag("select").title("Select").value("Monday")
                    .selectorTitle("Select day of week")
                    .options(arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                    .onValueChanged { item ->
                        Toast.makeText(this@MainActivity, "${item.title} value changed to ${item.value}", Toast.LENGTH_SHORT).show()
                    }
                +FormItemChoice().tag("choice").title("Choice").value("Tuesday")
                    .selectorTitle("Select day of week")
                    .options(arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                +FormItemPicker().tag("picker").title("Picker").value("Wednesday")
                    .selectorTitle("Select day of week")
                    .options(arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                +FormItemPickerInline().tag("picker_inline").title("Picker Inline")
                    .value("Thursday")
                    .selectorTitle("Select day of week")
                    .options(arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                +FormItemMultipleChoice().tag("multiple_choice").title("Multiple Choice")
                    .value("Thursday").selectorTitle("Select day of week")
                    .options(arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                +FormItemColor().tag("color_choice").title("Color").value("#03a9f4")
                +FormItemColor().tag("color_choice2").title("Color").value("#ff9800")
                    .cornerRadius(5)
            }

            +FormItemSection().title("Separator").apply {
                +FormItemNav().title("Default").separator(null)
                    .iconTitle(drawable(R.drawable.ic_form_info))
                +FormItemNav().title("Insets").separator(FormItem.Separator.Insets(16, 0, 24, 0))
                    .iconTitle(drawable(R.drawable.ic_form_info))
                +FormItemNav().title("No separator").separator(FormItem.Separator.None())
                    .iconTitle(drawable(R.drawable.ic_form_info))
            }
            +FormItemSection().title("Custom item").apply {
                +FormItemImage().tag("image").image(R.drawable.image1)
            }
            +FormItemSection().title("Dynamic add/delete").apply {
                +FormItemNav().title("Add item").tag("add_item")
                +FormItemNav().title("Add section").tag("add_section")
            }
        }

        adapter?.registerViewHolder(
            FormItemImage::class.java,
            R.layout.form_item_image,
            FormImageViewHolder::class.java
        )

        adapter?.registerViewHolder(
            FormItemFile::class.java,
            R.layout.form_item_file,
            FormFileViewHolder::class.java
        )
    }


    fun getSingleLauncher(callback: ActivityResultCallback<Boolean>): ActivityResultLauncher<String> {
        singleWrapper.callback = callback
        return activityResultSingleLauncher
    }

    override var onFormItemListener: FormItemCallback? = object : FormItemCallback {
        override fun onValueChanged(item: FormItem) {
            Log.i("onValueChanged", item.toString())
            if (item.tag == "switch_show_date") {
                if (item is FormItemSwitch) {
                    adapter?.let { adapter ->
                        val action = adapter.sectionBy("sec_date")
                        action?.let {
                            adapter.hide(it, !item.isOn)
                        }
                    }
                }
            } else if (item.tag == "switch_show_action") {
                if (item is FormItemSwitch) {
                    adapter?.let { adapter ->
                        val action = adapter.itemBy("action")
                        action?.let {
                            adapter.hide(it, !item.isOn)
                        }
                    }
                }
            }
        }

        override fun onItemClicked(item: FormItem, viewHolder: RecyclerView.ViewHolder) {
            Log.i("onItemClicked", item.toString())
            if (item is FormItemSection) {
                if (item.enableCollapse) {
                    adapter?.collapse(item, !item.collapsed)
                    return
                }
                if (item.title.contains("click to clear children")) {
                    item.clear()
                    return
                }
            }
            when (item.tag) {
                "add_item" -> {
                    newItemCreated += 1
                    adapter?.apply {
                        item.section?.add(
                            item, FormItemNav().title("New item $newItemCreated").trailingSwipe(
                                listOf(FormSwipeAction().title("Delete").style(FormSwipeAction.Style.Destructive))
                            )
                        )
                    }
                }
                "add_section" -> {
                    newSectionCreated += 1
                    adapter?.apply {
                        val sec = FormItemSection().title("New section $newSectionCreated (click to clear children)").apply {
                            trailingSwipe(
                                listOf(FormSwipeAction().title("Delete").style(FormSwipeAction.Style.Destructive))
                            )
                            +FormItemNav().title("Item 0")
                            +FormItemNav().title("item 1")
                        }

                        sec.add(FormItemNav().title("Item 2"))
                        add(item, sec)
                        ensureVisible(sec)
                    }
                }
            }
        }

        override fun onSwipedAction(
            item: FormItem,
            action: FormSwipeAction,
            viewHolder: RecyclerView.ViewHolder
        ): Boolean {
            super.onSwipedAction(item, action, viewHolder)
            Toast.makeText(this@MainActivity, "${item.title}: ${action.title}", Toast.LENGTH_SHORT)
                .show()
            if (action.title == "Delete") {
                return adapter?.remove(item) ?: false
            }
            return false
        }

        override fun getMinItemHeight(item: FormItem): Int {
            if (item is FormItemSection) {
                return 0
            }
            return 0
        }

        override fun onMoveItem(src: Int, dest: Int): Boolean {
            val srcItem = adapter?.itemBy(src)
            if (srcItem?.tag == "text_draggable_in_section") {
                val destItem = adapter?.itemBy(dest)
                if (srcItem.section != destItem?.section) {
                    // not allow to move out of section
                    return true
                }
            }
            return false
        }

        override fun onSetup(item: FormItem, viewHolder: RecyclerView.ViewHolder) {
            super.onSetup(item, viewHolder)
            if (item is FormItemSeekBar) {
                if (item.tag == "seekbar_red") {
                    if (viewHolder is FormSeekBarViewHolder) {
                        viewHolder.seekBar?.progressDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_IN)
                        viewHolder.seekBar?.thumb?.colorFilter = PorterDuffColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_IN)
                    }
                }
            }
        }

        override fun onEditorAction(
            item: FormItem,
            actionId: Int,
            viewHolder: RecyclerView.ViewHolder
        ): Boolean {
            return super.onEditorAction(item, actionId, viewHolder)
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent(id: String) {


        getSingleLauncher(ActivityResultCallback { result ->
            if (result) Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }).launch(Manifest.permission.CAMERA)
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 5
        const val TAG = "MainActivity"
    }
}

open class FormItemImage : FormItem() {
    var image: Int = 0
}

fun <T : FormItemImage> T.image(image: Int) = apply {
    this.image = image
}

class FormImageViewHolder(inflater: LayoutInflater, resource: Int, parent: ViewGroup) :
    FormViewHolder(inflater, resource, parent) {
    private var imgView: ImageView? = null

    init {
        imgView = itemView.findViewById(R.id.formELementImage)
    }

    override fun bind(s: FormItem, listener: FormItemCallback?) {

        if (s is FormItemImage) {
            Picasso.get().load(s.image).fit().centerInside().into(imgView)

            imgView?.setOnClickListener {
                listener?.onValueChanged(s)
            }
        }
    }


}