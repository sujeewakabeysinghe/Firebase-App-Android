package com.example.firebasesapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;
    private Button b1,b2;
    private EditText et1;
    private TextView tv1;
    private ImageView iv1;
    private ProgressBar pb1;
    private Uri imageUri;
    private StorageReference sr;
    private DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
        et1=findViewById(R.id.et1);
        tv1=findViewById(R.id.tv1);
        iv1=findViewById(R.id.iv1);
        pb1=findViewById(R.id.pb1);
        sr= FirebaseStorage.getInstance().getReference();
        dbr= FirebaseDatabase.getInstance().getReference("uploads");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });

    }

    private void openFileChooser(){
        Intent in=new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).into(iv1);
            //iv1.setImageURI(imageUri);
        }

    }

    private String getFileExtention(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(){
        if(imageUri!=null){
            StorageReference fr=sr.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));
            fr.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler h=new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb1.setProgress(0);
                        }
                    },5000);
                    Toast.makeText(MainActivity.this,"Successfully Uploaded!",Toast.LENGTH_SHORT).show();
                    Upload u=new Upload(et1.getText().toString().trim(), taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    String uID=dbr.push().getKey();
                    dbr.child(uID).setValue(u);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity. this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double pr=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pb1.setProgress((int)pr);
                }
            });
        }else {
            Toast.makeText(this,"no file selected!",Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagesActivity(){
        Intent in=new Intent(this,ImagesActivity.class);
        startActivity(in);
    }

}
