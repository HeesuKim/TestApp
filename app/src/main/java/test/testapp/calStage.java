package test.testapp;

/**
 * Created by Administrator on 2016-08-11.
 */
public class calStage {
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
