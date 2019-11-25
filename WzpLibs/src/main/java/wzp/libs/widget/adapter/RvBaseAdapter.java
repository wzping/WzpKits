package wzp.libs.widget.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


/**
 * RecyclerView的adapter的基类
 * @param <T>
 */
public class RvBaseAdapter<T> extends RecyclerView.Adapter<RvBaseViewHolder> {

    //adapter所需数据
    private List<T> data = new ArrayList<>();
    //recyclerview的布局文件
    private int layoutRes;
    //设置页面监听
    private OnBindViewListener<T> bindViewListener;

    public RvBaseAdapter(int layoutRes, OnBindViewListener<T> bindViewListener) {
        this.layoutRes = layoutRes;
        this.bindViewListener = bindViewListener;
    }

    public RvBaseAdapter(@NonNull List<T> data, @LayoutRes int layoutRes, @NonNull OnBindViewListener<T> bindViewListener) {
        this.data = data;
        this.layoutRes = layoutRes;
        this.bindViewListener = bindViewListener;
    }


    //-------------- extends RecyclerView.Adapter 就要复写的方法 (3个)-----------------------

    @NonNull
    @Override
    public RvBaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new RvBaseViewHolder(inflater.inflate(layoutRes, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RvBaseViewHolder holder, int position) {
        if (bindViewListener != null) {
            bindViewListener.onBindView(holder, data.get(position), position);
        }
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    /**
     * ItemView 设置接口(相对抽象函数灵活性、扩展性更好)
     * @param <T> 数据类型
     */
    public interface OnBindViewListener<T> {
        void onBindView(RvBaseViewHolder holder, T data, int position);
    }


    public void addData(List<T> valueBeanList) {
        data = valueBeanList;
        notifyDataSetChanged();
    }


    public void setData(List<T> valueBeanList) {
        data.clear();
        data.addAll(valueBeanList);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }
}



//public class MedicalRuleAdapter extends RecyclerView.Adapter<MedicalRuleAdapter.ViewHolder> {
//    /** 当前上下文 */
//    private Context mContext;
//    private ArrayList<MedicalBeanRule> medicalRuleBeanArrayList;
//
//    public MedicalRuleAdapter(Context mContext, ArrayList<MedicalBeanRule> medicalRuleBeanArrayList){
//        this.mContext = mContext;
//        this.medicalRuleBeanArrayList = medicalRuleBeanArrayList;
//    }
//
//    //-------------- extends RecyclerView.Adapter 就要复写的方法 (3个)-----------------------
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rv_medical_rule,parent,false));
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
//        viewHolder.bean_number.setText(medicalRuleBeanArrayList.get(position).getNumber()+"");
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return medicalRuleBeanArrayList.size();
//    }
//
//
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//        TextView bean_description;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            bean_number =  itemView.findViewById(R.id.bean_number);
//        }
//    }
//}
