package esa.mylibrary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import esa.mylibrary.R;

public class BaseFragment extends Fragment {


    //region 工具方法
    //显示信息
    protected void ShowToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //显示信息
    protected void ShowToastMessageLongTime(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    //endregion





    private final static String TAG="FragementDemo";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, this.getClass()+"--onAttach:");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, this.getClass()+"--onCreate:");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, this.getClass()+"--onActivityCreated:");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, this.getClass()+"--onStart:");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, this.getClass()+"--onResume:");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, this.getClass()+"--onPause:");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, this.getClass()+"--onStop:");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, this.getClass()+"--onDestroyView:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, this.getClass()+"--onDestroy:");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, this.getClass()+"--onDetach:");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_osm, null);

        Log.d(TAG, this.getClass()+"--onCreateView:");
        return view;

    }

}
