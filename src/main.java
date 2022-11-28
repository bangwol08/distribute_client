import Host.HostController;
import Operation.DistributeStorage;
import Operation.RemoteWordCounting;
import Operation.ResultPrint;
import UI.UI;

import java.util.Scanner;

public class main
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        UI UI = new UI();
        HostController Hostcontrol = new HostController();
        DistributeStorage distributeStorage = new DistributeStorage();
        RemoteWordCounting remotewordCounting = new RemoteWordCounting();
        ResultPrint ResultPrint = new ResultPrint();

        int ChooseFlag = -1;
        while(true)
        {
            ChooseFlag = UI.MainMenu();

            //사용자가 프로그램종료를 입력했을때 : 0번입력
            if(ChooseFlag == 0)
            {
                System.exit(0);
            }

            //사용자가 서버리스트 보기를 입력했을때 : 1번입력
            else if(ChooseFlag == 1)
            {
                Hostcontrol.printHostInfo();
            }

            //사용자가 기사제목 분산저장을 입력했을때 : 2번입력
            else if(ChooseFlag == 2)
            {
                distributeStorage.insertData();
            }

            //사용자가 WordCounting시작을 입력했을때 : 3번입력
            else if(ChooseFlag == 3)
            {
                remotewordCounting.doRemoteWordCounting();
            }

            //사용자가 결과보기를 입력했을때 : 4번입력
            else if(ChooseFlag == 4)
            {
                ResultPrint.resultPrint();
            }
            else
            {
                System.out.println("Please enter only Integer from 0~4");
            }
        }
    }
}
