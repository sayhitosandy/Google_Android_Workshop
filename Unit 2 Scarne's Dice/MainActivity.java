package com.example.scarnesdice;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected int USER_OVERALL_SCORE;
    protected int USER_TURN_SCORE;
    protected int COMP_OVERALL_SCORE;
    protected int COMP_TURN_SCORE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        USER_OVERALL_SCORE = 0;
        USER_TURN_SCORE = 0;
        COMP_OVERALL_SCORE = 0;
        COMP_TURN_SCORE = 0;
        updateLabel();
    }

//    @TargetApi(21)
    public void rollOnClick(View v) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Random rand = new Random();
        int value = rand.nextInt(6)+1;
        USER_TURN_SCORE = value;
        if (value == 1) {
            image.setImageResource(R.drawable.dice1);
            USER_TURN_SCORE = 0;
            computerTurn();
            return;
        }
        else if (value == 2) {
            image.setImageResource(R.drawable.dice2);
        }
        else if (value == 3) {
            image.setImageResource(R.drawable.dice3);
        }
        else if (value == 4) {
            image.setImageResource(R.drawable.dice4);
        }
        else if (value == 5) {
            image.setImageResource(R.drawable.dice5);
        }
        else if (value == 6) {
            image.setImageResource(R.drawable.dice6);
        }
        USER_OVERALL_SCORE += USER_TURN_SCORE;
        if (USER_OVERALL_SCORE >= 100) {
            TextView winner = (TextView)findViewById(R.id.textView);
            winner.setText("You WON !\nYour Score: " + USER_OVERALL_SCORE + " Computer Score: " + COMP_OVERALL_SCORE +"\nPlease Click on Reset...");
            return;
//            resetOnClick(winner);
        }
        updateScore();
    }

    public void resetOnClick(View v) {
        USER_OVERALL_SCORE = 0;
        USER_TURN_SCORE = 0;
        COMP_OVERALL_SCORE = 0;
        COMP_TURN_SCORE = 0;
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.dice1);
        updateLabel();
    }

    public void holdOnClick(View v) {
        USER_TURN_SCORE = 0;
        updateLabel();
        if (USER_OVERALL_SCORE >= 100) {
            TextView winner = (TextView)findViewById(R.id.textView);
            winner.setText("You WON !\nYour Score: " + USER_OVERALL_SCORE + " Computer Score: " + COMP_OVERALL_SCORE +"\nPlease Click on Reset...");
//            resetOnClick(winner);
        }
        computerTurn();
    }

    public void updateLabel() {
        TextView textLabel = (TextView) findViewById(R.id.textView);
        textLabel.setText("Your Score: " + USER_OVERALL_SCORE + " Computer Score: " + COMP_OVERALL_SCORE);
    }

    public void updateScore() {
        TextView textLabel = (TextView) findViewById(R.id.textView);
        textLabel.setText("Current Score: (" + USER_TURN_SCORE + " (User), " + COMP_TURN_SCORE + " (Computer))");
    }

    public void computerTurn() {
        Button roll = (Button)findViewById(R.id.button);
        Button hold = (Button)findViewById(R.id.button2);
        roll.setClickable(false);
        hold.setClickable(false);

        Random rand = new Random();
        int val = 0;
        ImageView image = (ImageView) findViewById(R.id.imageView);
        while (COMP_TURN_SCORE <= 20) {
            val = rand.nextInt(6)+1;
            if (val == 1) {
                image.setImageResource(R.drawable.dice1);
                COMP_TURN_SCORE = 0;
                break;
            }
            COMP_TURN_SCORE += val;
            updateScore();
            if (val == 2) {
                image.setImageResource(R.drawable.dice2);
            }
            else if (val == 3) {
                image.setImageResource(R.drawable.dice3);
            }
            else if (val == 4) {
                image.setImageResource(R.drawable.dice4);
            }
            else if (val == 5) {
                image.setImageResource(R.drawable.dice5);
            }
            else if (val == 6) {
                image.setImageResource(R.drawable.dice6);
            }
        }
        COMP_OVERALL_SCORE += COMP_TURN_SCORE;
        updateLabel();
        COMP_TURN_SCORE = 0;

        roll.setClickable(true);
        hold.setClickable(true);

        if (COMP_OVERALL_SCORE >= 100) {
            TextView winner = (TextView)findViewById(R.id.textView);
            winner.setText("You LOST !\nYour Score: " + USER_OVERALL_SCORE + " Computer Score: " + COMP_OVERALL_SCORE +"\nPlease Click on Reset...");
//            resetOnClick(winner);
        }
    }
}
