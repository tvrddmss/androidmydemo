package esa.mylibrary.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import esa.mylibrary.R;
import esa.mylibrary.databinding.FragmentListBinding;
import esa.mylibrary.uicomponent.MyRecyclerView;
import esa.mylibrary.utils.log.MyLog;

public class ListFragment extends BaseFragment {


    //数据模型
    private ListViewModel mViewModel;
    //xml绑定实例
    private FragmentListBinding binding;
    //当前页面view
    private View view;

    private MyRecyclerView lv;

    //region 静态内部类单例模式

    /**
     * 静态内部类单例
     * 优点：外部类加载的时候并不需要立即去加载内部类，内部类不被加载则不会实例化mInstance，不占内存资源，
     * 保证单例的唯一性，同时也延迟了单例的实例化。
     */
    private static class ListFragmentHolder {
        private static ListFragment mInstance = new ListFragment();
    }

    /**
     * @return esa.mylibrary.gps.GpsConfig
     * @description
     * @author Administrator
     * @time 2023/04/04 16:44
     */
    public static ListFragment getInstance() {
        return ListFragment.ListFragmentHolder.mInstance;
    }

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        //设置数据绑定对象生命周期的拥有者
        binding.setLifecycleOwner(this);
        //获取当前View
        view = binding.getRoot();

        mViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        // TODO: Use the ViewModel
        //实例设置到页面绑定
        binding.setViewModel(mViewModel);
        lv = view.findViewById(R.id.lv);

//        lv.setCanRefresh(false);
//        lv.setCanLoadMore(false);

        JSONArray jsonArray = new JSONArray();
        myAdapter = new RefreshRecycleAdapter(getContext(), jsonArray);
        lv.setAdapter(myAdapter);

//        lv.setBackgroundColor(Color.BLUE);
        lv.setListener(new MyRecyclerView.IOnScrollListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            myAdapter.list = new JSONArray();
                            for (int i = 0; i < 8; i++) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("test", i);
                                myAdapter.list.put(jsonObject);
                            }
                            MyLog.d("notifyChanged");
                            myAdapter.notifyDataSetChanged();


                        } catch (Exception ex) {
                        }
                    }
                }, 3000);

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("test", myAdapter.getItemCount());
                            myAdapter.list.put(jsonObject);
                            myAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                        }
                    }
                }, 100);
            }

            @Override
            public boolean isAllData() {
                if (myAdapter.getItemCount() >= 20) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        //间隔线
        lv.addItemDecoration(new DividerItemDecoration(getContext(), 1));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        lv.showHeader();
        lv.refresh();
    }

    protected RefreshRecycleAdapter myAdapter;

    public class RefreshRecycleAdapter extends RecyclerView.Adapter<RefreshRecycleAdapter.ViewHolder> {


        private JSONArray list;

        private Context context;

        public RefreshRecycleAdapter(Context context, JSONArray list) {

            this.context = context;
            this.list = list;

        }

        @Override

        public RefreshRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item, parent, false);
            view.setBackgroundColor(Color.BLUE);
            RefreshRecycleAdapter.ViewHolder viewHolder = new RefreshRecycleAdapter.ViewHolder(view);

            return viewHolder;

        }

        @Override

        public void onBindViewHolder(ViewHolder holder, int position) {

            try {

                holder.text.setText(list.getJSONObject(position).toString());

                holder.itemView.setTag(position);
            } catch (Exception ex) {
            }

        }

        @Override

        public int getItemCount() {

            return list.length();

        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView text;

            public ViewHolder(View itemView) {

                super(itemView);

                text = itemView.findViewById(R.id.tvItem);

            }

        }

    }
}

