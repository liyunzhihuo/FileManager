package com.xiao.filemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiao.filemanager.bga_photopicker.model.BGAPhotoFolderModel;
import com.xiao.filemanager.bga_photopicker.util.BGAAsyncTask;
import com.xiao.filemanager.bga_photopicker.util.BGALoadPhotoTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSelectActivity extends AppCompatActivity implements BGAAsyncTask.Callback<ArrayList<BGAPhotoFolderModel>> {

    private static final int SELECT_IMAGE_SIZE = 9;
    private BGAPhotoFolderModel mCurrentPhotoFolderModel;

    /**
     * 是否可以拍照
     */
    private boolean mTakePhotoEnabled;
    private BGALoadPhotoTask mLoadPhotoTask;

    private RecyclerView recyclerView;
    /**
     * 图片目录数据集合
     */
    private ArrayList<BGAPhotoFolderModel> mPhotoFolderModels;

    private ImageLoaderAdapter imageLoaderAdapter;
    private List<ImageModel> imageModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_file_act);
        intUI();


    }

    private void intUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imageLoaderAdapter = new ImageLoaderAdapter(imageModels, this);
        recyclerView.setAdapter(imageLoaderAdapter);
        Log.e("TestSelectActivity", "main thread id" + Thread.currentThread());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoadPhotoTask = new BGALoadPhotoTask(this, this, mTakePhotoEnabled).perform();
    }

    @Override
    public void onPostExecute(ArrayList<BGAPhotoFolderModel> photoFolderModels) {
        mLoadPhotoTask = null;
        mPhotoFolderModels = photoFolderModels;
        if (mPhotoFolderModels != null && mPhotoFolderModels.size() > 0) {
            mCurrentPhotoFolderModel = mPhotoFolderModels.get(0);
            if (mCurrentPhotoFolderModel == null) {
                return;
            }
            ArrayList<String> temps = mCurrentPhotoFolderModel.getPhotos();
            if (temps != null) {
                imageModels.clear();
                ImageModel imageModel;
                for (String string : temps) {
                    imageModel = new ImageModel(string, false);
                    imageModels.add(imageModel);
                }
                imageLoaderAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onTaskCancelled() {

    }


    protected class ImageLoaderAdapter extends RecyclerView.Adapter<ImageLoaderAdapter.ImageLoadViewHolder> {

        private List<ImageModel> mDatas;
        private Context mContext;
        private HashMap<String, ImageModel> selectImgs = new HashMap<>();

        public ImageLoaderAdapter(List<ImageModel> mDatas, Context mContext) {
            this.mDatas = mDatas;
            this.mContext = mContext;
        }


        @NonNull
        @Override
        public ImageLoadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_img_item, null);
            return new ImageLoadViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ImageLoadViewHolder viewHolder, final int position) {
            final ImageModel imageModel = mDatas.get(position);
            if (imageModel != null) {
                Glide.with(mContext).load(imageModel.imagePath).into(viewHolder.img);
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectImgs.size() >= SELECT_IMAGE_SIZE) {
                            Toast.makeText(mContext, mContext.getString(R.string.select_img_tip), Toast.LENGTH_SHORT).show();
                        }
                        if (imageModel.isSelect) {
                            imageModel.setSelect(false);
                            selectImgs.remove(imageModel.getImagePath());
                        } else {
                            imageModel.setSelect(true);
                            selectImgs.put(imageModel.getImagePath(), imageModel);

                        }
                        notifyDataSetChanged();
                    }
                });
                viewHolder.index.setSelected(imageModel.isSelect);
            }


        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        class ImageLoadViewHolder extends RecyclerView.ViewHolder {
            private ImageView img;
            private TextView index;

            public ImageLoadViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.select_item_img);
                index = itemView.findViewById(R.id.select_item_index);
            }
        }

    }

    protected class ImageModel {
        private String imagePath;
        private boolean isSelect;

        public ImageModel(String imagePath, boolean isSelect) {
            this.imagePath = imagePath;
            this.isSelect = isSelect;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
