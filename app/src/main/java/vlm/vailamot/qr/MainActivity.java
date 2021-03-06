package vlm.vailamot.qr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn,btn2,btnGet;
    TextView tv;
    ImageView iv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        btn2 = (Button) findViewById(R.id.btn2);
        tv = (TextView)  findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        et = (EditText) findViewById(R.id.et);
        btnGet = (Button) findViewById(R.id.btnGet);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(et.getText().toString(), BarcodeFormat.QR_CODE, 400, 400);
                    ImageView imageViewQrCode = (ImageView) findViewById(R.id.iv);
                    imageViewQrCode.setImageBitmap(bitmap);
                } catch(Exception e) {

                }
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openChrome(view);
                String s = tv.getText().toString();
                open(s);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("value",tv.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_LONG).show();
            }
        });

    }

    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    tv = (TextView) findViewById(R.id.tv);
                    tv.setText(result.getContents());
                }
            });
    // Launch
    public void onButtonClick(View view) {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a QR code");
        barcodeLauncher.launch(options);
    }

    public boolean check(String s) {
        try {
            URI u = new URL(s).toURI();
            return true;
        } catch (Exception e){
            return  false;
        }
    }

    public void open(String s) {
        if (check(s)) {
            Uri uri = Uri.parse(s);
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }else {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
            startActivity(intent);
        }



    }





}