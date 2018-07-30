package com.loop.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Locale;
import java.util.MissingResourceException;

class LaunchManager {

    static void checkDataValid(Context context) {
        if (!MD5Manager.isPreferencesValid(context)) {
            Toast.makeText(context, context.getResources().getString(R.string.data_error),
                    Toast.LENGTH_LONG).show();
            SaveManager.setDefaultData(context, false);
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName());
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }
    }

    static boolean isFirstLaunch(Context context) {
        boolean isFirst = SaveManager.getIsFirstLaunch(context);
        if (isFirst) {
            SaveManager.setDefaultData(context, false);
            SaveManager.setPrivacyPolicyAccepted(context, false);
        }
        return isFirst;
    }

    static boolean isFirstLaunchAfterUpdate(Context context) {
        boolean isFirst = SaveManager.getIsFirstLaunchAfterUpdate(context);
        if (isFirst) {
            SaveManager.updateLastVersion(context);
        }
        return isFirst;
    }

    static boolean isPrivacyPolicyAccepted(Context context) {
        return SaveManager.getIsPrivacyPolicyAccepted(context);
    }

    static void loadLocale(Context context) {
        String localeName = SaveManager.getLanguage(context);
        if (localeName == null) {
            Locale currentLocale = context.getResources().getConfiguration().locale;
            String iso3Language  = currentLocale.toString();
            String languageCode = Locale.getDefault().getLanguage();
            String supportedLanguage;
            if (iso3Language.equals("zh_CN")) {
                supportedLanguage = "zh_CN";
            } else if (iso3Language.equals("zh_TW")) {
                supportedLanguage = "zh_TW";
            } else if (languageCode.equals("ar")){
                supportedLanguage = "ar";
            } else if (languageCode.equals("de")){
                supportedLanguage = "de";
            } else if (languageCode.equals("es")){
                supportedLanguage = "es";
            } else if (languageCode.equals("fr")){
                supportedLanguage = "fr";
            }  else if (languageCode.equals("pt")){
                supportedLanguage = "pt";
            } else if (languageCode.equals("ru")){
                supportedLanguage = "ru";
            } else {
                supportedLanguage= "en";
            }
            SaveManager.saveLanguage(context, supportedLanguage);
            loadLocale(context);
            return;
        }
        Locale currentLocale = context.getResources().getConfiguration().locale;
        String iso3Language;
        try {
            iso3Language = currentLocale.toString();
        } catch (MissingResourceException e){
            return;
        }
        if (iso3Language.equals(localeName)) {
            return;
        }
        Locale myLocale;
        if (localeName.contains("_")) {
            int index = localeName.indexOf("_");
            String code = localeName.substring(0, index);
            String region = localeName.substring(index + 1);
            myLocale = new Locale(code, region);
        } else {
            myLocale = new Locale(localeName);
        }
        Locale.setDefault(myLocale);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(myLocale);
        }
        res.updateConfiguration(conf, dm);
    }

}
