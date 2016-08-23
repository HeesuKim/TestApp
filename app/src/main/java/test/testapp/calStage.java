package test.testapp;

import java.util.Random;

/**
 * Created by Administrator on 2016-08-11.
 */
public class calStage {
    Random randNum = new Random();

    public int mkRan(int down, int up) {
        int cal = up - down;
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

    //int curNum;
    int calNum;
    int ansArr[] = new int[2];

    public int[] selStage(int stageNum, int curNum) {
        if (stageNum % 2 == 1) {
            if (stageNum < 2) {
                calNum = mkRan(0, 5);
            } else {
                calNum = mkRan(0, 9);
            }
            ansArr[0] = calAdd(curNum, calNum);
        } else {
            if (stageNum < 3) {
                calNum = mkRan(0, 5);
            } else {
                calNum = mkRan(0, 9);
            }
            ansArr[0] = calSub(curNum, calNum);
        }
        ansArr[1] = calNum;
        //System.out.println("NTPS calNum : " + calNum);
        return ansArr;
    }

    public String stageInfo(int stageNum) {
        String str = "Stage" + stageNum;
        if (stageNum % 2 == 1) {
            str += "  ADD";
        } else {
            str += "  SUB";
        }
        if (stageNum >= 5) {
            str += "  Double(X2) Speed";
        }
        return str;
    }

    /*GraphicObject infoPage = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
            )
    public GraphicObject stageInfo(int stageNum) {
        if (stageNum == 1) {

        }
    }*/

    /*public int selSym(int stageNum) {
        if (stageNum == 1) {
            return 1;
        }
    }*/
}
