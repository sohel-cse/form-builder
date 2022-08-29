package com.feiyilin.form.signature;

import androidx.appcompat.app.AppCompatActivity;

public class FormSignatureActivity extends AppCompatActivity /*implements SignaturePad.OnSignedListener, SignatureActivityListener*/ {

   /* private static final String TAG = FormSignatureActivity.class.getSimpleName();
    public static final String INTENT_KEY_BITMAP = "INTENT_KEY_BITMAP";
    private String bitmap;

    @Inject
    AppExecutors appExecutors;

    @Inject
    WorkerSubmissionDao workerSubmissionDao;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_form_signature;
    }

    @Override
    protected String getClassTag() {
        return TAG;
    }

    @Override
    protected Activity getActivityContext() {
        return FormSignatureActivity.this;
    }

    @Override
    public View getContainer() {
        return dataBinding.container;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar(dataBinding.toolbar, true);
        getSupportActionBar().setTitle(TranslationUtils
                .translate("Signature"));

        if (getIntent() != null) {
            bitmap = getIntent().getStringExtra(INTENT_KEY_BITMAP);
        }

        try {
            String cleanImage = bitmap.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "");
            byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (decodedBitmap != null) dataBinding.signaturePad.setSignatureBitmap(decodedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataBinding.signaturePad.setOnSignedListener(this);
        dataBinding.setCallback(this);

    }

    @Override
    protected boolean shouldHaveOptionsMenu() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_create_project, menu);
//        return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            onSignatureDone();
        }
        return true;
    }

    public static Intent newIntent(Context context, String bitmap) {
        Intent intent = new Intent(context, FormSignatureActivity.class);
        intent.putExtra(INTENT_KEY_BITMAP, bitmap);
        return intent;
    }


    @Override
    public void onStartSigning() {
        Log.e(TAG, "onStartSigning: ");
    }

    @Override
    public void onSigned() {
        Log.e(TAG, "onSigned: ");

    }

    @Override
    public void onClear() {
        Log.e(TAG, "onClear: ");
    }

    @Override
    public void onSignatureClear() {
        dataBinding.signaturePad.clear();
    }

    @Override
    public void onSignatureDone() {
        Bitmap bitmap = dataBinding.signaturePad.getSignatureBitmap();
        if (bitmap != null) {
            Log.e(TAG, "onSignatureDone: bitmap");
//            saveSignature(bitmap);
            convertToBase64(bitmap);
        }
    }

    *//**
     * reduces the size of the image
     *
     * @param image
     * @param maxSize
     * @return
     *//*
    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapWidthRatio = (float) width / (float) 500;
        if (bitmapWidthRatio > 1) {
            width = 500;
            height = (int) (image.getHeight() / bitmapWidthRatio);
        } else {
            height = image.getHeight();
            width = image.getWidth();
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void convertToBase64(Bitmap bitmap) {
        dataBinding.setStatus(Status.LOADING);
        new AsyncTask<Bitmap, Void, String>() {

            @Override
            protected String doInBackground(Bitmap... bitmaps) {
                Bitmap bitmap1 = bitmaps[0];
                if (bitmap1 != null) {
                    bitmap1 = getResizedBitmap(bitmap1);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 0 *//*ignored for PNG*//*, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    Log.e(TAG, "doInBackground: byte size: " + bitmapdata.length);
                    String encoded = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                    if (encoded.indexOf("data:") < 0) {
                        encoded = "data:image/png;base64," + encoded;
                    }
                    return encoded;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String encoded) {
                Log.e(TAG, "onPostExecute:base64:  " + encoded);
                setResult(RESULT_OK, new Intent().putExtra("bitmap", encoded));
                finish();
            }
        }.execute(bitmap);
    }

    private void saveSignature(Bitmap bitmap) {
        dataBinding.setStatus(Status.LOADING);
        new AsyncTask<Bitmap, Void, String>() {

            @Override
            protected String doInBackground(Bitmap... bitmaps) {
                Bitmap bitmap1 = bitmaps[0];
                if (bitmap1 != null) {
                    File f = new File(getFilesDir(), "signature.png");
                    try {
                        f.createNewFile();

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0 *//*ignored for PNG*//*, bos);
                        byte[] bitmapdata = bos.toByteArray();

                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                        return f.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(String path) {
                if (TextUtils.isEmpty(path)) {
                    return;
                } else {
                    setResult(RESULT_OK, new Intent().putExtra("path", path));
                    finish();
                }
            }
        }.execute(bitmap);
    }

*/
}
