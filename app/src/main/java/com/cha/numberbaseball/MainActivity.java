package com.cha.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.cha.NumberBaseball.R;
import com.cha.NumberBaseball.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Button[] btnArr = new Button[10];
    TextView num1,num2,num3,strike,ball,out,gameResult,round,ballResult1,ballResult2;
    int roundCount = 1, strikeNum=0, ballNum=0, outNum=0;
    int answer100,answer10,answer1;

    String numResult = "", ballResult = "";
    Button retryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initial();
    }


    private void initial(){
        setAnswer();
        // 뷰 객체 id 대입
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);

        strike = findViewById(R.id.strike);
        ball = findViewById(R.id.ball);
        out = findViewById(R.id.out);

        gameResult = findViewById(R.id.gameResult);
        ballResult2 = findViewById(R.id.ballResult2);
        round = findViewById(R.id.round);



        round.setText( roundCount +"");
        round.setVisibility(View.VISIBLE);


        // 버튼 리스너
        btnArr = new Button[]{binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4, binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9};
        for (Button button : btnArr) button.setOnClickListener(listener);


        binding.btnRetry.setOnClickListener((v)->retryEvent());
    }


    /*
     *
     *       정답 만들기. 중복은 빼야한다. 중복처리를 위해 자릿수 마다 랜덤함수를 돌렸음.
     *
     * */
    private void setAnswer(){
        Random rand = new Random();
        answer100 = rand.nextInt(10);
        while(answer100 == answer10) answer10 = rand.nextInt(10);
        while(answer100 == answer1 || answer10 == answer1) answer1 = rand.nextInt(10);
    }



    /*
    *
    *       숫자 버튼 클릭 이벤트
    *
    * */
    private final View.OnClickListener listener = (view)->{
        setSelectNumber((Button) view);
        selectedAll();
    };




    /*
     *
     *   버튼 select 여부를 확인하여, select 되어있으면, select 을 취소 하고 버튼색, 글씨색을 원래대로 바꾸고, 선택 숫자를 지운다.
     *   select 이 안되어있으면 버튼색, 글씨색을 바꾸고, 선택숫자에 기입한다. select 여부를 true 로 바꾼다.
     *
     * */
    public void setSelectNumber (Button btn){
        if(btn.isSelected()) {
            if (binding.num3.getText().equals(btn.getText())) binding.num3.setText("");
            else if (binding.num2.getText().equals(btn.getText())) binding.num2.setText("");
            else binding.num1.setText("");
            btn.setBackgroundColor(Color.parseColor("#9CB580"));
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setSelected(false);
        }else{
            if (binding.num1.getText().equals("")) binding.num1.setText(btn.getText());
            else if (binding.num2.getText().equals("")) binding.num2.setText(btn.getText());
            else binding.num3.setText(btn.getText());
            btn.setBackgroundColor(Color.parseColor("#FF555950"));
            btn.setTextColor(Color.parseColor("#FF2C2B2B"));
            btn.setSelected(true);
        }
    }



    /*
     *
     *   선택되어 있는 버튼이 총 3개이면 선택이 완료된거니, 모든 버튼을 선택불가하게 만든다. 그리고
     *   compareAnswer() 를 이용하여 선택한 숫자와 정답을 비교 하고, init() 으로 초기화하여 다음 라운드 진행.
     *
     * */
    public void selectedAll(){
        if(binding.num1.getText().equals("")
                || binding.num2.getText().equals("")
                || binding.num3.getText().equals("") ) return;
        for(int i = 0; i < 10; i++) btnArr[i].setSelected(false);
        compareToAnswer();
    }






    /*
    *
    *       strike, ball, out 을 체크해서 화면에 표시, 승리 조건인지 판단, round 수 갱신
    *
    * */
    public void compareToAnswer(){

        int n100 = Integer.parseInt(num1.getText().toString());
        int n10 = Integer.parseInt(num2.getText().toString());
        int n1 = Integer.parseInt(num3.getText().toString());

        if(n100 == answer100) strikeNum++;
        else if(n100 == answer10 || n100 == answer1) ballNum++;
        else outNum++;

        if(n10 == answer10) strikeNum++;
        else if(n10 == answer100 || n10 == answer1) ballNum++;
        else outNum++;

        if(n1 == answer1) strikeNum++;
        else if(n1 == answer10 || n1 == answer100) ballNum++;
        else outNum++;

        binding.strike.setText(String.valueOf(strikeNum));
        binding.ball.setText(String.valueOf(ballNum));
        binding.out.setText(String.valueOf(outNum));

        if(strikeNum == 3) homeRun();
        else nextRound();
    }



    /*
    *
    *        승리 조건 판단 . 3 strike 이면 승리. 승리시, 다시하기 버튼 활성화와, success 출력 및 버튼 선택 불가
    *
    * */
    public void homeRun(){
        if(strikeNum != 3) return;

        for(int i = 0; i < 10; i++){
            btnArr[i].setEnabled(false);
            btnArr[i].setBackgroundColor(Color.parseColor("#FF555950"));
            btnArr[i].setTextColor(Color.parseColor("#FF2C2B2B"));
        }
        binding.gameResult.setVisibility(View.VISIBLE);
        binding.btnRetry.setVisibility(View.VISIBLE);
    }



    /*
    *
    *       round 가 달라질때, 선택결과 숫자, 버튼, 선택 결과와 정답을 비교한 s,b,o 을 초기화
    *
    * */
    public void nextRound(){

        roundCount++;
        round.setText(String.valueOf(roundCount));
        drawNum();
        resetElement();
    }



    /*
    *
    *       다음 라운드 진행 시 초기화 작업
    *
    * */
    private void resetElement(){
        for (int i = 0; i < 10; i++) {
            btnArr[i].setEnabled(true);
            btnArr[i].setBackgroundColor(Color.parseColor("#9CB580"));
            btnArr[i].setTextColor(Color.parseColor("#FFFFFF"));
        }

        binding.num1.setText("");
        binding.num2.setText("");
        binding.num3.setText("");

        strikeNum = 0;
        ballNum = 0;
        outNum = 0;
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
            binding.resultBoard1.setText(numResult);
            binding.ballResult1.setText(ballResult);
        } else if(roundCount < 19){
            if(roundCount == 10) {
                numResult = "";
                ballResult = "";
            }
            numResult += (roundCount-1) + "R  :  " + num1.getText().toString()
                    + " " + num2.getText().toString() + " "
                    + num3.getText().toString() + "\n";
            ballResult += strikeNum + " S  " + ballNum + " B"+"\n";
            binding.resultBoard2.setText(numResult);
            ballResult2.setText(ballResult);
        } else {
            if(roundCount == 19) {
                numResult = "";
                ballResult = "";
                binding.resultBoard1.setText("");
                binding.resultBoard2.setText("");
                ballResult1.setText("");
                ballResult2.setText("");
            }
            numResult += (roundCount-1) + "R  :  " + num1.getText().toString()
                    + " " + num2.getText().toString() + " "
                    + num3.getText().toString() + "\n";
            ballResult += strikeNum + " S  " + ballNum + " B"+"\n";
            binding.resultBoard1.setText(numResult);
            ballResult1.setText(ballResult);
        }
    }

    // 각각 초기상태로 돌려놓는다.
    private void retryEvent(){
        binding.gameResult.setVisibility(View.GONE);
        for(int i = 0; i < 10; i++){
            btnArr[i].setEnabled(true);
            btnArr[i].setBackgroundColor(Color.parseColor("#9CB580"));
            btnArr[i].setTextColor(Color.parseColor("#FFFFFF"));
        }

        binding.resultBoard1.setText("");
        binding.resultBoard2.setText("");
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
}



