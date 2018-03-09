package com.team.jcti.ttr.utils;

import android.content.Context;

import com.team.jcti.ttr.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import model.Color;
import model.TrainCard;

/**
 * Created by Chance on 3/7/18.
 */

public class Util {

    public static int getColorCode(TrainCard c) {

        int color = 0;

        switch (c) {
            case RED:
                color = 0xFFFF8888;
                break;
            case BLACK:
                color = 0xFF222222;
                break;
            case BLUE:
                color = 0xFF2288FF;
                break;
            case WHITE:
                color = 0xffFFFFFF;
                break;
            case PURPLE:
                color = 0xFFBB88FF;
                break;
            case YELLOW:
                color = 0xFFFFEE88;
                break;
            case ORANGE:
                color = 0xFFFFAA66;
                break;
            case GREEN:
                color = 0xFF88FF88;
                break;
            case WILD:
                color = 0xFF888888;
                break;
        }

        return color;
    }

    public static int getPlayerColorCode(Color c) {
        switch(c) {
            case BLUE:
                return 0xFF0000FF;
            case GREEN:
                return 0xFF00FF00;
            case BLACK:
                return 0xFF000000;
            case YELLOW:
                return 0xFFFFEEAA;
            case RED:
                return 0xFFFF0000;
            default:
                return 0x00000000;
        }
    }

    public static int getImage(int length) {

        int image = 0;

        switch (length){
            case 1:
                image = R.drawable.one_big;
                break;
            case 2:
                image = R.drawable.two_big;
                break;
            case 3:
                image = R.drawable.three_big;
                break;
            case 4:
                image = R.drawable.four_big;
                break;
            case 5:
                image = R.drawable.five_big;
                break;
            case 6:
                image = R.drawable.six_big;
                break;

        }

        return image;
    }

    public static String getStringFromResourceFile(Context context, int resourceCode) throws IOException {
        InputStream is = context.getResources().openRawResource(resourceCode);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        String jsonString = writer.toString();

        return jsonString;
    }


}
