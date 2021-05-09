package com.chen.fy.niceshop.main.look.publish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.look.publish.data.BasePublishResponse;
import com.chen.fy.niceshop.network.DynamicService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishActivity extends TakePhotoActivity {

    private static final String TAG = "PublishActivity";

    // view
    private EditText etContent;
    private GridView gvPictures;
    private RadioGroup radioGroup;
    private BasePopupView loading;

    // data
    private ShareGridViewAdapter adapter = new ShareGridViewAdapter(this);
    private List<Object> mPictures = new ArrayList<>();

    // picture
    private TakePhoto mTakePhoto;
    private Uri mUri;

    private boolean isPublish = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish);

        ShowUtils.changeStatusBarTextImgColor(this, true);
        initView();
        initData();
        configTakePhoto();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (loading != null) {
            loading.dismiss();
        }
    }

    private void initView() {
        etContent = findViewById(R.id.et_content_publish);
        gvPictures = findViewById(R.id.gv_box_public);
        radioGroup = findViewById(R.id.rg_publish);
        setClick();
    }

    private void setClick() {
        gvPictures.setOnItemClickListener((parent, view, position, id) ->
                ShowUtils.zoomPicture(this, view, position));
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_publish).setOnClickListener(v -> doPublish());
        findViewById(R.id.ll_add_pictures).setOnClickListener(v -> addPicture());
    }

    public void initData() {
        adapter.setUris(mPictures);
        gvPictures.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void doPublish() {
        if (!isPublish) {
            showLoading();
            // content
            String content = etContent.getText().toString();
            // type
            RadioButton btn = findViewById(radioGroup.getCheckedRadioButtonId());
            String type = btn.getText().toString();
            postHeadline(content, type);
            isPublish = true;
        } else {
            Toast.makeText(this, "请勿重复点击", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        loading = new XPopup.Builder(this).asLoading("正在上传...");
        loading.setOnTouchListener((v, event) -> true);
        loading.show();
    }

    /// 发表动态
    private void postHeadline(String content, String type) {
        DynamicService service = ServiceCreator.create(DynamicService.class);
        // Authorization
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");

        RequestBody contentBody = RequestBody.create(null, content.getBytes(Util.UTF_8));
        RequestBody typeBody = RequestBody.create(null, type.getBytes(Util.UTF_8));

        setPublish(token, contentBody, typeBody);
    }

    private void setPublish(String token, RequestBody contentBody, RequestBody typeBody) {
        int size = mPictures.size();
        switch (size) {
            case 1:
                postOne(token, contentBody, typeBody);
                break;
            case 2:
                postTwo(token, contentBody, typeBody);
                break;
            case 3:
                postThree(token, contentBody, typeBody);
                break;
            default:
                break;
        }
    }

    private void postOne(String token, RequestBody contentBody, RequestBody typeBody) {
        DynamicService service = ServiceCreator.create(DynamicService.class);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), new File((String) mPictures.get(0)));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img1"
                , RUtil.toString(R.string.photo_name_1), fileBody);
        // 上传
        service.publishOne(token, contentBody, typeBody, filePart).enqueue(new Callback<BasePublishResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasePublishResponse> call
                    , @NonNull Response<BasePublishResponse> response) {
                loading.dismiss();
                BasePublishResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(PublishActivity.this
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.i(TAG, "publish response is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasePublishResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "publish failure");
                loading.dismiss();
            }
        });
    }

    private void postTwo(String token, RequestBody contentBody, RequestBody typeBody) {
        DynamicService service = ServiceCreator.create(DynamicService.class);
        // image
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("image/jpg"), new File((String) mPictures.get(0)));
        MultipartBody.Part filePart1 = MultipartBody.Part.createFormData("img1"
                , RUtil.toString(R.string.photo_name_1), fileBody1);

        RequestBody fileBody2 = RequestBody.create(MediaType.parse("image/jpg"), new File((String) mPictures.get(1)));
        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("img2"
                , RUtil.toString(R.string.photo_name_2), fileBody2);

        // 上传
        service.publishTwo(token, contentBody, typeBody, filePart1, filePart2).enqueue(new Callback<BasePublishResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasePublishResponse> call
                    , @NonNull Response<BasePublishResponse> response) {
                loading.dismiss();
                BasePublishResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(PublishActivity.this
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.i(TAG, "publish response is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasePublishResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "publish failure");
                loading.dismiss();
            }
        });
    }

    private void postThree(String token, RequestBody contentBody, RequestBody typeBody) {
        DynamicService service = ServiceCreator.create(DynamicService.class);
        // image
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("image/jpg"), new File((String) mPictures.get(0)));
        MultipartBody.Part filePart1 = MultipartBody.Part.createFormData("img1"
                , RUtil.toString(R.string.photo_name_1), fileBody1);

        RequestBody fileBody2 = RequestBody.create(MediaType.parse("image/jpg"), new File((String) mPictures.get(1)));
        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("img2"
                , RUtil.toString(R.string.photo_name_2), fileBody2);

        RequestBody fileBody3 = RequestBody.create(MediaType.parse("image/jpg"), new File((String) mPictures.get(2)));
        MultipartBody.Part filePart3 = MultipartBody.Part.createFormData("img3"
                , RUtil.toString(R.string.photo_name_3), fileBody3);

        // 上传
        service.publishThree(token, contentBody, typeBody, filePart1, filePart2, filePart3).enqueue(new Callback<BasePublishResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasePublishResponse> call
                    , @NonNull Response<BasePublishResponse> response) {
                loading.dismiss();
                BasePublishResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(PublishActivity.this
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.i(TAG, "publish response is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasePublishResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "publish failure");
                loading.dismiss();
            }
        });
    }

    private void addPicture() {
        new XPopup.Builder(this)
                .asCenterList("", new String[]{"拍照", "从相册选择"},
                        (position, text) -> {
                            switch (position) {
                                case 0:         //拍照
                                    mTakePhoto.onPickFromCapture(mUri);
                                    break;
                                case 1:         //相册
                                    mTakePhoto.onPickMultiple(3 - mPictures.size());
                                    break;
                            }
                        })
                .show();
    }

    private void configTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = getTakePhoto();

            //图片文件名
            File file = new File(this.getExternalFilesDir(null)
                    , RUtil.toString(R.string.headline_file));
            mUri = Uri.fromFile(file);

        }

    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        if (!result.getImages().isEmpty()) {
            for (int i = 0; i < result.getImages().size(); i++) {
                mPictures.add(result.getImages().get(i).getOriginalPath());
            }
            adapter.setUris(mPictures);
            gvPictures.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
