package io.osman.football.league.helper;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.squareup.picasso.Picasso;

public class ConstantUtils {

    public static final String GLIDE_TIMEOUT = "com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout";

    public static final String API_KEY_PARAM = "X-Auth-Token";
    public static final String API_KEY = "d4f35e41e5ee4718b58eda10bc39acd3";
    public static final String FOOTBALL_API_URL = "https://api.football-data.org/v2/";


    public static void loadImage(Context context, String url, ImageView imageView) {
        if (url.contains(".svg"))
            GlideToVectorYou.justLoadImage((Activity) context, Uri.parse(url), imageView);
        else Picasso.get().load(url).into(imageView);
    }
}
