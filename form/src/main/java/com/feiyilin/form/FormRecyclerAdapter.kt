package com.feiyilin.form

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.feiyilin.form.file.FormFileViewHolder
import com.feiyilin.form.file.FormItemFile
import com.feiyilin.form.signature.FormItemSignature
import com.feiyilin.form.signature.FormSignatureViewHolder

interface FormItemCallback {
    fun onSetup(item: FormItem, viewHolder: RecyclerView.ViewHolder) {}
    fun onValueChanged(item: FormItem) {}
    fun onItemClicked(item: FormItem, viewHolder: RecyclerView.ViewHolder) {}
    fun onTitleImageClicked(item: FormItem, viewHolder: RecyclerView.ViewHolder) {}
    fun onStartReorder(item: FormItem, viewHolder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    fun onMoveItem(src: Int, dest: Int): Boolean {
        return false
    }

    fun onSwipedAction(
        item: FormItem,
        action: FormSwipeAction,
        viewHolder: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    fun onEditorAction(item: FormItem, actionId: Int, viewHolder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    fun getMinItemHeight(item: FormItem): Int {
        return 0
    }

    fun getSeparator(item: FormItem): FormItem.Separator? {
        return null
    }
}

open class FormRecyclerAdapter(
    protected var listener: FormItemCallback? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var _sections: MutableList<FormItemSection> = mutableListOf()
    var sections: List<FormItemSection>
        get() = _sections
        set(value) {
            _sections = value.toMutableList()
            update()
        }

    protected var itemTouchHelper: ItemTouchHelper? = null
    protected var recyclerView: RecyclerView? = null

    val activity: AppCompatActivity?
        get() {
            return recyclerView?.context as? AppCompatActivity
        }

    class ViewHolderItem(
        var type: Class<out FormItem>,
        var layoutId: Int,
        var viewHolderClass: Class<out FormViewHolder>
    )

    private var viewHolders: MutableList<ViewHolderItem> = mutableListOf()

    init {
        viewHolders = mutableListOf(
            ViewHolderItem(
                FormItemSection::class.java,
                R.layout.form_item_section,
                FormSectionViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemText::class.java,
                R.layout.form_item_text,
                FormTextViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemPassword::class.java,
                R.layout.form_item_text,
                FormPasswordViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemNumber::class.java,
                R.layout.form_item_text,
                FormTextViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemTextFloatingHint::class.java,
                R.layout.form_item_text_floating_hint,
                FormTextViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemTextArea::class.java,
                R.layout.form_item_text,
                FormTextAreaViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemTextAreaFloatingHint::class.java,
                R.layout.form_item_text_floating_hint,
                FormTextAreaViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemAction::class.java,
                R.layout.form_item_action,
                FormActionViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemSwitchCustom::class.java,
                R.layout.form_item_switch_custom,
                FormSwitchCustomViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemSwitch::class.java,
                R.layout.form_item_switch,
                FormSwitchViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemRadioCustom::class.java,
                R.layout.form_item_radio_custom,
                FormRadioCustomViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemRadio::class.java,
                R.layout.form_item_radio,
                FormRadioViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemNav::class.java,
                R.layout.form_item_nav,
                FormNavViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemLabel::class.java,
                R.layout.form_item_label,
                FormLabelViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemDate::class.java,
                R.layout.form_item_date,
                FormDateViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemSelect::class.java,
                R.layout.form_item_choice,
                FormSelectViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemChoice::class.java,
                R.layout.form_item_choice,
                FormChoiceViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemPicker::class.java,
                R.layout.form_item_choice,
                FormPickerViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemPickerInline::class.java,
                R.layout.form_item_picker,
                FormPickerInlineViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemMultipleChoice::class.java,
                R.layout.form_item_choice,
                FormMultipleChoiceViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemColor::class.java,
                R.layout.form_item_color,
                FormColorViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemSeekBar::class.java,
                R.layout.form_item_seekbar,
                FormSeekBarViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemStepper::class.java,
                R.layout.form_item_stepper,
                FormStepperViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemCheck::class.java,
                R.layout.form_item_check,
                FormCheckViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemCheckCustom::class.java,
                R.layout.form_item_check_custom,
                FormCheckCustomViewHolder::class.java
            ),
            ViewHolderItem(
                FormItemSignature::class.java,
                R.layout.form_item_signature,
                FormSignatureViewHolder::class.java
            )
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.update()
        val touchHelper =
            object : FormSwipeHelper() {

                override fun getFormItem(pos: Int): FormItem {
                    val item = itemBy(pos)
                    return item!!
                }

                override fun updateItem(pos: Int) {
                    recyclerView.adapter?.notifyItemChanged(pos)
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val src = viewHolder.adapterPosition
                    val des = target.adapterPosition
                    return onFormItemCallback.onMoveItem(src, des)
                }

                override fun onActionClicked(pos: Int, action: FormSwipeAction) {
                    super.onActionClicked(pos, action)
                    itemBy(pos)?.let { item ->
                        var processed = false
                        recyclerView.findViewHolderForAdapterPosition(pos)?.let {
                            processed = onFormItemCallback.onSwipedAction(item, action, it)
                        }
                        if (!processed && indexOf(item) >= 0) {
                            updateItem(pos)
                        }
                    }
                }
            }

        itemTouchHelper = ItemTouchHelper(touchHelper)
        itemTouchHelper?.attachToRecyclerView(recyclerView)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = viewHolders[viewType]
        val viewHolder = item.viewHolderClass.kotlin
        return viewHolder.constructors.first().call(inflater, item.layoutId, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val s = itemBy(position)

        if (holder is FormViewHolder && s != null) {
            holder.bind(s, onFormItemCallback, activity)

            onFormItemCallback.onSetup(s, holder)
        }
    }

    override fun getItemCount(): Int {
        return sections.sumBy { it.sizeVisible }
    }

    /**
     * update all items (e.g., after setting sections)
     */
    fun update() {
        activity?.runOnUiThread {
            sections.forEach {
                it.adapter = this
                it.update()
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val m = itemBy(position)!!
        return viewHolders.indexOfFirst { it.type == m::class.java }
    }

    /**
     * register a new view holder
     */
    fun registerViewHolder(
        type: Class<out FormItem>,
        layoutId: Int,
        viewHolderClass: Class<out FormViewHolder>
    ): Boolean {
        viewHolders.add(ViewHolderItem(type, layoutId, viewHolderClass))
        return true
    }

    /**
     * update the item (e.g., after changing some fields)
     * @param item the item to be updated
     */
    fun updateItem(item: FormItem) {
        if (item.hidden) {
            return
        }
        val index = indexOf(item)
        if (index != -1) {
            activity?.let {
                it.runOnUiThread {
                    notifyItemChanged(index)
                }
            }
        }
    }

    /**
     * update the radio group
     * @param item the selected item in the radio group
     */
    fun updateRadioGroup(item: FormItemRadio) {
        item.section?.updateRadioGroup(item)
    }

    /**
     * select the item in radio group
     * @param item the selected item in the radio group
     */
    fun selectRadioItem(item: FormItemRadio) {
        onFormItemCallback.onValueChanged(item)
    }

    protected var onFormItemCallback = object : FormItemCallback {
        override fun onValueChanged(item: FormItem) {
            if (item is FormItemRadio) {
                updateRadioGroup(item)
            }
            if (item.onValueChanged != null) {
                item.onValueChanged?.invoke()
            } else {
                listener?.onValueChanged(item)
            }
        }

        override fun onItemClicked(item: FormItem, viewHolder: RecyclerView.ViewHolder) {
            val index = indexOf(item)

            Handler().postDelayed({
                //Do something after 100ms
                activity?.runOnUiThread {
                    recyclerView?.smoothScrollToPosition(index)
                }
            }, 100)

            if (item.onItemClicked != null) {
                item.onItemClicked?.invoke(viewHolder)
            } else {
                listener?.onItemClicked(item, viewHolder)
            }
        }

        override fun onSetup(item: FormItem, viewHolder: RecyclerView.ViewHolder) {
            if (item.onSetup != null) {
                item.onSetup?.invoke(viewHolder)
            } else {
                listener?.onSetup(item, viewHolder)
            }
        }

        override fun onStartReorder(
            item: FormItem,
            viewHolder: RecyclerView.ViewHolder
        ): Boolean {
            if (item.onStartReorder != null) {
                if (item.onStartReorder?.invoke(viewHolder) == true) {
                    return true
                }
            } else if (listener?.onStartReorder(item, viewHolder) == true)
                return true
            itemTouchHelper?.startDrag(viewHolder)
            return true
        }

        override fun onMoveItem(src: Int, dest: Int): Boolean {
            super.onMoveItem(src, dest)
            val itemSrc = itemBy(src)!!
            if (itemSrc.onMoveItem != null) {
                if (itemSrc.onMoveItem?.invoke(src, dest) == true) {
                    return false
                }
            } else if (listener?.onMoveItem(src, dest) == true) {
                // it has been processed, ignore the default operation
                return false
            }

            val itemDest = itemBy(dest)!!
            if (itemDest == itemSrc.section && dest == 0) {
                // not allow to move before the first section
                return false
            }

            itemDest.section?.let {
                val offset = it.items.indexOf(itemDest)
                val secSrc = itemSrc.section

                if (it == secSrc) {
                    // src, dest are in same section
                    if (it == itemDest) {
                        // move the item out of the current section (swap the item with the section)
                        // get the section of previous item
                        val secDest = itemBy(dest - 1)?.section
                        if (secDest == null || secDest.collapsed) {
                            // the previous section is not able to receive the new item as it is
                            // either invalid or in collapsed status
                            return false
                        }
                        // move it to the previous section, as in a section, the first item shall
                        // always be the section itself. No need to update UI, will be updated with
                        // notifyItemMoved
                        itemSrc.section?.remove(itemSrc, false)
                        secDest.add(itemSrc, false)
                    } else {
                        itemSrc.section?.remove(itemSrc, false)
                        it.add(offset, itemSrc, false)
                    }
                } else if (dest > src) {
                    // move to the section after
                    if (it.collapsed) {
                        // the destination section is not able to receive the new item as it is
                        // in collapsed status
                        return false
                    }

                    itemSrc.section?.remove(itemSrc, false)
                    it.add(offset + 1, itemSrc, false)
                } else {
                    // move to the section before

                    var secDest: FormItemSection? = it
                    var offsetDest = offset
                    if (secDest?.collapsed == true && dest > 0) {
                        // the destination section is not able to receive the new item as it is
                        // in collapsed status; try the item above: if it is in a valid section,
                        // add the src item there
                        secDest = itemBy(dest - 1)?.section
                        offsetDest = secDest?.items?.size ?: -1
                    }

                    if (secDest?.collapsed == true || offsetDest == -1) {
                        // the destination section is still not able to receive the new item as it is
                        // in collapsed status
                        return false
                    }

                    itemSrc.section?.remove(itemSrc, false)
                    secDest?.add(offsetDest, itemSrc, false)
                }
                notifyItemMoved(src, dest)
                return true
            }
            return false
        }

        override fun onTitleImageClicked(item: FormItem, viewHolder: RecyclerView.ViewHolder) {
            super.onTitleImageClicked(item, viewHolder)
            if (item.onTitleImageClicked != null) {
                item.onTitleImageClicked?.invoke(viewHolder)
            } else {
                listener?.onTitleImageClicked(item, viewHolder)
            }
        }

        override fun onSwipedAction(
            item: FormItem,
            action: FormSwipeAction,
            viewHolder: RecyclerView.ViewHolder
        ): Boolean {
            super.onSwipedAction(item, action, viewHolder)
            if (item.onSwipedAction != null) {
                return item.onSwipedAction?.invoke(action, viewHolder) ?: false
            }
            return listener?.onSwipedAction(item, action, viewHolder) ?: false
        }

        override fun onEditorAction(item: FormItem, actionId: Int, viewHolder: RecyclerView.ViewHolder): Boolean {
            super.onEditorAction(item, actionId, viewHolder)
            if (item.onEditorAction != null) {
                if (item.onEditorAction?.invoke(actionId, viewHolder) == true) {
                    return true
                }
            } else if (listener?.onEditorAction(item, actionId, viewHolder) == true) {
                return true
            }
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    goToNextEdit(item)
                }
                EditorInfo.IME_ACTION_PREVIOUS -> {
                    goToNextEdit(item, false)
                }
            }
            return true
        }

        override fun getMinItemHeight(item: FormItem): Int {
            return listener?.getMinItemHeight(item) ?: 0
        }

        override fun getSeparator(item: FormItem): FormItem.Separator? {
            return listener?.getSeparator(item) ?: super.getSeparator(item)
        }
    }

    /**
     * get the section by its tag from all sections (include all hidden items)
     * @param tag the section tag
     * @return the section or null if not found
     */
    fun sectionBy(tag: String): FormItemSection? {
        for (sec in sections) {
            if (sec.tag == tag) {
                return sec
            }
        }
        return null
    }

    /**
     * get the item by its tag from all items (include all hidden items)
     * @param tag the item tag
     * @return the item or null if not found
     */
    fun itemBy(tag: String): FormItem? {
        var item: FormItem? = null
        for (sec in sections) {
            if (sec.tag == tag) {
                item = sec
                break
            }
            item = sec.itemBy(tag)
            if (item != null) {
                break
            }
        }
        return item
    }

    /**
     * get the item by its index from all visible items
     * @param index the index in adapter
     * @return the item or null if not found
     */
    fun itemBy(index: Int): FormItem? {
        var item: FormItem? = null
        var count = 0
        for (sec in sections) {
            if (index < count + sec.sizeVisible) {
                item = sec[index - count]
                break
            }
            count += sec.sizeVisible
        }
        return item
    }

    /**
     * return the index of the item in adapter
     * @param item item to retrieve the index
     * @return the item index or -1 if not found
     */
    fun indexOf(item: FormItem): Int {
        var count = 0
        for (sec in sections) {
            if (sec == item.section) {
                val index = sec.indexOf(item)
                if (index != -1) {
                    return index + count
                }
                break
            }
            count += sec.sizeVisible
        }
        require(false) { "item not in adapter" }
        return -1
    }

    /**
     * show/hide a section (and all its children)
     * @param section section to be shown/hidden
     * @return true if succeed
     */
    fun hide(section: FormItemSection, hide: Boolean): Boolean {
        // show/hide a section
        if (section.adapter != this) {
            return false
        }
        if (section.hidden == hide) {
            return true
        }
        activity?.runOnUiThread {
            section.hidden = hide
            val index = startOfSection(section)
            val size = section.sizeVisible
            section.update()
            val sizeNew = section.sizeVisible

            // show/hide the last |size-sizeNew| items
            if (section.hidden) {
                notifyItemRangeRemoved(index + sizeNew, size - sizeNew)
            } else {
                notifyItemRangeInserted(index + size, sizeNew - size)
            }
        }
        return true
    }

    fun hide(item: FormItem, hide: Boolean): Boolean {
        if (item is FormItemSection) {
            return hide(item, hide)
        }
        return item.section?.hide(item, hide) ?: false
    }

    /**
     * collapse/un-collapse a section
     * @param section section to be (un)collapsed
     * @return true if succeed
     */
    fun collapse(section: FormItemSection, collapsed: Boolean): Boolean {
        if (section.adapter != this) {
            return false
        }
        if (!section.enableCollapse) {
            return false
        }
        if (section.collapsed == collapsed) {
            return true
        }
        activity?.runOnUiThread {
            section.collapsed(collapsed)
            val index = startOfSection(section)
            val size = section.sizeVisible
            section.update()
            val sizeNew = section.sizeVisible
            if (sizeNew != size) {
                // show/hide the last |size-sizeNew| items
                if (section.collapsed) {
                    notifyItemRangeRemoved(index + sizeNew, size - sizeNew)
                } else {
                    notifyItemRangeInserted(index + size, sizeNew - size)
                }
            }
        }
        return true
    }

    /**
     * clear all sections
     */
    fun clear() {
        sections.forEach {
            it.adapter = null
        }
        _sections.clear()
        update()
    }

    /**
     * add section to adapter, need to call update() after adding all sections
     */
    operator fun FormItemSection.unaryPlus() {
        if (sections.indexOf(this) != -1 || adapter != null) {
            return
        }
        add(section = this)
    }

    /**
     * insert a section
     * @param secIndex the index (in sections)
     * @param section section to be added
     * @return true if succeed
     */
    fun add(secIndex: Int, section: FormItemSection): Boolean {
        if (sections.indexOf(section) != -1 || secIndex < 0 || secIndex > sections.size || section in sections) {
            return false
        }
        section.adapter = this
        _sections.add(secIndex, section)
        section.update()

        if (section.isNotEmpty) {
            val start = startOfSection(section)
            activity?.runOnUiThread {
                notifyItemRangeInserted(start, section.sizeVisible)
            }
        }
        return true
    }

    /**
     * add a section to the end
     * @param section the section to be added
     * @return true if succeed
     */
    fun add(section: FormItemSection): Boolean {
        return add(sections.size, section)
    }

    /**
     * add section after the "after" section
     * @param after any item of the section before the added section
     * @param section the section to be added
     * @return true if succeed
     */
    fun add(after: FormItem, section: FormItemSection): Boolean {
        val sec = after.section ?: return false
        val index = sections.indexOf(sec)
        if (index == -1) {
            return false
        }
        return add(index + 1, section)
    }


    /**
     * If section is visible, it return the index of its first item; otherwise, return the index of
     * its first child if the section is visible.
     */
    internal fun startOfSection(section: FormItemSection): Int {
        var index = 0
        for (sec in sections) {
            if (sec == section) {
                return index
            }
            index += sec.sizeVisible
        }
        return -1
    }

    /**
     * remove the section and all its children
     * @param section section to be removed
     * @return true if succeed
     */
    fun remove(section: FormItemSection): Boolean {
        if (section.adapter != this) {
            return false
        }
        val index = startOfSection(section)
        if (index == -1) {
            // section is not in adapter
            return false
        }
        _sections.remove(section)
        section.adapter = null

        activity?.runOnUiThread {
            if (section.isNotEmpty) {
                notifyItemRangeRemoved(index, section.sizeVisible)
            }
        }
        return true
    }

    fun remove(item: FormItem): Boolean {
        if (item is FormItemSection) {
            return remove(item)
        }
        return item.section?.remove(item) ?: false
    }

    /**
     * ensure the item is visible
     * @param item item to show
     */
    fun ensureVisible(item: FormItem) {
        val index = indexOf(item)
        if (index != -1) {
            activity?.runOnUiThread {
                recyclerView?.smoothScrollToPosition(index)
            }
        }
    }

    /**
     * go to the next text item and show keyboard
     * @param item the item to start
     * @param next if true, go to the next text item; otherwise, go the the previous item
     */
    fun goToNextEdit(item: FormItem, next: Boolean = true) {
        var index = indexOf(item)
        while (index != -1) {
            index += if (next) 1 else -1
            // TODO: itemBy() is O(n), could be slow
            val nextItem = itemBy(index) ?: break
            if (nextItem is FormItemText && !nextItem.readOnly) {
                ensureVisible(nextItem)
                nextItem.focused(true)
                updateItem(nextItem)
                break
            }
        }
    }

    fun getAllItems(includeHidden: Boolean = false) = _sections.flatMap { section -> section.items.filter { (!it.hidden || includeHidden) && it.canEvaluate } }

    fun validateAllItems() = getAllItems().map { formItem ->
        val failedRules = formItem.rules.filter { it.enforceOnHidden || !formItem.hidden }.filterNot { it.validate(formItem) }
        formItem.combinedEvaluationValue = failedRules.isEmpty()
        formItem.combinedFailureMessage = failedRules.joinToString { it.failureMessage() }
        updateItem(formItem)
        Pair(formItem.tag, formItem.combinedFailureMessage)
    }
}