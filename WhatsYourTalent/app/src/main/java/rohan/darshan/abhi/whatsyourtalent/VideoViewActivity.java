package rohan.darshan.abhi.whatsyourtalent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class VideoViewActivity extends ActionBarActivity {

    String title, name, category, views, likes, description, imageurl;

    TextView titleTv, UploaderTv, CategoryTv, ViewsTv, LikeTv, DescriptionTv;
    Button likeButton;
    ImageView thumbnail;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        getinfo();
        init();


    }

    private void init() {
        titleTv = (TextView) findViewById(R.id.titleTV);
        DescriptionTv = (TextView) findViewById(R.id.descriptionTV);
        UploaderTv = (TextView) findViewById(R.id.uploadedUserTV);
        CategoryTv = (TextView) findViewById(R.id.categoryTv);
        ViewsTv = (TextView) findViewById(R.id.viewsTV);
        LikeTv = (TextView) findViewById(R.id.likesTv);
        pb = (ProgressBar) findViewById(R.id.pb);
        thumbnail = (ImageView) findViewById(R.id.thumbnailIV);
        new downloadImageTask(thumbnail, pb).execute(imageurl);
        likeButton = (Button) findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                likeButton.setBackgroundResource(R.drawable.star_gold);
            }
        });

        titleTv.setText(title);
        DescriptionTv.setText(description);
        UploaderTv.setText(name);
        CategoryTv.setText(category);
        ViewsTv.setText(views);
        LikeTv.setText(likes);


    }

    private void getinfo() {
        Intent in = getIntent();
        Bundle recievedBundle = in.getBundleExtra(TimeLineActivity.VIDEO_BUNDLE);
        title = recievedBundle.getString(TimeLineActivity.VIDEO_TITLE);
        name = recievedBundle.getString(TimeLineActivity.VIDEO_USER);
        category = recievedBundle.getString(TimeLineActivity.VIDEO_CATEGORY);
        views = recievedBundle.getString(TimeLineActivity.VIDEO_VIEWS);
        likes = recievedBundle.getString(TimeLineActivity.VIDEO_LIKES);
        description = recievedBundle.getString(TimeLineActivity.VIDEO_DESCRIPTION);
        imageurl = recievedBundle.getString(TimeLineActivity.VIDEO_IMAGE_URL);
    }

    public class downloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bitmapImage;
        ProgressBar pb;

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        public downloadImageTask(ImageView bitmapImage, ProgressBar pbSmall) {
            this.bitmapImage = bitmapImage;
            this.pb = pbSmall;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String url = params[0];
            url = "http://upload.wikimedia.org/wikipedia/en/8/83/So_Oregon_Spartans_logo.png";
            Bitmap bitmap = null;

            try {
                InputStream in = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bitmapImage.setImageBitmap(bitmap);
            pb.setVisibility(View.GONE);

        }
    }


}
