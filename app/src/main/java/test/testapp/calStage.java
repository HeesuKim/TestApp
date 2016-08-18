package test.testapp;

import java.util.Random;

/**
 * Created by Administrator on 2016-08-11.
 */
public class calStage {
    Random randNum = new Random();

    public int mkRan(int down, int up) {
        int cal =  up - down;
        int rst = Math.abs(randNum.nextInt() % (cal + 1));
        return (rst + down);
    }

    public int calAdd(int curNum, int calNum) {
        return (curNum + calNum) % 10;
    }

    public int calSub(int curNum, int calNum) {
        if (curNum < calNum) {
            return (curNum + 10 - calNum) % 10;
        } else {
            return (curNum - calNum) % 10;
        }
    }

    public int selStage(int stageNum, int curNum, int calNum) {
        if (stageNum == 1) {
            return calAdd(curNum, calNum);
        } else if (stageNum == 2) {
            return 50 + calSub(curNum, calNum);
        } else {
            return -10;
        }
    }

    /*public int selSym(int stageNum) {
        if (stageNum == 1) {
            return 1;
        }
    }*/
}
