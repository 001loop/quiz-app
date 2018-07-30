package com.loop.quizapp;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Scanner;

class MD5Manager {

    @SuppressWarnings("FieldCanBeLocal")
    private static final String SECURITY_FILES_DIR = "files/";
    private static final String SECURITY_FILE_PREFERENCES = "security";
    private static final String PREFERENCES_FILE_DIR = "shared_prefs/preferences.xml";

    static boolean savePreferencesMD5(Context context) {
        return saveMD5(context, PREFERENCES_FILE_DIR, SECURITY_FILE_PREFERENCES);
    }

    static boolean isPreferencesValid(Context context) {
        return isDataValid(context, PREFERENCES_FILE_DIR, SECURITY_FILES_DIR + SECURITY_FILE_PREFERENCES);
    }

    private static boolean saveMD5(Context context, String protectedFileDir, String securityFileDir) {
        File preferences = new File(context.getApplicationInfo().dataDir, protectedFileDir);
        String md5 = calculateMD5(preferences);
        FileOutputStream outputStream;
        if (md5 == null) {
            return false;
        }
        try {
            outputStream = context.openFileOutput(securityFileDir, Context.MODE_PRIVATE);
            outputStream.write(md5.getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("All")
    private static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            return false;
        }
        String calculatedDigest = calculateMD5(updateFile);
        if (calculatedDigest == null) {
            return false;
        }
        return calculatedDigest.equalsIgnoreCase(md5);
    }

    private static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            return null;
        }
        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("All")
    private static boolean isDataValid(Context context, String checkedFileDir, String securityFileDir) {
        File preferences = new File(context.getApplicationInfo().dataDir, checkedFileDir);
        File securityFile = new File(context.getApplicationInfo().dataDir, securityFileDir);
        String md5;
        try {
            md5 = new Scanner(securityFile).useDelimiter("\\Z").next();
        } catch (FileNotFoundException | NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
        if (md5.isEmpty() || (preferences == null)) {
            return false;
        }
        return checkMD5(md5, preferences);
    }

}
