package Operation;

import Host.HostController;
import RMI.RemoteClient;
import RMI.RemoteInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteWordCounting
{
    class ThreadingWordCounting extends Thread
    {
        int i;
        HostController.HostInfo[] hostInfo;
        RemoteInterface[] inf;

        public ThreadingWordCounting(int i,HostController.HostInfo[] hostInfo,RemoteInterface[] inf)
        {
           this.i = i;
           this.hostInfo = hostInfo;
           this.inf = inf;
        }

        public void Task()
        {
            try
            {
                    if(inf[i] != null )
                    {
                        System.out.println(hostInfo[i].serverName + " WordCounting Start!");
                        inf[i].doRemoteWordCounting();
                        System.out.println(hostInfo[i].serverName + " WordCounting Complete!");
                    }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
                System.out.println("RMI Fail");
            }
        }

        public void run()
        {
            Task();
        }
    }
    public void doRemoteWordCounting()
    {
        //호스트 정보 불러오기
        HostController hostController = new HostController();
        //호스트 총 갯수 불러오기
        int hostCount = hostController.getHostCount();
        //호스트 정보를 저장하기 위한 배열 선언
        HostController.HostInfo[] hostInfo = hostController.getHostInfo();

        //원격메소드호출을 위한 객체배열생성
        RemoteClient[] client = new RemoteClient[hostCount];
        RemoteInterface[] inf = new RemoteInterface[hostCount];

        for(int i=0;i<hostCount;i++)
        {
            client[i] = new RemoteClient();
        }

        //각호스트에 접근하며 RMI객체 생성
        for(int i=0;i<hostCount;i++)
        {
                inf[i] = client[i].getConnect(hostInfo[i].host, Integer.parseInt(hostInfo[i].AppPort));
                if(inf[i] == null)
                {
                    System.out.println(hostInfo[i].serverName + " RMI Connect Fail");
                }
                else
                {
                    System.out.println(hostInfo[i].serverName + " RMI Ready!");
                }
        }
        int connCount=0;
        for(int i=0;i<hostCount;i++)
        {
            if (inf[i] != null)
            {
                connCount++;
            }
        }
        if(connCount == 0)
        {
            System.out.println("Unable to connect to all servers. please check again");
            return;
        }

        //각 호스트에 원격으로 wordCounting함수 실행명령 하달
        try
        {
            Thread[] thread =new Thread[hostCount];
            for(int i=0;i<hostCount;i++)
            {
                if(inf[i] != null )
                {
                   thread[i] = new ThreadingWordCounting(i,hostInfo,inf);
                   thread[i].start();
                }
            }
            try
            {
                for(int i=0;i<hostCount;i++)
                {
                    if(inf[i] != null )
                    {
                        thread[i].join();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Thread Error! RemoteWordCounting Fail!");
            }
        }
        catch (Exception e)
        {
            System.out.println("Thread Error! Thread Runtime Error!");
        }

    }
}
