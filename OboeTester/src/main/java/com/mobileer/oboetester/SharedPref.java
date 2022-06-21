package com.mobileer.oboetester;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPref {

    private static final String OBOE_TESTER_LATENCY_Normal = "OBOE_TESTER_LATENCY_Normal";
    private static final String OBOE_TESTER_LATENCY_Normal_ISSET = "OBOE_TESTER_LATENCY_Normal_ISSET";
    private static final String OBOE_TESTER_LATENCY_Communication = "OBOE_TESTER_LATENCY_Communication";
    private static final String OBOE_TESTER_LATENCY_Communication_ISSET = "OBOE_TESTER_LATENCY_Communication_ISSET";
    private static final String FIRST_TIME = "FIRST_TIME";
    private static final String OBOE_TESTER_LATENCY_ISSET_VOICECOM_Input = "OBOE_TESTER_LATENCY_ISSET_VOICECOM_Input";
    private static final String IS_MMAP_SUPPORTED = "IS_MMAP_SUPPORTED";

    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static void setVoiceRecLatencyNormal(String Latency, Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(OBOE_TESTER_LATENCY_Normal_ISSET, true);
        editor.putString(OBOE_TESTER_LATENCY_Normal, Latency);
        editor.apply();
    }
    public static boolean isVoiceRecLatencyNormalSet(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        Boolean RNN = mPrefs.getBoolean(OBOE_TESTER_LATENCY_Normal_ISSET, false);
        return RNN;
    }
    public static String getVoiceRecLatencyNormal(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        String Latency = mPrefs.getString(OBOE_TESTER_LATENCY_Normal, "");
        return (Latency);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static void setVoiceRecLatencyCommunication(String Latency, Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(OBOE_TESTER_LATENCY_Communication_ISSET, true);
        editor.putString(OBOE_TESTER_LATENCY_Communication, Latency);
        editor.apply();
    }
    public static boolean isVoiceRecLatencyCommunicationSet(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        Boolean RNN = mPrefs.getBoolean(OBOE_TESTER_LATENCY_Communication_ISSET, false);
        return RNN;
    }
    public static String getVoiceRecLatencyCommunication(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        String Latency = mPrefs.getString(OBOE_TESTER_LATENCY_Communication, "");
        return (Latency);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////










    public static void setVoiceComLatency(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(OBOE_TESTER_LATENCY_ISSET_VOICECOM_Input, true);
        editor.apply();
    }
    public static boolean isVoiceComLatencySet(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);

        Boolean RNN = mPrefs.getBoolean(OBOE_TESTER_LATENCY_ISSET_VOICECOM_Input, false);
        return RNN;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static void setMMapSupported(String  value, Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(IS_MMAP_SUPPORTED, value);
        editor.apply();

    }
//    public static String getMMapSupported(Context con) {
//        try
//        {
//            SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
//            String Latency = mPrefs.getString(IS_MMAP_SUPPORTED, "2.0");
//            return (Latency);
//        }
//        catch (Exception e)
//        {
//            e.getMessage();
//            SharedPref.clear(con);
//        }
//        return ("2.0");
//    }

    /////////////////////////////////////////////////////

    public static boolean getGirstTime(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);

        Boolean RNN = mPrefs.getBoolean(FIRST_TIME, false);
        return RNN;

    }
    public static void setFirstTime(Context con) {
        SharedPreferences mPrefs = con.getSharedPreferences("OBOE_TESTER", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(FIRST_TIME, true);
        editor.apply();
    }

    ////////////////////////////////////////////////////////////////

    public static void clear(Context con) {
        SharedPreferences sharedPreferences = con.getSharedPreferences("OBOE_TESTER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
