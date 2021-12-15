package com.example.photoalbum;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button addButton,openButton, deleteButton, renameButton, searchButton;
    ListView listView;
    private ArrayList<Album> albums;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        path = this.getApplicationInfo().dataDir+ "/albums.dat";
        File f = new File(path);

        if (!f.exists() || !f.isFile()) { // if file doesnt exist or isnt a file, add stock user
            try {
                f.createNewFile();
                albums = new ArrayList<Album>();
                saveAndWriteData.writeApp(albums, path);
            } catch (Exception e) {

            }
        }
        try{
            albums = saveAndWriteData.readApp(path);
        }catch(Exception e){

        }

        addButton = findViewById(R.id.addButton);
        openButton = findViewById(R.id.openButton);
        deleteButton = findViewById(R.id.deleteButton);
        renameButton = findViewById(R.id.renameButton);
        searchButton = findViewById(R.id.searchButton);

        ArrayAdapter<Album> adapter = new ArrayAdapter<Album>(this,R.layout.album, albums);
        adapter.setNotifyOnChange(true); //figure it out tomorrow
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setItemChecked(0,true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setItemChecked(i, true);
            }
        });


        addButton.setOnClickListener(view -> add_album());
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView.getAdapter().getCount() > 0) {
//                if(listView.getCheckedItemPosition() == 0){
//                    return;
//                }
                    Intent intent = new Intent(MainActivity.this, albumActivity.class);

                    intent.putExtra("albums", albums);
                    intent.putExtra("albumPosition", listView.getCheckedItemPosition());
                    startActivity(intent);
                }

            }
        });
        deleteButton.setOnClickListener(view -> remove_album());
        renameButton.setOnClickListener(view -> rename_album());
        searchButton.setOnClickListener(view -> searchAlbum());
    }
    private void searchAlbum(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LinearLayout style = new LinearLayout(MainActivity.this);
        style.setOrientation(LinearLayout.VERTICAL);

        EditText person = new EditText(MainActivity.this);
        person.setHint("Person");
        style.addView(person);

        EditText location = new EditText(MainActivity.this);
        location.setHint("Location");
        style.addView(location);

        builder.setView(style)
                .setPositiveButton("Search", (dialog, id) -> {
                    ArrayList<Photo> search_list = new ArrayList<>();
                    String person_tag = person.getText().toString(),
                            location_tag = location.getText().toString();
                    boolean added = false;

                    for (Album curr_Album : albums) {
                        for (Photo curr_Photo : curr_Album.get_photos()) {
                            for (Tag currentTag : curr_Photo.get_tags()) {
                                String tag = currentTag.get_value();
                                if (!tag.isEmpty()) {
                                    if (!person_tag.isEmpty() && tag.contains(person_tag) || !location_tag.isEmpty() && tag.contains(location_tag)) {
                                        for (Photo currentAddedPhoto : search_list) {
                                            if (currentAddedPhoto.equals(curr_Photo)) {
                                                added = true;
                                                break;
                                            }
                                        }
                                        if (!added) {
                                            search_list.add(curr_Photo);
                                            added = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (search_list.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No Results.", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        AlertDialog.Builder searchBuilder = new AlertDialog.Builder(builder.getContext());
                        PhotoAdapter adapter_pic = new PhotoAdapter(builder.getContext(), R.layout.photo, search_list);
                        ListView searchView = new ListView(builder.getContext());
                        searchView.setAdapter(adapter_pic);
                        searchBuilder.setView(searchView).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog popup = builder.create();
        popup.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        popup.show();
    }
    private void add_album(){
//        if(listView.getCheckedItemPosition() == 0){
//            return;
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText new_name = new EditText(this);
        ArrayAdapter<Album> adapter = (ArrayAdapter<Album>) listView.getAdapter();

        new_name.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(new_name);
        builder.setMessage("Enter Album").setPositiveButton("Add", (dialog,id) -> {
                    String album_name = new_name.getText().toString();
                    Album newAlbum = new Album(album_name);

                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (album_name.trim().equals(adapter.getItem(i).toString().trim())) {
                            Toast.makeText(this, "Album already exists, Try Again", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    adapter.add(newAlbum);
                    ArrayList<Album> save_albums = new ArrayList<Album>();

                    for (int i = 0; i < adapter.getCount(); i++)
                        save_albums.add(adapter.getItem(i));

                    try {
                        saveAndWriteData.writeApp(save_albums, path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

    AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
    private void rename_album(){
//        if(listView.getCheckedItemPosition() == 0){
//            return;
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText new_name = new EditText(this);
        ArrayAdapter<Album> adapter = (ArrayAdapter<Album>) listView.getAdapter();

        new_name.setInputType(InputType.TYPE_CLASS_TEXT);
        new_name.setText(adapter.getItem(listView.getCheckedItemPosition()).toString());
        new_name.setSelection(new_name.getText().length());
        builder.setView(new_name);

        builder.setMessage("Rename Album").setPositiveButton("Rename", (dialog,id) -> {
            String album_name = new_name.getText().toString();

            for (int i = 0; i < adapter.getCount(); i++) {
                if (album_name.trim().equals(adapter.getItem(i).toString().trim())) {
                    Toast.makeText(this, "Album already exists, Try Again", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            adapter.getItem(listView.getCheckedItemPosition()).setName(album_name);
            adapter.notifyDataSetChanged();

            ArrayList<Album> save_albums = new ArrayList<Album>();
            for (int i = 0; i < adapter.getCount(); i++)
                save_albums.add(adapter.getItem(i));

            try {
                saveAndWriteData.writeApp(save_albums, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

    }
    private void remove_album(){
//        if(listView.getCheckedItemPosition() == 0){
//            return;
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayAdapter<Album> adapter = (ArrayAdapter<Album>) listView.getAdapter();
        Album curr_album = adapter.getItem(listView.getCheckedItemPosition());

        builder.setMessage("Are you sure?").setPositiveButton("Yes", (dialog,id) -> {
            adapter.remove(curr_album);

            ArrayList<Album> save_albums = new ArrayList<Album>();
            for (int i = 0; i < adapter.getCount(); i++)
                save_albums.add(adapter.getItem(i));

            try {
                saveAndWriteData.writeApp(save_albums, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int num = 0;
            if(listView.getCheckedItemPosition() == 0){
                num = 1;
            }
            listView.setItemChecked(listView.getCheckedItemPosition()-1, true);
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}