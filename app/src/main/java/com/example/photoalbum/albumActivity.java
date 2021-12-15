package com.example.photoalbum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class albumActivity extends AppCompatActivity {
    private ArrayList<Album> albums;
    private Album checkedAlbum;
    private ListView listView;
    private String path;
    private int albumPosition = 0;
    Button addPhoto, removePhoto, displayPhoto, movePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_album);
        path = this.getApplicationInfo().dataDir + "/albums.dat";
        Intent intent = getIntent();
        albums = (ArrayList<Album>) intent.getSerializableExtra("albums");
        albumPosition = intent.getIntExtra("albumPosition", 0);
        checkedAlbum = albums.get(albumPosition);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbar1= (TextView) findViewById(R.id.toolbar_title);
        toolbar1.setText(checkedAlbum.toString());

        PhotoAdapter adapter = new PhotoAdapter(this, R.layout.photo, checkedAlbum.get_photos());
        adapter.setNotifyOnChange(true);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                listView.setItemChecked(i, true);
            }
        });

        addPhoto = findViewById(R.id.addPhoto);
        removePhoto = findViewById(R.id.removePhoto);
        displayPhoto = findViewById(R.id.displayPhoto);
        movePhoto = findViewById(R.id.movePhoto);

        addPhoto.setOnClickListener(view -> add_photo());
        removePhoto.setOnClickListener(view -> remove_photo());
        displayPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView.getAdapter().getCount() > 0) {
                    Intent intent = new Intent(albumActivity.this, photoActivity.class);

                    intent.putExtra("albums", albums);
                    intent.putExtra("albumPosition", albumPosition);
                    intent.putExtra("photoPosition", listView.getCheckedItemPosition());
                    startActivity(intent);
                }
            }
        });
        movePhoto.setOnClickListener(view -> move_photo());
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri uri = data.getData();

                    Bitmap image = null;
                    try {
                        ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
                        FileDescriptor fd = pfd.getFileDescriptor();
                        image = BitmapFactory.decodeFileDescriptor(fd);
                        pfd.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    String caption = uri.getLastPathSegment();
                    Photo photo = new Photo(caption, image);
                    PhotoAdapter adapter = (PhotoAdapter) listView.getAdapter();

                    adapter.add(photo);

                    try {
                        saveAndWriteData.writeApp(albums, path);
                    } catch (IOException e) {

                    }

                }
            }
        });

    public void add_photo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
    }
    private void remove_photo(){
        PhotoAdapter adapter = (PhotoAdapter) listView.getAdapter();
        if (adapter.getCount() == 0) {
            Toast.makeText(this, "No Photos to delete", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int pos = listView.getCheckedItemPosition();
        Photo currPhoto = adapter.getItem(pos);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", (dialog, id) -> {
            adapter.remove(currPhoto);

            try {
                saveAndWriteData.writeApp(albums, path);
            } catch (IOException e) {

            }
            int num = 0;
            if(listView.getCheckedItemPosition() == 0){
                num = 1;
            }
            listView.setItemChecked(listView.getCheckedItemPosition() - 1 , true);
        });

        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        builder.show();
    }
    private void move_photo(){
        ArrayList<String> all_albums = new ArrayList<String>();

        for (Album currentAlbum : albums) {
            if (!currentAlbum.toString().equals(checkedAlbum.toString()))
                all_albums.add(currentAlbum.toString());
        }

        CharSequence[] albumnames = all_albums.toArray(new CharSequence[all_albums.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        PhotoAdapter adapter = (PhotoAdapter) listView.getAdapter();
        Album dest = new Album(albumnames[0].toString());

        builder.setSingleChoiceItems(albumnames, 0, (dialog, id) -> dest.setName(albumnames[id].toString()))
                .setPositiveButton("Move", (dialog, which) -> {
                    Photo photo = adapter.getItem(listView.getCheckedItemPosition());
                    adapter.remove(photo);

                    for (Album currentAlbum : albums) {
                        if (currentAlbum.toString().equals(dest.toString())) {
                            for (Photo currentPhoto : currentAlbum.get_photos()) {
                                if (currentPhoto.equals(photo)) {
                                    Toast.makeText(this, "Photo already exists", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            currentAlbum.get_photos().add(photo);
                            try {
                                saveAndWriteData.writeApp(albums, path);
                            } catch (IOException e) {

                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem it) {
        switch (it.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(it);
    }
}
