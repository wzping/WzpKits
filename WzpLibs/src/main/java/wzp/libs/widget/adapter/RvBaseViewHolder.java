package wzp.libs.widget.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import wzp.libs.R;
import wzp.libs.utils.GlideUtils;
import wzp.libs.widget.CircleImageView;
import wzp.libs.widget.able.MyTextWatcher;


/**
 * 通用 RecyclerView  ViewHolder
 *
 * class ViewHolder extends RecyclerView.ViewHolder{
 *     TextView tv;
 *     public ViewHolder(View itemView) {
 *         super(itemView);
 *         tv =  itemView.findViewById(R.id.tv);
 *     }
 * }
 */

public class RvBaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mSparseArray;


    RvBaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mSparseArray = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T getView(@IdRes int viewId) {
        View view = mSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);     // 使用 RecyclerView.RvBaseViewHolder 保存的 itemView
            mSparseArray.put(viewId, view);
        }
        return (T) view;
    }


    /********************************** 常用函数 ****************************************/

    public RvBaseViewHolder setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public RvBaseViewHolder setPadding(int viewId, int left, int top, int right, int bottom) {
        View view = getView(viewId);
        view.setPadding(left, top, right, bottom);
        return this;
    }

    public RvBaseViewHolder setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

    public RvBaseViewHolder setBackgroundColor(int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public RvBaseViewHolder setBackgroundRes(int viewId, @DrawableRes int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public RvBaseViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        View view = getView(viewId);
        view.setBackground(drawable);
        return this;
    }

    public RvBaseViewHolder setBackgroundBitmap(Context context,int viewId, Bitmap bitmap) {
        View view = getView(viewId);
        view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
        return this;
    }

    public RvBaseViewHolder setEnabled(int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
        return this;
    }

    public RvBaseViewHolder setEnabled(boolean enabled) {
        itemView.setEnabled(enabled);
        return this;
    }

    public boolean isEnabled(int viewId) {
        return getView(viewId).isEnabled();
    }



    /********************************** 常用监听 ****************************************/

    public RvBaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public RvBaseViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public RvBaseViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * 设置 RecyclerView item 点击监听
     * @param listener
     * @return
     */
    public RvBaseViewHolder setOnItemClickListener(View.OnClickListener listener) {
        itemView.setClickable(true);
        itemView.setOnClickListener(listener);
        return this;
    }


    public RvBaseViewHolder setOnItemTouchListener(View.OnTouchListener listener) {
        itemView.setOnTouchListener(listener);
        return this;
    }

    /**
     * 设置 RecyclerView item 长按监听
     * @param listener
     * @return
     */
    public RvBaseViewHolder onItenLongClickListener(View.OnLongClickListener listener) {
        itemView.setLongClickable(true);
        itemView.setOnLongClickListener(listener);
        return this;
    }



    /********************************** TextView 相关函数 ****************************************/

    public RvBaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public RvBaseViewHolder setTextSize(int viewId, int testSize) {
        TextView view = getView(viewId);
        view.setTextSize(testSize);
        return this;
    }

    public RvBaseViewHolder setTextColor(int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public RvBaseViewHolder setTextColorRes(Context context,int viewId, @ColorRes int color) {
        TextView view = getView(viewId);
        view.setTextColor(context.getResources().getColor(color));
        return this;
    }

    //相当于设置drawableLeft、drawableTop、drawaleRight、drawableBottom
    public RvBaseViewHolder setTextDrawable(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        TextView textView = getView(viewId);
        textView.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }


    /********************************** EditText 相关函数 ****************************************/

    public RvBaseViewHolder addTextChangedListener(int viewId, TextWatcher watcher) {
        EditText editText = getView(viewId);
        editText.addTextChangedListener(watcher);
        return this;
    }


    public RvBaseViewHolder addTextChangedListener(int viewId, MyTextWatcher myTextWatcher) {
        EditText editText = getView(viewId);
        editText.addTextChangedListener(myTextWatcher);
        return this;
    }

    /********************************** CheckBox 相关函数 ****************************************/

    public RvBaseViewHolder setOnCheckedChangeListener(int viewId, final CheckBox.OnCheckedChangeListener listener) {
        CheckBox checkBox = getView(viewId);
        checkBox.setOnCheckedChangeListener(listener);
        return this;
    }

    public RvBaseViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public boolean isChecked(int viewId) {
        CheckBox checkBox = getView(viewId);
        return checkBox.isChecked();
    }

    /********************************** ProgressBar 相关函数 ****************************************/

    public RvBaseViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public RvBaseViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    /********************************** ImageView 相关函数 *****************************************/

    public RvBaseViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public RvBaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public RvBaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public RvBaseViewHolder setUrlImage(Context context, int viewId, String imageUrl) {
        ImageView view = getView(viewId);
        GlideUtils.getInstance().loadPic(context,imageUrl,view, R.drawable.ic_error1);
        return this;
    }

   public RvBaseViewHolder setUrlImage(Context context, int viewId, String imageUrl, int defaultPic) {
        ImageView view = getView(viewId);
        GlideUtils.getInstance().loadPic(context,imageUrl,view,defaultPic);
        return this;
    }


    public RvBaseViewHolder setUrlImageList(Context context,int viewId, String imageUrl) {
        ImageView view = getView(viewId);
        GlideUtils.getInstance().loadPicList(context,imageUrl,view, R.drawable.ic_error1);
        return this;
    }


    public RvBaseViewHolder setUrlImageList(Context context,int viewId, String imageUrl,int defaultPic) {
        ImageView view = getView(viewId);
        GlideUtils.getInstance().loadPicList(context,imageUrl,view,defaultPic);
        return this;
    }


    public RvBaseViewHolder setUrlCircleImage(Context context,int viewId, String imageUrl,int defaultPic) {
        CircleImageView view = getView(viewId);
        GlideUtils.getInstance().loadPic(context,imageUrl,view,defaultPic);
        return this;
    }

    /********************************** RadioGroup 相关函数 *****************************************/

    public RvBaseViewHolder removeAllRadiogroupViews(int viewId){
        RadioGroup radioGroup = getView(viewId);
        radioGroup.removeAllViews();
        return this;
    }

    public RvBaseViewHolder radiogroupAddView(int viewId,View view){
        RadioGroup radioGroup = getView(viewId);
        radioGroup.addView(view);
        return this;
    }


    /********************************** LinearLayout 相关函数 *****************************************/

    public RvBaseViewHolder removeAllLinearViews(int viewId) {
        LinearLayout linearLayout = getView(viewId);
        linearLayout.removeAllViews();
        return this;
    }


    public RvBaseViewHolder linearAddView(int viewId,View view) {
        LinearLayout linearLayout = getView(viewId);
        linearLayout.addView(view);
        return this;
    }
}
