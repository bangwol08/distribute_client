package Operation;

import DB.DBcontroller;
import Host.HostController;

import java.io.*;
import java.util.Scanner;

public class DistributeStorage
{
    public void insertData()
    {
        HostController HostInfo = new HostController();
        Scanner scan = new Scanner(System.in);

        int hostCount = HostInfo.getHostCount();
        int connectCount = 0;
        String conntinueFlag;
        HostController.HostInfo[] hosts_ConnectionInfo = HostInfo.getHostInfo();
        DBcontroller[] server_DBcontrol = new DBcontroller[hostCount];

        //서버별 컨트롤러 생성
        for(int i=0; i<hostCount; i++)
        {
            server_DBcontrol[i] = new DBcontroller(hosts_ConnectionInfo[i].serverName, hosts_ConnectionInfo[i].host,
                    hosts_ConnectionInfo[i].dbPort, "distribute_database", hosts_ConnectionInfo[i].ID, hosts_ConnectionInfo[i].PW);
        }

        //DB연결 현황 확인
        for(int i=0; i<hostCount; i++)
        {
            //각 클래스의 conn이 null이 아니면 접속성공 간주
            if(server_DBcontrol[i].conn != null)
            {
                connectCount++;
            }
        }

        //연결된 DB가 하나도 없을시 메인메뉴로 돌아감
        if(connectCount==0)
        {
            System.out.println("Connection to all DBs is not possible. Please check the DB status again.");
        }
        else
        {
            System.out.println("A total "+connectCount+"DB is connected. Start Title Distribute Storage");
            System.out.println("If you continue, the original data on each server will be deleted. Would you like to continue? (Y/N)");
            while(true)
            {
                conntinueFlag = scan.nextLine();
                //Y선택시 모든 데이터 삭제 후 새롭게 데이터 input
                if(conntinueFlag.equals("Y"))
                {
                    System.out.println("Progress...");
                    //DB DELETE 수행
                    for(int i=0; i<hostCount; i++)
                    {
                        if(server_DBcontrol[i].conn != null)
                        {
                            server_DBcontrol[i].deleteRawDataAll();
                        }
                    }

                    //DB AUTO_INCREMENT 1로 초기화
                    for(int i=0; i<hostCount; i++)
                    {
                        if(server_DBcontrol[i].conn != null)
                        {
                            server_DBcontrol[i].initIncrement();
                        }
                    }

                    //DB INSERT 수행.
                    try
                    {
                        //rawData 전체 라인 수 읽기
                        InputStream in = getClass().getResourceAsStream("/resource/rawData.txt");
                        BufferedReader countFile = new BufferedReader(new InputStreamReader(in));
                        int Totalline = 0;
                        while(countFile.readLine() != null)
                        {
                            Totalline++;
                        }

                        //txt파일에서 한줄씩 읽어오면서 INSERT수행
                        InputStream inin = getClass().getResourceAsStream("/resource/rawData.txt");
                        BufferedReader inFile = new BufferedReader(new InputStreamReader(inin));
                        String lineString;
                        int linePointer = 0;
                        int dbPointer = 0;
                        int connPointer = 1;
                        while((lineString = inFile.readLine()) != null)
                        {
                            //sql에서 '를 인식하지 못하는 문제때문에 ''으로 문자열 재배치 후 INSERT 수행
                            lineString = lineString.replace("\'", "\'\'").replace("\"", "\"\"");
                            server_DBcontrol[dbPointer].insertData(lineString);
                            linePointer++;
                            if(connPointer == connectCount)
                            {
                                while((lineString = inFile.readLine()) != null)
                                {
                                    lineString = lineString.replace("\'", "\'\'").replace("\"", "\"\"");
                                    server_DBcontrol[dbPointer].insertData(lineString);
                                }
                            }

                            if(linePointer == Totalline/connectCount)
                            {
                                while(true)
                                {
                                    dbPointer++;
                                    if(server_DBcontrol[dbPointer].conn != null)
                                    {
                                        connPointer++;
                                        break;
                                    }
                                }
                                linePointer = 0;
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        System.out.println("rawData.txt not found");
                    }
                    System.out.println("Complete!");
                    break;
                }
                else if(conntinueFlag.equals("N"))
                {
                    System.out.println("Job canceled. You will return to the main menu.");
                    break;
                }
                else
                {
                    System.out.println("Please enter only Y or N.");
                }
            }
        }
    }
}
