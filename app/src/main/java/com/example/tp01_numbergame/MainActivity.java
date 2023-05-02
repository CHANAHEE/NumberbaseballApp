package com.example.tp01_numbergame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Button[] btnArr = new Button[10];
    TextView num1,num2,num3,strike,ball,out,gameResult,round,refResult1, refResult2,ballResult1,ballResult2;
    int roundCount = 1, strikeNum=0, ballNum=0, outNum=0;
    int answer100,answer10,answer1;
    Random rand = new Random();
    String numResult = "", ballResult = "";
    Button retryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버튼 배열 id 대입
        for(int i = 0; i < 10 ; i++){
            btnArr[i] = findViewById(R.id.btn0++);
        }
        // 뷰 객체 id 대입
        retryBtn = findViewById(R.id.btn_retry);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);

        strike = findViewById(R.id.strike);
        ball = findViewById(R.id.ball);
        out = findViewById(R.id.out);

        gameResult = findViewById(R.id.gameResult);
        refResult1 = findViewById(R.id.refResult1);
        refResult2 = findViewById(R.id.refResult2);
        ballResult1 = findViewById(R.id.ballResult1);
        ballResult2 = findViewById(R.id.ballResult2);
        round = findViewById(R.id.round);
        round.setText( roundCount +"");
        round.setVisibility(View.VISIBLE);

        // 컴퓨터 정답 만들기
        setAnswer();
        // 버튼 리스너
        for(int i = 0; i< 10; i++){
            btnArr[i].setOnClickListener(listener);
        }
        retryBtn.setOnClickListener(listener_retry);

    }

    // 버튼을 누르면, num1,num2,num3 에 작성하는 setNumber() 와 버튼의 select 여부를 체크해서
    //
    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            setBtnNumber(btn);
            allBtnChecked();
        }
    };

    // 각각 초기상태로 돌려놓는다.
    View.OnClickListener listener_retry = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            gameResult.setVisibility(View.GONE);
            for(int i = 0; i < 10; i++){
                btnArr[i].setEnabled(true);
                btnArr[i].setBackgroundColor(Color.parseColor("#9CB580"));
                btnArr[i].setTextColor(Color.parseColor("#FFFFFF"));
            }

            refResult1.setText("");
            refResult2.setText("");
            ballResult1.setText("");
            ballResult2.setText("");

            numResult = "";
            ballResult = "";

            retryBtn.setVisibility(View.INVISIBLE);

            strike.setText("0");
            roundCount = 1;
            round.setText("1");
            setAnswer();
        }
    };

    // strike, ball, out 을 체크해서 화면에 표시, 승리 조건인지 판단, round 수 갱신
    public void inputNumber(){


        String s1 = num1.getText().toString();
        int n100 = Integer.parseInt(s1);

        String s2 = num2.getText().toString();
        int n10 = Integer.parseInt(s2);

        String s3 = num3.getText().toString();
        int n1 = Integer.parseInt(s3);


        if(n100 == answer100) strikeNum++;
        else if(n100 == answer10) ballNum++;
        else if(n100 == answer1) ballNum++;
        else outNum++;

        if(n10 == answer100) ballNum++;
        else if(n10 == answer10) strikeNum++;
        else if(n10 == answer1) ballNum++;
        else outNum++;

        if(n1 == answer100) ballNum++;
        else if(n1 == answer10) ballNum++;
        else if(n1 == answer1) strikeNum++;
        else outNum++;

        strike.setText(String.valueOf(strikeNum));
        ball.setText(String.valueOf(ballNum));
        out.setText(String.valueOf(outNum));

        homeRun();


    }

    // 승리 조건 판단 . 3 strike 이면 승리. 승리시, 다시하기 버튼 활성화와, success 출력 및 버튼 선택 불가
    public void homeRun(){
        if(strikeNum == 3) {
            gameResult.setVisibility(View.VISIBLE);
            for(int i = 0; i < 10; i++){
                btnArr[i].setEnabled(false);
                btnArr[i].setBackgroundColor(Color.parseColor("#FF555950"));
                btnArr[i].setTextColor(Color.parseColor("#FF2C2B2B"));
            }
            retryBtn.setVisibility(View.VISIBLE);
        }
    }

    // s,b,o 의 이전 결과값들을 표기.
    public void drawNum(){

        strike.setTextColor(Color.parseColor("#3580BC"));
        ball.setTextColor(Color.parseColor("#668A3C"));
        // round 의 숫자를 기준으로, 9라운드까지는 왼쪽에, 18라운드까지는 오른쪽에, 그 이상은 화면 초기화 후 다시 처음부터 진행
        if(roundCount < 10) {
            numResult += (roundCount-1) + "R  :  " + num1.getText().toString()
                    + " " + num2.getText().toString()
                    + " " + num3.getText().toString() + "\n";
            ballResult += strikeNum + " S  " + ballNum + " B"+"\n";
            refResult1.setText(numResult);
            ballResult1.setText(ballResult);
        } else if(roundCount < 19){
            if(roundCount == 10) {
                numResult = "";
                ballResult = "";
            }
            numResult += (roundCount-1) + "R  :  " + num1.getText().toString()
                    + " " + num2.getText().toString() + " "
                    + num3.getText().toString() + "\n";
            ballResult += strikeNum + " S  " + ballNum + " B"+"\n";
            refResult2.setText(numResult);
            ballResult2.setText(ballResult);
        } else {
            if(roundCount == 19) {
                numResult = "";
                ballResult = "";
                refResult1.setText("");
                refResult2.setText("");
                ballResult1.setText("");
                ballResult2.setText("");
            }
            numResult += (roundCount-1) + "R  :  " + num1.getText().toString()
                    + " " + num2.getText().toString() + " "
                    + num3.getText().toString() + "\n";
            ballResult += strikeNum + " S  " + ballNum + " B"+"\n";
            refResult1.setText(numResult);
            ballResult1.setText(ballResult);
        }
    }
    // round 가 달라질때, 선택결과 숫자, 버튼, 선택 결과와 정답을 비교한 s,b,o 을 초기화
    public void init(){

        if(strikeNum != 3) {
            roundCount++;
            round.setText(roundCount + "");
        }

        // 버튼 초기화
        if(strikeNum != 3) {
            for (int i = 0; i < 10; i++) {
                btnArr[i].setEnabled(true);
                btnArr[i].setBackgroundColor(Color.parseColor("#9CB580"));
                btnArr[i].setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
        // 선택결과 저장을 위해 이전 결과들을 화면에 표기
        drawNum();

        // 선택 숫자 초기화
        num1.setText("");
        num2.setText("");
        num3.setText("");

        // 새로운 선택결과와 정답의 비교를 위해 s,b,o 초기화
        strikeNum = 0;
        ballNum = 0;
        outNum = 0;
    }
    // 버튼 select 여부를 확인하여, select 되어있으면, select 을 취소 하고 버튼색, 글씨색을 원래대로 바꾸고, 선택 숫자를 지운다.
    // select 이 안되어있으면 버튼색, 글씨색을 바꾸고, 선택숫자에 기입한다. select 여부를 true 로 바꾼다.
    public void setBtnNumber (Button btn){


        if(!btn.isSelected()) {
            if (num1.getText().equals("")) num1.setText(btn.getText());
            else if (num2.getText().equals("")) num2.setText(btn.getText());
            else num3.setText(btn.getText());
            btn.setBackgroundColor(Color.parseColor("#FF555950"));
            btn.setTextColor(Color.parseColor("#FF2C2B2B"));
            btn.setSelected(true);
        }else{
            if (num3.getText().equals(btn.getText())) num3.setText("");
            else if (num2.getText().equals(btn.getText())) num2.setText("");
            else num1.setText("");
            btn.setBackgroundColor(Color.parseColor("#9CB580"));
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setSelected(false);
        }
    }

    // 선택되어 있는 버튼이 총 3개이면 선택이 완료된거니, 모든 버튼을 선택불가하게 만든다. 그리고
    // inputNumber() 를 이용하여 선택한 숫자와 정답을 비교 하고, init() 으로 초기화하여 다음 라운드 진행.
    public void allBtnChecked(){
        int count = 0;
        for(Button b : btnArr) if(b.isSelected()) count++;

        if(count == 3){
            for(int i = 0; i < 10; i++){
                btnArr[i].setSelected(false);
            }

            inputNumber();
            init();
        }

    }

    // 컴퓨터 정답 만들기. 중복은 빼야한다. 중복처리를 위해 자릿수 마다 랜덤함수를 돌렸음.
    public void setAnswer(){

        answer100 = rand.nextInt(10);
        while(answer100 == answer10) answer10 = rand.nextInt(10);
        while(answer100 == answer1 || answer10 == answer1) answer1 = rand.nextInt(10);

    }
}



