package com.d4rk.androidtutorials.java.data.source;

import android.content.res.AssetManager;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reads quiz questions from the assets folder.
 */
public class DefaultQuizLocalDataSource implements QuizLocalDataSource {

    private final AssetManager assetManager;

    public DefaultQuizLocalDataSource(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public List<QuizQuestion> loadQuestions() {
        try (InputStream is = assetManager.open("quiz_questions.json")) {
            byte[] buffer = new byte[is.available()];
            int read = is.read(buffer);
            String json = new String(buffer, 0, read, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);
            List<QuizQuestion> result = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String question = obj.getString("question");
                JSONArray opts = obj.getJSONArray("options");
                String[] options = new String[opts.length()];
                for (int j = 0; j < opts.length(); j++) {
                    options[j] = opts.getString(j);
                }
                int answer = obj.getInt("answer");
                result.add(new QuizQuestion(question, options, answer));
            }
            return result;
        } catch (IOException | JSONException e) {
            return Collections.emptyList();
        }
    }
}
