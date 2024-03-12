package com.roydon.community.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.roydon.community.BaseActivity;
import com.roydon.community.R;
import com.roydon.community.api.Api;
import com.roydon.community.api.ApiConfig;
import com.roydon.community.api.HttpCallback;
import com.roydon.community.domain.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class QrCodeScanActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    // handler
    private static final int HANDLER_WHAT_EMPTY = 0;
    private static final int HANDLER_WHAT_HAS_VALUE = 1;
    private static final int HANDLER_WHAT_LOGIN_CONFIRM = 2;

    private PreviewView previewView;
    private TextView tvCodeResult;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private BarcodeScanner barcodeScanner;

    private String qrCodeValue;

    private String avatar;
    private String uuid;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_WHAT_HAS_VALUE:
                    Log.e("qrCodeValue", qrCodeValue);
                    scanLogin(qrCodeValue);
                    break;
                case HANDLER_WHAT_LOGIN_CONFIRM:
                    Bundle bundle = new Bundle();
                    bundle.putString("avatar", avatar);
                    bundle.putString("uuid", uuid);
                    navigateToWithBundle(LoginConfirmActivity.class, bundle);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_qr_code_scan;
    }

    @Override
    protected void initView() {
        previewView = findViewById(R.id.preview_view);
        tvCodeResult = findViewById(R.id.tv_code_result);
        request();

        setupCamera();
    }

    @Override
    protected void initData() {

    }

    private void setupCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

                // 创建BarcodeScanner实例
                barcodeScanner = BarcodeScanning.getClient();

                // 创建ImageAnalysis实例
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

                // 设置ImageAnalysis分析器
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
                    @Override
                    @OptIn(markerClass = ExperimentalGetImage.class)
                    public void analyze(@NonNull ImageProxy image) {
                        // 获取ImageProxy对象
                        Image mediaImage = image.getImage();
                        if (mediaImage != null) {
                            // 创建InputImage对象
                            InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

                            // 使用BarcodeScanner识别二维码
                            barcodeScanner.process(inputImage).addOnSuccessListener(barcodes -> {
                                for (Barcode barcode : barcodes) {
                                    if (barcode.getFormat() == Barcode.FORMAT_QR_CODE) {
                                        qrCodeValue = barcode.getRawValue();
                                        mHandler.sendEmptyMessage(HANDLER_WHAT_HAS_VALUE);
                                        // 在UI线程中更新TextView
                                        runOnUiThread(() -> tvCodeResult.setText(qrCodeValue));
                                        image.close();
                                        // 识别到结果就关闭解析
                                        barcodeScanner.close();
                                        return;
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                // 处理扫描失败的情况
                                Log.e("QrScanActivity", "Failed to scan barcode", e);
                            }).addOnCompleteListener(task -> image.close());
                        } else {
                            image.close();
                        }
                    }
                });

                // 绑定ImageAnalysis实例到相机生命周期中
                cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("QrScanActivity", "Failed to initialize camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * 扫码登录
     *
     * @param res 二维码识别结果
     */
    private void scanLogin(String res) {
        // 校验res串的格式
        if (!isValidUrl(res)) {
            return;
        }
        if (res.startsWith(ApiConfig.BASE_URl + "/qr_login") && res.contains("uuid=")) {
            String[] split = res.split("uuid=");
            if (split[1] == null) {
                return;
            }
            String uuidRes = split[1];
            HashMap<String, Object> params = new HashMap<>();
            params.put("uuid", uuidRes);
            Api.build(ApiConfig.SCAN, params).getRequestWithToken(this, new HttpCallback() {
                @Override
                public void onSuccess(final String res) {
                    BaseResponse response = new Gson().fromJson(res, BaseResponse.class);
                    if (response != null && response.getCode() == 200) {
                        String msg = response.getMsg();
                        // 使用handler将数据传递给主线程
                        avatar = msg;
                        uuid = uuidRes;
                        mHandler.sendEmptyMessage(HANDLER_WHAT_LOGIN_CONFIRM);
                    } else {
                        finish();
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

    }

    private boolean isValidUrl(String url) {
        // 简单判断是否以"http://"或"https://"开头,使用正则表达式或其他方式
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        // 跳转到应用内浏览器

    }

    private void request() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};//可以添加其他的权限，用来判断
        //判断有没有权限
        if (EasyPermissions.hasPermissions(this, permissions)) {
            // 有权限，需要做什么
        } else {
            // 没有权限, 申请权限
            EasyPermissions.requestPermissions(this, "摄像机需要用户允许才能调用，请开启相关权限（理由）", 1, permissions);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        for (String temp : perms) {
            if (temp.equals(Manifest.permission.CAMERA)) {
                Log.e("QrCodeScanActivity", "CAMERA onPermissionsGranted");
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("QrCodeScanActivity", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        String permissions = Manifest.permission.CAMERA;
        if (perms.contains(permissions)) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this).setTitle("The permission has been denied by you").setRationale("If you do not open the permission, you cannot use this function, click OK to open the permission").setRequestCode(12345).build().show();
            }
        }
    }
}