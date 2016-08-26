package test.testapp;

import java.util.Random;

/**
 * 난이도, 계산결과 관련 클래스
 * Created by Administrator on 2016-08-11.
 */
public class calStage {
    //랜덤함수 사용을 위한 초기화
    Random randNum = new Random();

    //down ~ up 사이의 수 중에서 하나의 숫자를 무작위로 반환
    public int mkRan(int down, int up) {
        int cal = up - down;
        int rst = Math.abs(randNum.nextInt() % (cal + 1));
        return (rst + down);
    }

    //덧셈
    public int calAdd(int curNum, int [] calNum) {
        int rst = 0;
        for (int i = 0; i < 3; i++) {
            //계산숫자배열에 31(초기화상태)가 아닌 실제값이 있을 때만 결과에 반영
            if (calNum[i] != 31) {
                rst += calNum[i];
            }
        }
        //1의 자리만 결과로 반환
        return (curNum + rst) % 10;
    }

    //뺄셈
    public int calSub(int curNum, int [] calNum) {
        int rst = 0;
        for (int i = 0; i < 3; i++) {
            if (calNum[i] != 31) {
                rst += calNum[i];
            }
        }
        //절대값 반환
        return (Math.abs(curNum - rst)) % 10;
    }

    int [] calNum = {31, 31, 31};   //계산숫자배열(5단계부터 2개씩, 7단계부터 3개씩 연산)
    int ansArr[] = new int[4];      //반환배열(0 : 기존수, 1~3 : 계산숫자)

    //스테이지 번호에 따라 계산방법을 분기하여 결과반환
    public int[] selStage(int stageNum, int curNum) {
        //첫 계산숫자 무작위선택
        calNum[0] = mkRan(0, 9);
        if (stageNum >= 5) { //스테이지 5부터 두번째 계산숫자 추가
            calNum[1] = mkRan(0, 9);
        }
        if (stageNum >= 7) { //스테이지 7부터 세번째 계산숫자 추가
            calNum[2] = mkRan(0, 9);
        }

        //홀수 스테이지 덧셈, 짝수 스테이지 뺄셈
        if (stageNum % 2 == 1) {
            ansArr[0] = calAdd(curNum, calNum);
        } else {
            ansArr[0] = calSub(curNum, calNum);
        }

        //계산숫자를 반환배열에 차례로 입력
        for (int i = 0; i < 3; i++) {
            ansArr[i+1] = calNum[i];
        }

        return ansArr;
    }

    // 스테이지 시작시 출력되는 설명문
    public String[] stageInfo(int stageNum) {
        String stArr[] = new String[5];
        stArr[0] = "Stage" + stageNum;
        if (stageNum % 2 == 1) {
            stArr[0] += " ADD";
            stArr[1] = "계산결과가 10이 넘으면";
            stArr[2] = "1의 자리 수만 구합니다.";
        } else {
            stArr[0] += " SUB";
            stArr[1] = "계산결과가 0보다 작으면";
            stArr[2] = "절대값으로 구합니다.";
        }
        if (stageNum >= 3) {
            stArr[3] = "Double(X2) Speed";
        }
        if (stageNum == 5) {
            stArr[4] = "추가연산 1개";
        } else if (stageNum > 5) {
            stArr[4] = "추가연산 2개";
        }
        return stArr;

    }
}
