package Host;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class HostController
{
    public int getHostCount()
    {
        int hostcount = 0;
        try
        {
            InputStream in = getClass().getResourceAsStream("/resource/hostInfo.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while(br.readLine() != null)
                hostcount++;

        } catch(Exception ex)
        {
            System.out.println("can't find hostInfo.csv");
        }

        //호스트카운트는 파일수에서 한줄을빼야 구할 수 있다.
        return hostcount-1;
    }

    public HostInfo[] getHostInfo()
    {
        int hostCount = getHostCount();
        HostInfo[] HostInfomation = new HostInfo[hostCount];
        try
        {
            InputStream in = getClass().getResourceAsStream("/resource/hostInfo.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            br.readLine();
            for(int i=0; i<hostCount; i++)
            {
                String[] temp = br.readLine().split(",");
                HostInfomation[i] = new HostInfo(temp[0],temp[1],temp[2],temp[3],temp[4],temp[5]);
            }
            return HostInfomation;
        }
        catch (Exception e)
        {
            System.out.println("can't find hostInfo.csv");
        }

        return null;
    }

    public void printHostInfo()
    {
        int hostCount = getHostCount();
        HostInfo[] HostInfomation = new HostInfo[hostCount];
        try
        {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resource/hostInfo.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            br.readLine();
            System.out.printf("%s %10s %13s %12s","serverName","host","DBPort", "AppPort\n");
            for(int i=0; i<hostCount; i++)
            {
                String[] temp = br.readLine().split(",");
                System.out.printf("%s %15s %10s %10s\n",temp[0], temp[1],temp[2], temp[3]);

            }
        }
        catch (Exception e)
        {
            System.out.println("can't find hostInfo.csv");
        }
    }

    public class HostInfo
    {
        public String serverName;
        public String host;
        public String dbPort;
        public String AppPort;
        public String ID;
        public String PW;

        public HostInfo(String NAME, String HOST, String DBPORT, String APPPORT, String ID, String PW)
        {
            this.serverName = NAME;
            this.host = HOST;
            this.dbPort = DBPORT;
            this.AppPort = APPPORT;
            this.ID = ID;
            this.PW = PW;
        }
    }
}
