package android.com.changyou;

import android.com.changyou.imgload.ImgTools;
import android.com.changyou.imgload.ImgUri;
import android.com.changyou.imgload.LoadDialog;
import android.com.changyou.imgload.LoadService;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.VolleyUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoadImgActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO = 1;

    private static final int SCALE = 5;// 照片缩小比例
    private String uploadFile;

    private ImageView imageView;
    private TextView tvusername;



    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_img);

        imageView = (ImageView)findViewById(R.id.imageView);
        tvusername = (TextView)findViewById(R.id.userName);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String imgPath = intent.getStringExtra("imgPath");

        tvusername.setText(username);

        ImageLoader imageLoader =
                VolleyUtil.getInstance(this).getImageLoader();
        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(imageView,
                        R.drawable.logo2,
                        R.drawable.logo2);
        imageLoader.get(VolleyUtil.BASE_URL + imgPath, listener);

        // 工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 应用栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("更改头像");
        actionBar.setHomeAsUpIndicator(R.drawable.left);


    }

    public void choosePhotoClick(View view) {
        // 显示相片操作(0 拍照 / 1 选择相片)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("图片来源:");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case TAKE_PHOTO:
                                Intent openCameraIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri imageUri = Uri.fromFile(new File(Environment
                                        .getExternalStorageDirectory(), "image.jpg"));
                                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(openCameraIntent, TAKE_PHOTO);
                                break;

                            case CHOOSE_PHOTO:
                                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                openAlbumIntent.setType("image/*");
                                startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void uploadImageClick(View view) {
        // 上传
        if (TextUtils.isEmpty(uploadFile)) {
            Toast.makeText(this, "请先选择一张头像", Toast.LENGTH_SHORT).show();
        } else {
            // 上传图片
            String name = "userimg";
            String requestUrl = VolleyUtil.BASE_URL + "UploadJsonServlet?name="+name;
            File file = new File(uploadFile);
            LoadDialog loadDialog = new LoadDialog(this);
            // (回调处理方法,上传图片类,指定上传图片类中的方法,请求服务器的URL路径,本地需上传的文件)
            loadDialog.execute(new LoadDialog.Callback() {
                @Override
                public void getResult(Object obj) {
                    // do nothing
                }
            }, LoadService.class, "postUseUrlConnection", requestUrl, file);

            final String userimgpath = "images" +"/" +name + uploadFile.substring(
                    uploadFile.lastIndexOf("/"),
                    uploadFile.length());

            // 提交表单数据到服务器
            requestQueue = Volley.newRequestQueue(this);
            stringRequest = new StringRequest(
                    // 请求服务器的方式(POST/GET)
                    Request.Method.POST,
                    // 请求服务器的URL路径
                    VolleyUtil.getAbsoluteUrl("UserJsonServlet?action=modify"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            // 响应成功时处理方法
                            Toast.makeText(getApplicationContext(),
                                    s, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // 响应出错时处理方法
                            ChangYouUtil.toast(getApplicationContext(),"响应失败");
                        }
                    }) {
                // 以内部类的形式提交表单数据
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", tvusername.getText().toString());
                    // map.put("image", "images/qq.jpg");
                    map.put("userimgpath", userimgpath);
                    return map;
                }
            };

            int uid = ChangYouUtil.getUserId(this);
            String uname = ChangYouUtil.getUsername(this);
            String rname = ChangYouUtil.getRealname(this);
            String model = ChangYouUtil.getModel(this);

            ChangYouUtil.savePreferences(this,uid,uname,model,rname,userimgpath);

            stringRequest.setTag(VolleyUtil.TAG);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    // 将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment
                            .getExternalStorageDirectory() + "/image.jpg");
                    Bitmap newBitmap = ImgTools.zoomBitmap(bitmap, bitmap.getWidth()
                            / SCALE, bitmap.getHeight() / SCALE);
                    // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    // 将处理过的图片显示在界面上，并保存到本地
                    imageView.setImageBitmap(newBitmap);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                    String filename = sdf.format(new Date());

                    ImgTools.savePhotoToSDCard(newBitmap, Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            .getAbsolutePath()
                            + "/Camera", "IMG_" + filename);
                    uploadFile = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            .getAbsolutePath()
                            + "/Camera/" + "IMG_" + filename + ".png";
                    break;

                case CHOOSE_PHOTO:
                    ContentResolver resolver = getContentResolver();
                    // 照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        // 使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
                                originalUri);
                        if (photo != null) {
                            // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImgTools.zoomBitmap(photo, photo.getWidth()
                                    / SCALE, photo.getHeight() / SCALE);
                            // 释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            imageView.setImageBitmap(smallBitmap);
                            uploadFile = ImgUri.getImageAbsolutePath(this, originalUri);
                        }
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(this, "读取文件,出错啦", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            Log.v(TAG, "............." + uploadFile);
        }
    }
}
