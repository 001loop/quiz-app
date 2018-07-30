package com.loop.quizapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity {

    ImageView btnBack;
    Context context;

    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    final int PERCENTS = 100;
    final float legendsAndAxisTextSize = 13f;
    final float valueTextSize = 20f;    
    int soundIdButtonClick;
    boolean isVolumeOn;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_statistic);
        loadSounds();
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
        ButtonBack.SetOnTouchListenerAndColorFilter(context, btnBack);
        DataForStatisticActivity dataForStatisticActivity =
                SaveManager.getDataForStatisticActivity(getApplicationContext());
        int timeChallengeHighScore = dataForStatisticActivity.getTimeChallengeHighScore();
        int timeChallengeAverageScore = dataForStatisticActivity.getTimeChallengeAverageScore();
        int timeChallengeGamesCount = dataForStatisticActivity.getTimeChallengeGamesCount();
        int yesOrNoHighScore = dataForStatisticActivity.getYesOrNoHighScore();
        int yesOrNoAverageScore = dataForStatisticActivity.getYesOrNoAverageScore();
        int yesOrNoGamesCount = dataForStatisticActivity.getYesOrNoGamesCount();
        int findThePairsHighScore = dataForStatisticActivity.getFindThePairsHighScore();
        int findThePairsAverageScore = dataForStatisticActivity.getFindThePairsAverageScore();
        int findThePairsGamesCount = dataForStatisticActivity.getFindThePairsGamesCount();
        int numOfCompletedLevels = dataForStatisticActivity.getNumOfCompletedLevels();
        int numOfAnswersWithoutHints = dataForStatisticActivity.getNumOfAnswersWithoutHints();
        int numOfAnswersWithoutMistakes = dataForStatisticActivity.getNumOfAnswersWithoutMistakes();
        int numOfRemoveLettersHints = dataForStatisticActivity.getNumOfRemoveLettersHints();
        int numOfShowLetterHints = dataForStatisticActivity.getNumOfShowLetterHints();

        boolean isNeedSendDataToAnswers = dataForStatisticActivity.getIsNeedSendDataToAnswers();
        SaveManager.setStatAnswersNeedToSend(context, false);

        TextView textViewTimeChallengeHighScore = findViewById(R.id.statistic_text_game_mode_time_challenge_high_score);
        String stringTimeChallengeHighScore = getResources().getString(R.string.high_score)
                + LocaleTextHelper.getLocaleNumber(timeChallengeHighScore);
        textViewTimeChallengeHighScore.setText(stringTimeChallengeHighScore);

        TextView textViewTimeChallengeAverageScore = findViewById(R.id.statistic_text_game_mode_time_challenge_game_average_score);
        String stringTimeChallengeAverageScore = getResources().getString(R.string.statistic_average_score)
                + LocaleTextHelper.getLocaleNumber(timeChallengeAverageScore);
        textViewTimeChallengeAverageScore.setText(stringTimeChallengeAverageScore);

        TextView textViewTimeChallengeGamesCount = findViewById(R.id.statistic_text_game_mode_time_challenge_game_count);
        String stringTimeChallengeGamesCount = getResources().getString(R.string.statistic_games_count)
                + LocaleTextHelper.getLocaleNumber(timeChallengeGamesCount);
        textViewTimeChallengeGamesCount.setText(stringTimeChallengeGamesCount);

        TextView textViewYesOrNoHighScore = findViewById(R.id.statistic_text_game_mode_yes_or_no_high_score);
        String stringYesOrNoHighScore = getResources().getString(R.string.high_score)
                + LocaleTextHelper.getLocaleNumber(yesOrNoHighScore);
        textViewYesOrNoHighScore.setText(stringYesOrNoHighScore);

        TextView textViewYesOrNoAverageScore = findViewById(R.id.statistic_text_game_mode_yes_or_no_average_score);
        String stringYesOrNoAverageScore = getResources().getString(R.string.statistic_average_score)
                + LocaleTextHelper.getLocaleNumber(yesOrNoAverageScore);
        textViewYesOrNoAverageScore.setText(stringYesOrNoAverageScore);

        TextView textViewYesOrNoGamesCount = findViewById(R.id.statistic_text_game_mode_yes_or_no_game_count);
        String stringYesOrNoGamesCount = getResources().getString(R.string.statistic_games_count)
                + LocaleTextHelper.getLocaleNumber(yesOrNoGamesCount);
        textViewYesOrNoGamesCount.setText(stringYesOrNoGamesCount);

        TextView textViewFindThePairsHighScore = findViewById(R.id.statistic_text_game_mode_find_the_pairs_high_score);
        String stringFindThePairsHighScore = getResources().getString(R.string.high_score)
                + LocaleTextHelper.getLocaleNumber(findThePairsHighScore);
        textViewFindThePairsHighScore.setText(stringFindThePairsHighScore);

        TextView textViewFindThePairsAverageScore = findViewById(R.id.statistic_text_game_mode_find_the_pairs_average_score);
        String stringFindThePairsAverageScore = getResources().getString(R.string.statistic_average_score)
                + LocaleTextHelper.getLocaleNumber(findThePairsAverageScore);
        textViewFindThePairsAverageScore.setText(stringFindThePairsAverageScore);

        TextView textViewFindThePairsGamesCount = findViewById(R.id.statistic_text_game_mode_find_the_pairs_game_count);
        String stringFindThePairsGamesCount = getResources().getString(R.string.statistic_games_count)
                + LocaleTextHelper.getLocaleNumber(findThePairsGamesCount);
        textViewFindThePairsGamesCount.setText(stringFindThePairsGamesCount);

        TextView textViewNumOfLevels = findViewById(R.id.statistic_text_num_of_levels);
        String stringNumOfLevels = getResources().getString(R.string.statistic_num_of_levels)
                + LocaleTextHelper.getLocaleNumber(numOfCompletedLevels);
        textViewNumOfLevels.setText(stringNumOfLevels);

        TextView textViewAnswersWithoutMistakes = findViewById(R.id.statistic_text_answers_without_mistakes);
        PieChart chart_1 = findViewById(R.id.chart_1);
        if (numOfCompletedLevels > 0) {
            String stringAnswersWithoutMistakes =
                    getResources().getString(R.string.statistic_answers_without_mistakes)
                            + LocaleTextHelper.getLocaleNumber(numOfAnswersWithoutMistakes);
            textViewAnswersWithoutMistakes.setText(stringAnswersWithoutMistakes);
            setPieData(numOfAnswersWithoutMistakes, numOfCompletedLevels - numOfAnswersWithoutMistakes,
                    getResources().getString(R.string.statistic_description_without_mistakes),
                    getResources().getString(R.string.statistic_description_with_mistakes),
                    chart_1);
        } else {
            textViewAnswersWithoutMistakes.setVisibility(View.GONE);
            chart_1.setVisibility(View.GONE);
        }

        TextView textViewAnswersWithoutHints = findViewById(R.id.statistic_text_answers_without_hints);
        PieChart chart_2 = findViewById(R.id.chart_2);
        if (numOfCompletedLevels > 0) {
            String stringAnswersWithoutHints =
                    getResources().getString(R.string.statistic_answers_without_hints)
                            + LocaleTextHelper.getLocaleNumber(numOfAnswersWithoutHints);
            textViewAnswersWithoutHints.setText(stringAnswersWithoutHints);
            setPieData(numOfAnswersWithoutHints, numOfCompletedLevels - numOfAnswersWithoutHints,
                    getResources().getString(R.string.statistic_description_without_hints),
                    getResources().getString(R.string.statistic_description_with_hints),
                    chart_2);
        } else {
            textViewAnswersWithoutHints.setVisibility(View.GONE);
            chart_2.setVisibility(View.GONE);
        }

        TextView textViewNumOfRemoveLettersHints = findViewById(R.id.statistic_text_num_of_remove_letters_hints);
        String stringNumOfRemoveLettersHints =
                getResources().getString(R.string.statistic_num_of_used_hints)
                        + getResources().getString(R.string.remove_letters)
                        + ": "
                        + LocaleTextHelper.getLocaleNumber(numOfRemoveLettersHints);
        textViewNumOfRemoveLettersHints.setText(stringNumOfRemoveLettersHints);

        TextView textViewNumOfShowLetterHints = findViewById(R.id.statistic_text_num_of_show_letter_hints);
        String stringNumOfShowLetterHints =
                getResources().getString(R.string.statistic_num_of_used_hints)
                        + getResources().getString(R.string.insert_letter)
                        + ": "
                        + LocaleTextHelper.getLocaleNumber(numOfShowLetterHints);
        textViewNumOfShowLetterHints.setText(stringNumOfShowLetterHints);

    }

    @SuppressWarnings("deprecation")
    private void setPieData(int firstValue, int secondValue, String firstValueDescription,
                            String secondValueDescription, PieChart mChart) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(firstValue, firstValueDescription));
        entries.add(new PieEntry(secondValue, secondValueDescription));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int)value);
            }
        });
        mChart.setDrawEntryLabels(false);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#"+Integer.toHexString(
                getResources().getColor(R.color.pieChartFirstColor))));
        colors.add(Color.parseColor("#"+Integer.toHexString(
                getResources().getColor(R.color.pieChartMainColor))));
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(
                    float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return LocaleTextHelper.getLocaleNumber((int) value);
            }
        });
        data.setValueTextSize(valueTextSize);
        data.setValueTextColor(getResources().getColor(R.color.textOnMpChartsBackground));
        mChart.setData(data);
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        mChart.getLegend().setTextSize(legendsAndAxisTextSize);
        mChart.getLegend().setTextColor(getResources().getColor(R.color.textOnActivityBackground));
        mChart.highlightValues(null);
        mChart.setDrawHoleEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        playSound(soundIdButtonClick);
        Intent intent = new Intent(StatisticActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    private void loadSounds() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (isVolumeOn) {
            if (soundPool == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    soundPool = new SoundPool.Builder()
                            .setMaxStreams(MAX_SOUND_STREAMS)
                            .build();
                } else {
                    soundPool = new SoundPool(MAX_SOUND_STREAMS, AudioManager.STREAM_MUSIC, 1);
                }
                soundIdButtonClick = soundPool.load(this, R.raw.button_click, 1);
            }
        }
    }

    private void playSound(int id) {
        if (isVolumeOn) {
            if (soundPool != null) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        LaunchManager.loadLocale(context);
        loadData();
        loadSounds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseSoundPool();
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    private void loadData() {
        isVolumeOn = SaveManager.getVolumeOn(context);
    }

}
