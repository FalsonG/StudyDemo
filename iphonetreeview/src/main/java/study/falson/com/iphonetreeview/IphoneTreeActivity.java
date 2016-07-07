package study.falson.com.iphonetreeview;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import study.falson.com.API.GitAPI;
import study.falson.com.Mode.GitModel;

public class IphoneTreeActivity extends AppCompatActivity {
    Button click;
    TextView tv;
    EditText edit_user;
    String API = "https://api.github.com";  // BASE URL
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iphone_tree);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        click = (Button) findViewById(R.id.custom_button);
        tv = (TextView) findViewById(R.id.custom_label);
        edit_user = (EditText) findViewById(R.id.custom_editText);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edit_user.getText().toString();

                // Retrofit section start from here...
                // create an adapter for retrofit with base url
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();

                // creating a service for adapter with our GET class
                GitAPI git = restAdapter.create(GitAPI.class);

                // Now ,we need to call for response
                // Retrofit using gson for JSON-POJO conversion

                git.getFeed(user,new Callback<GitModel>() {
                    @Override
                    public void success(GitModel gitmodel, Response response) {
                        // we get json object from github server to our POJO or model class

                        tv.setText("Github Name :" + gitmodel.getName() +
                                "\nWebsite :"+gitmodel.getBlog() +
                                "\nCompany Name :"+gitmodel.getCompany());

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tv.setText(error.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "IphoneTree Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://study.falson.com.iphonetreeview/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "IphoneTree Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://study.falson.com.iphonetreeview/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
