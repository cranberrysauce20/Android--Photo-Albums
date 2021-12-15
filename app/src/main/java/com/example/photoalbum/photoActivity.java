package com.example.photoalbum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;

public class photoActivity  extends AppCompatActivity {
    private ArrayList<Album> albums;
    private Album album;
    private Photo photo;
    private ListView listView;
    private ImageView imageView;
    private String path;
    private int albumpos, photopos;
    Button prev, next, AddTag, DeleteTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = this.getApplicationInfo().dataDir+ "/albums.dat";
        //setContentView(R.layout.content_photo);
        setContentView(R.layout.new_photo);

        Intent intent = getIntent();
        albums = (ArrayList<Album>) intent.getSerializableExtra("albums");
        albumpos = intent.getIntExtra("albumPosition", 0);
        album = albums.get(albumpos);
        photopos = intent.getIntExtra("photoPosition", 0);
        photo = album.get_photos().get(photopos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<Tag> adapter = new ArrayAdapter<>(this, R.layout.album, photo.get_tags());
        adapter.setNotifyOnChange(true);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);

        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(photo.getBitmap());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setItemChecked(i, true);
            }
        });

        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        AddTag = findViewById(R.id.AddTag);
        DeleteTag = findViewById(R.id.DeleteTag);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photopos = photopos > 0 ? photopos - 1 : album.get_photos().size() - 1;
                photo = album.get_photos().get(photopos);
                imageView.setImageBitmap(photo.getBitmap());
                ArrayAdapter<Tag> adapter = new ArrayAdapter<>(photoActivity.this, R.layout.album, photo.get_tags());
                listView.setAdapter(adapter);
                listView.setItemChecked(0, true);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photopos = photopos < album.get_photos().size() - 1 ? photopos + 1 : 0;
                photo = album.get_photos().get(photopos);
                imageView.setImageBitmap(photo.getBitmap());
                ArrayAdapter<Tag> adapter = new ArrayAdapter<>(photoActivity.this, R.layout.album, photo.get_tags());
                listView.setAdapter(adapter);
                listView.setItemChecked(0, true);

            }
        });
        AddTag.setOnClickListener(view -> add_tag());
        DeleteTag.setOnClickListener(view -> remove_tag());



    }
    private void add_tag(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<Tag> adapter = (ArrayAdapter<Tag>) listView.getAdapter();
        final EditText input = new EditText(this);
        final Tag tag = new Tag("person", "");

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setFocusableInTouchMode(true);
        input.requestFocus();
        builder.setView(input);
        String[] tag_types = {"person", "location"};
        builder.setSingleChoiceItems(tag_types, 0, (dialog, id) -> tag.set_name(id == 0 ? "person" : "location"))
                .setPositiveButton("Add", (dialog, which) -> {
                    tag.set_value(input.getText().toString());

                    for (int i = 0; i < adapter.getCount(); i++)
                        if (tag.equals(adapter.getItem(i))) {
                            Toast.makeText(this, "Tag already exists.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    adapter.add(tag);
                    try {
                        saveAndWriteData.writeApp(albums, path);
                    } catch (IOException e) {

                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
    private void remove_tag(){
        ArrayAdapter<Tag> adapter = (ArrayAdapter<Tag>) listView.getAdapter();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int pos = listView.getCheckedItemPosition();

        if (adapter.getCount() == 0) {
            Toast.makeText(this, "No Tags to delete", Toast.LENGTH_LONG).show();
            return;
        }
        final Tag checkedTag = adapter.getItem(pos);

        builder.setMessage("Are you sure?")
            .setPositiveButton("Yes", (dialog, id) -> {
                        adapter.remove(checkedTag);
                        try {
                            saveAndWriteData.writeApp(albums, path);
                        } catch (IOException e) {

                        }
                        listView.setItemChecked(pos, true);
                    });

        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem it) {
        switch (it.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, albumActivity.class);
                intent.putExtra("albums", albums);
                intent.putExtra("albumPosition", albumpos);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(it);
    }

}
