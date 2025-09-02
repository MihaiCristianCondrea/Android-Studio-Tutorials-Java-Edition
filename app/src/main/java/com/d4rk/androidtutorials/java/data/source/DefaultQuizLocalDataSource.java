package com.d4rk.androidtutorials.java.data.source;

import android.content.res.AssetManager;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Reads quiz questions from the assets folder.
 */
public class DefaultQuizLocalDataSource implements QuizLocalDataSource {

    private final AssetManager assetManager;
    private final ExecutorService executorService;

    public DefaultQuizLocalDataSource(AssetManager assetManager, ExecutorService executorService) {
        this.assetManager = assetManager;
        this.executorService = executorService;
    }

    @Override
    public void loadQuestions(QuestionsCallback callback) {
        executorService.execute(() -> {
            List<QuizQuestion> result = new ArrayList<>();
            try (InputStream is = assetManager.open("quiz_questions.json");
                 JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    String question = null;
                    List<String> options = new ArrayList<>();
                    int answer = -1;
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        switch (name) {
                            case "question" -> question = reader.nextString();
                            case "options" -> {
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    options.add(reader.nextString());
                                }
                                reader.endArray();
                            }
                            case "answer" -> answer = reader.nextInt();
                            default -> reader.skipValue();
                        }
                    }
                    reader.endObject();
                    if (question != null && !options.isEmpty() && answer >= 0) {
                        result.add(new QuizQuestion(
                                question,
                                options.toArray(new String[0]),
                                answer));
                    }
                }
                reader.endArray();
            } catch (IOException e) {
                result = Collections.emptyList();
            }
            callback.onResult(result);
        });
    }
}
